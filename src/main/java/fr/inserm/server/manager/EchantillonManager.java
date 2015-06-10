package fr.inserm.server.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fr.inserm.bean.v2.EchantillonBean;
import fr.inserm.bean.v2.FormatDefinition;
import fr.inserm.server.bean.AnomalieBean;
import fr.inserm.server.bean.FileImportedBean;
import fr.inserm.server.bean.FunctionalObjectType;
import fr.inserm.server.bean.LevelAnomalie;
import fr.inserm.server.model.Echantillon;
import fr.inserm.server.model.FileImported;

/**
 * class to load data about echantillons.
 * 
 * @author nicolas
 * 
 */
public class EchantillonManager extends AbstractManager {

	private static final Logger LOGGER = Logger
			.getLogger(EchantillonManager.class);

	/**
	 * ajout d echantillone en base. return AnomalieBean if problem return null
	 * if ok
	 * 
	 * @param xmlBean
	 * @param fileId
	 * @param siteId
	 *            site uploading data.
	 * @return an anomalieBean if add echantillon failed,else null
	 */
	public static AnomalieBean addEchantillon(EchantillonBean bean,
			String fileId, String biobankId) {
		AnomalieBean result = null;
		Echantillon pojo = null;
		if (bean == null)
			return result;
		try {
			pojo = beanToPojo(bean);
			if (pojo != null) {
				pojo.setValue(Echantillon.Fields.file_imported_id.toString(),
						fileId);
				pojo.setValue(Echantillon.Fields.biobank_id.toString(),
						biobankId);
				getEchantillonDao().save(pojo);
			} else {
				LOGGER.debug("Echantillon non sauvegardé, verifier les champs obligatoires");
				result = new AnomalieBean(LevelAnomalie.minor,
						"Echantillon non sauvegardé",
						FunctionalObjectType.sample, new Date());
				return result;
			}
		} catch (Exception e) {
			LOGGER.debug("Pb de sauvegarde d echantillon" + e.getMessage());
			result = new AnomalieBean(LevelAnomalie.minor,
					"Pb de sauvegarde d echantillon" + e.getMessage(),
					FunctionalObjectType.sample, new Date());
		}
		return result;
	}

	/**
	 * pour chaque valeur du bean, mapping avec un attribut existant
	 * 
	 * @param bean
	 * @return
	 */
	public static Echantillon beanToPojo(EchantillonBean bean) throws Exception {
		Echantillon pojo = new Echantillon();
		Map<FormatDefinition, String> map = bean.getMapValues();
		try {
			for (FormatDefinition key : map.keySet()) {
				String value = map.get(key);
				if (!value.isEmpty()) {
					pojo.setValue(key.toString(), value);
				}
			}
			pojo.setNotes(bean.getNotes());
		} catch (Exception e) {
			LOGGER.error("ERREUR - l'echantillon est null en raison d'une erreur de parsing"
					+ e.getMessage());
			return null;
		}
		return pojo;
	}

	/**
	 * delete for a biobank all samples not in the last import FIXME gerer le
	 * mecanisme d id unique
	 * 
	 * @param biobankId
	 * @return true if purge correcte si error then false
	 */
	public static boolean purgeEchantillon(final String biobankId) {
		boolean result = true;
		if (biobankId == null) {
			return false;
		}
		try {
			FileImportedBean lastImport;
			lastImport = FileImportedManager.getLastImport(biobankId);
			if (lastImport == null) {
				LOGGER.debug("Pas de précédent import pour cette biobanque.");
			} else {
				String lastExtId = lastImport.getExtractionId();
				LOGGER.debug("biobank:" + biobankId + "extraction id "
						+ lastExtId + "remove file : ");
				LOGGER.debug("Début de suppression des anciennes données."
						+ lastExtId);
				List<FileImported> listOldImport = getFileImportedDao()
						.getFilesImportedByBiobankExceptExtractionId(biobankId,
								lastExtId);
				if (listOldImport != null && listOldImport.size() > 0) {
					for (FileImported toRemove : listOldImport) {
						LOGGER.debug("biobank:"
								+ biobankId
								+ "extraction id "
								+ lastExtId
								+ "remove file : "
								+ toRemove.getValue(FileImported.FieldsEnum.id
										.toString()));
						getEchantillonDao().removeByFileId(
								toRemove.getValue(FileImported.FieldsEnum._id
										.toString()));
					}
				}
				LOGGER.debug("Les anciennes données ont bien été supprimées");
			}
		} catch (Exception e) {
			LOGGER.error("la suppression des anciens échantillons ne s'est pas déroulée correctement : "
					+ e.getMessage());
			result = false;
		}
		return result;
	}
}
