package fr.inserm.server.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import fr.inserm.bean.v2.EchantillonBean;
import fr.inserm.bean.v2.FormatDefinition;
import fr.inserm.exporter.Crypter;
import fr.inserm.server.bean.AnomalieBean;
import fr.inserm.server.bean.FileInputXML;
import fr.inserm.server.bean.FunctionalObjectType;
import fr.inserm.server.bean.LevelAnomalie;
import fr.inserm.server.bean.SiteXML;
import fr.inserm.server.exception.BadFormatInsermFileException;
import fr.inserm.server.exception.NoSiteFoundException;
import fr.inserm.server.manager.EchantillonManager;
import fr.inserm.server.manager.FileImportedManager;
import fr.inserm.tools.StringFileTools;

/**
 * parser du format xml <br>
 * prend en compte la derniere version du format inserm-xml 0.4
 * 
 * @author nicolas
 * 
 */
public class FormatXMLParser {

	private final static Logger LOGGER = Logger.getLogger(FormatXMLParser.class);

	static org.jdom.Document document;
	static Element racine;

	/**
	 * parsing xml et mise en bean. <br>
	 * TODO remonter des exceptions si pas trouvé ou pas formatable.
	 * 
	 * @param xmlResult
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static FileInputXML convertXml2Bean(File file, String decryptPassPhrase) throws JDOMException,
			BadFormatInsermFileException, IOException {
		FileInputXML fileInputXML = null;
		if (file == null) {
			LOGGER.warn("file string null");
			return null;
		}
		/**
		 * decryptage du fichier si necessaire
		 */
		String decryptedFichier = "";
		if (file.getName().endsWith(".encrypted")) {
			String fichierEncrypted = StringFileTools.fileToString(file);
			decryptedFichier = Crypter.decrypt(fichierEncrypted, decryptPassPhrase);
		} else {
			decryptedFichier = StringFileTools.fileToString(file);
			LOGGER.info("non encrypted file : " + file.getName() + ";");
		}
		/**
		 * construction du xml
		 */
		SAXBuilder sxb = new SAXBuilder();
		InputStream is = new ByteArrayInputStream(decryptedFichier.getBytes("UTF8"));
		document = sxb.build(is);
		is.close();
		fileInputXML = new FileInputXML();
		Element importElement = document.getRootElement();
		// check si l element import est bien present en racine
		if (!importElement.getName().equals("import")) {
			LOGGER.warn("element import non present");
			throw new BadFormatInsermFileException();
		}
		Element dateElement = importElement.getChild("date");
		fileInputXML.setDateImport(new Date());
		fileInputXML.setExtractionId(dateElement.getValue());
		fileInputXML.setFormatVersion(2);
		Element siteElement = importElement.getChild("site");
		Element idSite = siteElement.getChild("id");
		Element nameSite = siteElement.getChild("name");
		Element finessSite = siteElement.getChild("finess");
		SiteXML site = new SiteXML();
		int idSit = Integer.parseInt(idSite.getText());
		site.setId(idSit);
		site.setFiness(finessSite.getText());
		site.setName(nameSite.getText());
		fileInputXML.setSite(site);
		Element listEchantillons = importElement.getChild("echantillons");
		if (listEchantillons == null || listEchantillons.getContentSize() < 1) {
			LOGGER.warn("liste d echantillons xml nulle ou vide dnas le fichier");
		} else {
			List<EchantillonBean> echasB = new ArrayList<EchantillonBean>();
			List<Element> echas = listEchantillons.getChildren();
			if (echas != null && echas.size() > 0) {
				for (Element element : echas) {
					EchantillonBean ech = new EchantillonBean();
					List<Element> childs = element.getChildren();
					for (Element object : childs) {
						for (FormatDefinition fd : FormatDefinition.values()) {
							if (fd.name().equals(object.getName())) {
								ech.addValue(fd, object.getText());
							}
						}
						// cas des notes
						if (object.getName().equals("notes")) {
							List<Element> notes = object.getChildren();
							for (Element note : notes) {
								ech.addNote(note.getName(), note.getValue());
							}
						}
					}

					echasB.add(ech);
				}
				fileInputXML.setEchantillons(echasB);
			} else {
				LOGGER.warn("aucun echantillon dans le noeud xml echantillons!");
			}
		}
		return fileInputXML;
	}

	/**
	 * save each sample into file input.
	 * 
	 * @return the count of samples in error
	 * @throws NoSiteFoundException
	 */
	public static List<AnomalieBean> saveFileInput(FileInputXML input) {
		List<AnomalieBean> errors = new ArrayList<AnomalieBean>();
		if (input == null) {
			errors.add(new AnomalieBean(LevelAnomalie.error, "Aucun input fourni:", FunctionalObjectType.file));
			return errors;
		}
		if (input.getSite() == null) {
			errors.add(new AnomalieBean(LevelAnomalie.error, "Aucun site fourni dans l input:",
					FunctionalObjectType.file));
		}
		// save du file import
		try {
			String fileId = FileImportedManager.addFileImported(input);
			if (!fileId.equals("0")) {
				// save de chaque echantillon
				List<EchantillonBean> list = input.getEchantillons();
				LOGGER.debug("saving " + list.size() + " echantillons");
				for (EchantillonBean echantillon : list) {
					AnomalieBean res = EchantillonManager.addEchantillon(echantillon, fileId, ""
							+ input.getSite().getId());
					if (res != null)
						errors.add(res);
				}
			}
		} catch (NoSiteFoundException e) {
			errors.add(new AnomalieBean(LevelAnomalie.major,
					"Aucun site trouvé en base avec l id fourni dans le fichier:" + input.getSite().getId(),
					FunctionalObjectType.file));
		} catch (Exception e) {
			LOGGER.warn("exception inconnue : " + e.getMessage() + e.getCause().getMessage());
			errors.add(new AnomalieBean(LevelAnomalie.warn, "exception inconnue:" + e.getMessage(),
					FunctionalObjectType.file));
		}
		return errors;
	}
}
