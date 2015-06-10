package fr.inserm.server.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;

import fr.inserm.server.bean.AnomalieBean;
import fr.inserm.server.bean.FunctionalObjectType;
import fr.inserm.server.bean.LevelAnomalie;
import fr.inserm.server.model.Echantillon;

/**
 * DAO for echantillon.
 * 
 * @author nicolas
 * 
 */
public class EchantillonDAO extends AbstractDAO {

	private final static Logger LOGGER = Logger.getLogger(EchantillonDAO.class);

	private static String collectionName = "echantillon";

	/**
	 * save the file imported. Set the id generated into the object file
	 * 
	 * @param file
	 */
	public AnomalieBean save(Echantillon echantillon) {
		AnomalieBean result = null;
		if (echantillon == null) {
			result = new AnomalieBean(LevelAnomalie.minor,
					"Pb de sauvegarde d echantillon, echantillon null",
					FunctionalObjectType.sample, new Date());
			LOGGER.error("echantillon is null");
			return result;
		}
		// si l echantillon n a pas id interne alors null
		if (echantillon.getValue(Echantillon.Fields.id_sample) == null
				|| echantillon.getValue(Echantillon.Fields.id_sample).isEmpty()) {
			LOGGER.error("echantillon sans id provenant du site emetteur donc incorrect pour la tracabilite");
			result = new AnomalieBean(LevelAnomalie.minor,
					"Echantillon non sauvegardé : pas d'identifiant défini",
					FunctionalObjectType.field, new Date());
			return result;
		}
		DBCollection coll = getCollection(collectionName);
		BasicDBObject doc = new BasicDBObject();
		for (Echantillon.Fields key : Echantillon.Fields.values()) {
			try {
				String value = echantillon.getValue(key);
				if (value != null) {
					// trim pour supprimer les metacharacteres genants d
					// edebut et fin
					value = value.trim();
					if (value != null && value.length() > 0) {
						doc.append(key.toString(), value);
					}
				}
			} catch (Exception e) {
				LOGGER.warn("pb sur champs:" + key + ":" + e.getMessage());
			}
		}
		// ajout des notes
		// aggregation des notes dans un tableau associatif key-value
		ArrayList<BasicDBObject> notesJson = new ArrayList<BasicDBObject>();
		Map<String, String> notes = echantillon.getNotes();
		if (notes != null) {
			for (String key : notes.keySet()) {
				// objet cle
				BasicDBObject noteObj = new BasicDBObject();
				noteObj.put("key", key);
				// objet valeur
				noteObj.put("value", notes.get(key));
				notesJson.add(noteObj);
			}
			doc.put("notes", notesJson);
		}
		coll.insert(doc);
		// ObjectId id = (ObjectId) doc.get("_id");
		return null;
	}

	/**
	 * remove all echantillons by file id.<br>
	 * In MOngo, id is stored with _id
	 * 
	 * @param fileId
	 */
	public boolean removeByFileId(String fileId) {
		if (fileId == null) {
			return false;
		}
		DBCollection coll = getCollection(collectionName);
		BasicDBObject query = new BasicDBObject(
				Echantillon.Fields.file_imported_id.toString(), fileId);
		try {
			coll.remove(query);
		} catch (MongoException me) {
			LOGGER.error("pb remove echantillon by file:" + fileId);
			return false;
		}
		return true;

	}
}
