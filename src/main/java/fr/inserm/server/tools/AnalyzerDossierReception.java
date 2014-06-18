package fr.inserm.server.tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import fr.inserm.exporter.GZIPCompress;
import fr.inserm.server.bean.AnomalieBean;
import fr.inserm.server.bean.FileInputXML;
import fr.inserm.server.bean.FunctionalObjectType;
import fr.inserm.server.bean.LevelAnomalie;
import fr.inserm.server.dao.BiobankDAO;
import fr.inserm.server.exception.BadFormatInsermFileException;
import fr.inserm.server.exception.FolderInexistException;
import fr.inserm.server.exception.NoSiteFoundException;
import fr.inserm.server.manager.AbstractManager;
import fr.inserm.server.manager.EchantillonManager;
import fr.inserm.server.model.Biobank;
import fr.inserm.server.model.Biobank.FieldsEnum;
import fr.inserm.server.parser.FormatXMLParser;
import fr.inserm.tools.StringFileTools;

/**
 * classe d analyse du dossier de reception.<br>
 * Par defaut doit etre vide.<br>
 * 
 * @author nicolas
 * 
 */
public class AnalyzerDossierReception extends AbstractManager {

	private static final Logger LOGGER = Logger
			.getLogger(AnalyzerDossierReception.class);

	private AnalyzerDossierReception() {

	}

	private static int nbech = 0;
	static Date date = new Date();
	static long time = date.getTime();

	/**
	 * dossier de fichiers xml enregsitres.
	 */

	/**
	 * analyse le dossier de reception pour chaque biobank<br>
	 * s il n y apas de fichiers, ne fait rien, sinon envoi e de mail FIXME : la
	 * purge d echantillon intervient apres cahque insert de fichier, la
	 * reporter pour l autre laoder en fin d import total..
	 */
	public static synchronized void analyze() {
		LOGGER.info("analyse des dossiers biobank");
		// liste des anomalies detectees
		List<AnomalieBean> anomalies = new ArrayList<AnomalieBean>();
		// get de chaque biobanque
		List<Biobank> biobanks;
		BiobankDAO dao = getBiobankDao();
		biobanks = dao.loadAll();

		for (Biobank biobank : biobanks) {
			LOGGER.info("-analyzing biobank name:"
					+ biobank.getValue(FieldsEnum.identifier) + ",identifier:"
					+ biobank.getValue(FieldsEnum.identifier));
			File[] filesReceveid;

			try {
				filesReceveid = getFilesOnReception(
						biobank.getValue(FieldsEnum.folder_reception),
						biobank.getValue(FieldsEnum.folder_done));

				if (filesReceveid != null) {
					if (filesReceveid.length == 0) {
						LOGGER.info("aucun fichier a traiter");
					} else {

						LOGGER.info("-number of files received :"
								+ filesReceveid.length);
						String filesName = "";
						for (File file : filesReceveid) {
							filesName += "<li>" + file.getName() + "</li>";
						}
						// envoi de mail pour demarrage de traitement à l admin
						MailTools.sendMailNotifyStart(time,
								filesReceveid.length, filesName);
						List<String> filesNames = new ArrayList<String>();
						List<String> filesSaved = new ArrayList<String>();
						List<String> filesUnsaved = new ArrayList<String>();
						List<String> logsFiles = new ArrayList<String>();
						String prefixName = "ACK_" + time + "_";
						for (File file : filesReceveid) {
							LOGGER.info("traitement fichier : - "
									+ file.getName());
							if (file.isFile()) {
								filesNames.add(file.getName());
								// si le type de fichier est autre que .xml,ou
								// .encrypted, ou si .encrypted mais pas de
								// .xml dans le nom, deplace dans logs
								if ((!file.getName().endsWith(".xml")
										&& !file.getName().endsWith(
												".encrypted") || file.getName()
										.endsWith(".encrypted")
										&& !file.getName().contains(".xml"))

								) {
									LOGGER.info("Fichier non valide : "
											+ file.getName());
									logsFiles.add(file.getName());
									StringFileTools.moveFile(file, biobank
											.getValue(FieldsEnum.folder_done),
											"log_" + prefixName);

								} else {
									try {
										anomalies
												.addAll(injectDataFile(
														file,
														prefixName,
														biobank.getValue(FieldsEnum.passphrase)));
										// deplacement dans saved
										filesSaved.add(file.getName());

										StringFileTools
												.moveFile(
														file,
														biobank.getValue(FieldsEnum.folder_done),
														"saved_" + prefixName);
									} catch (Exception e) {
										filesUnsaved.add(file.getName()
												+ "(Error:IOException> )");
										// deplacement dans repo unsaved
										StringFileTools
												.moveFile(
														file,
														biobank.getValue(FieldsEnum.folder_done),
														"unsaved_" + prefixName);
										anomalies.add(new AnomalieBean(
												LevelAnomalie.error,
												"Fichier non injecté car Exception:"
														+ e.getMessage()
														+ " on"
														+ file.getName(),
												FunctionalObjectType.file));
									}
								}
							}
							if (file.isDirectory()) {
								LOGGER.warn("il y a un dossier qui traine dans le repertoire!! ->:"
										+ file.getName());
							}
							getFileDetectedDao().remove(file.getName());
							LOGGER.debug("-->fin traitement fichier : - "
									+ file.getName());

						}
						EchantillonManager.purgeEchantillon(biobank
								.getValue(FieldsEnum.id));
						// calcul temps de traitement
						Date dateFin = new Date();
						long millis = dateFin.getTime() - date.getTime();
						String tempsTraitement = String
								.format("%d min, %d sec",
										TimeUnit.MILLISECONDS.toMinutes(millis),
										TimeUnit.MILLISECONDS.toSeconds(millis)
												- TimeUnit.MINUTES
														.toSeconds(TimeUnit.MILLISECONDS
																.toMinutes(millis)));
						// envoi de fmail pour fin de traitement a l admin et au
						// user
						MailTools.sendMailNotifyEnd(time, tempsTraitement,
								anomalies,
								biobank.getValue(FieldsEnum.identifier),
								filesReceveid.length, filesNames, filesSaved,
								filesUnsaved, logsFiles, nbech);

					}
				} else {
					LOGGER.error("files null");
				}
			} catch (FolderInexistException e) {
				LOGGER.error("un dossier est inexistant");
			}

		}
		LOGGER.info("fin d'analyse");

	}

