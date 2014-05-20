package fr.inserm.server.manager;

import org.apache.log4j.Logger;

import fr.inserm.server.bean.FileImportedBean;
import fr.inserm.server.bean.FileInputXML;
import fr.inserm.server.exception.NoSiteFoundException;
import fr.inserm.server.model.Biobank;
import fr.inserm.server.model.FileImported;

/**
 * class to load data about file import.
 * 
 * @author nicolas
 * 
 */
public class FileImportedManager extends AbstractManager {

	private final static Logger LOGGER = Logger.getLogger(FileImportedManager.class);

	/**
	 * return id import. TODO completer avec plus de valeurs.
	 * 
	 * @param input
	 * @return
	 */
	public static String addFileImported(FileInputXML input) throws NoSiteFoundException {
		String result = "0";
		if (input.getSite() == null) {
			LOGGER.warn("site mandatory");
		} else {
			Biobank site = getBiobankDao().load(input.getSite().getId() + "");
			if (site == null) {
				throw new NoSiteFoundException();
			} else {
				FileImported pojo = new FileImported();
				pojo.setValue(FileImported.FieldsEnum.date_import, input.getDateImport().toString());
				pojo.setValue(FileImported.FieldsEnum.version_format, input.getFormatVersion() + "");
				pojo.setValue(FileImported.FieldsEnum.given_name, input.getNameFile());
				pojo.setValue(FileImported.FieldsEnum.generated_name, input.getGeneratedName());
				pojo.setValue(FileImported.FieldsEnum.extraction_id, input.getExtractionId());
				pojo.setValue(FileImported.FieldsEnum.biobank_id, input.getSite().getId() + "");
				pojo.setValue(FileImported.FieldsEnum.suffix_type, 1 + "");
				result = getFileImportedDao().save(pojo);
			}
		}
		return result;
	}

	/**
	 * get the last import file for a biobanke. Return null if nothing found.
	 * 
	 * @return
	 */
	public static FileImportedBean getLastImport(String idBiobank) {
		FileImportedBean bean = null;
		FileImported fi = getFileImportedDao().getLastFileImported(idBiobank);
		if (fi != null) {
			bean = pojoToBean(fi);
		}
		return bean;
	}

	/**
	 * transform pojo to fileImportedBean
	 * 
	 * @param pojo
	 * @return
	 */
	public static FileImportedBean pojoToBean(FileImported pojo) {
		if (pojo == null)
			return null;
		FileImportedBean bean = new FileImportedBean();
		bean.setExtractionId(pojo.getValue(FileImported.FieldsEnum.extraction_id));
		return bean;

	}

}