	/**
	 * parse le fichier et enregistre les donnees en base.
	 * 
	 * @param fileName
	 * @return number of samples in error
	 * @throws NoSiteFoundException
	 * @throws IOException
	 * @throws JDOMException
	 * @throws BadFormatInsermFileException
	 */
	public static List<AnomalieBean> injectDataFile(File file,
			String prefixName, String decryptePassPhrase) throws JDOMException,
			IOException, BadFormatInsermFileException {
		List<AnomalieBean> errors = new ArrayList<AnomalieBean>();
		if (file.getName().endsWith(".xml")
				|| file.getName().endsWith(".encrypted")) {
			FileInputXML xmlBean = null;
			try {
				xmlBean = FormatXMLParser.convertXml2Bean(file,
						decryptePassPhrase);
				nbech += xmlBean.getEchantillons().size();

			} catch (Exception e) {
				LOGGER.warn("le fichier recu n est pas convertible en bean");

				errors.add(new AnomalieBean(LevelAnomalie.error,
						"le fichier recu n est pas convertible en bean"
								+ e.getMessage(), FunctionalObjectType.file));
			}
			if (xmlBean != null) {

				xmlBean.setNameFile(file.getName());
				xmlBean.setGeneratedName(prefixName + file.getName());
				errors.addAll(FormatXMLParser.saveFileInput(xmlBean));
			} else {
				LOGGER.warn("pas de donnes a enregistrer!");
				errors.add(new AnomalieBean(
						LevelAnomalie.error,
						"le fichier recu n est pas un fichier xml, suffixe incorrect",
						FunctionalObjectType.file));
			}
		} else {
			errors.add(new AnomalieBean(
					LevelAnomalie.error,
					"le fichier recu n est pas un fichier xml, suffixe incorrect",
					FunctionalObjectType.file));
		}
		return errors;
	}

	public static String fileToString(File file) {
		String result = "";
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(file);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);

			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				result += strLine;
			}
			// Close the input stream
			br.close();

			isr.close();
			in.close();
			fstream.close();
		} catch (Exception e) {// Catch exception if any
			LOGGER.error("pb de conversion en string" + e.getMessage());
		}
		return result;
	}

	/**
	 * get an array of file into path folder reception
	 * 
	 * @param pathFolderReception
	 * @return
	 * @throws FolderInexistException
	 */
	public static File[] getFilesOnReception(String pathFolderReception,
			String pathFolderDone) throws FolderInexistException {
		if (pathFolderReception == null || pathFolderDone == null) {
			LOGGER.error("folder null for pathFolderReception:"
					+ pathFolderReception + ", pathFolderDone" + pathFolderDone);
			throw new FolderInexistException();
		}
		File folder = new File(pathFolderReception);
		if (!folder.exists()) {
			LOGGER.error("dossier de reception inexist:" + pathFolderReception);
			throw new FolderInexistException();
		}
		File[] filesList = folder.listFiles();

		for (File f : filesList) {

			if (f.getName().endsWith(".gz")) {
				GZIPCompress.uncompress(f);
				StringFileTools.moveFile(f, pathFolderDone, "ARC_GZ_" + time
						+ "_");
				// f.delete();
			}
		}
		File[] newList = folder.listFiles();
		return newList;

	}
}
