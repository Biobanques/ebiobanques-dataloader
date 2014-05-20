package fr.inserm.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.inserm.server.model.FileImported;

/**
 * DAO for fileImported
 * 
 * @author nicolas
 * 
 */
public class FileImportedDAO extends AbstractDAO {

	private final static Logger LOGGER = Logger.getLogger(FileImportedDAO.class);

	private static String collectionName = "file_imported";

	/**
	 * return the entire list of biobank.
	 * 
	 * @return
	 */
	public FileImported getLastFileImported(String biobankId) {
		FileImported result = null;
		DBCollection coll = getCollection(collectionName);
		BasicDBObject query = new BasicDBObject(FileImported.FieldsEnum.biobank_id.toString(), biobankId);
		DBCursor cursor = coll.find(query);
		try {
			while (cursor.hasNext()) {
				DBObject dbObject = cursor.next();
				if (dbObject != null) {
					result = dbObjectToPojo(dbObject);
				}
			}
		} catch (Exception ex) {
			LOGGER.error("pb parcours file imported" + ex.getMessage());
		} finally {
			cursor.close();
		}
		return result;
	}

	/**
	 * return the entire list of biobank.
	 * 
	 * @return
	 */
	public List<FileImported> getFilesImportedByExtractionId(String extractionId) {
		List<FileImported> list = new ArrayList<FileImported>();
		DBCollection coll = getCollection(collectionName);
		BasicDBObject query = new BasicDBObject(FileImported.FieldsEnum.extraction_id.toString(), extractionId);
		DBCursor cursor = coll.find(query);
		try {
			while (cursor.hasNext()) {
				DBObject dbObject = cursor.next();
				list.add(dbObjectToPojo(dbObject));
			}
		} catch (Exception ex) {
			LOGGER.error("pb parcours file imported");
		} finally {
			cursor.close();
		}
		return list;
	}

	/**
	 * return the entire list of files for one biobank but witout this extraction id
	 * 
	 * @return
	 */
	public List<FileImported> getFilesImportedByBiobankExceptExtractionId(String biobankId, String extractionId) {
		List<FileImported> list = new ArrayList<FileImported>();
		DBCollection coll = getCollection(collectionName);
		BasicDBObject query = new BasicDBObject(FileImported.FieldsEnum.extraction_id.toString(), new BasicDBObject(
				"$ne", extractionId));
		query.put(FileImported.FieldsEnum.biobank_id.toString(), biobankId);
		DBCursor cursor = coll.find(query);
		try {
			while (cursor.hasNext()) {
				DBObject dbObject = cursor.next();
				list.add(dbObjectToPojo(dbObject));
			}
		} catch (Exception ex) {
			LOGGER.error("pb parcours file imported:" + ex.getMessage());
		} finally {
			cursor.close();
		}
		return list;
	}

	/**
	 * save the file imported. Set the id generated into the object file
	 * 
	 * @param file
	 * @return the _id generated
	 */
	public String save(FileImported file) {
		DBCollection coll = getCollection(collectionName);
		BasicDBObject doc = new BasicDBObject();
		for (FileImported.FieldsEnum key : FileImported.FieldsEnum.values()) {
			try {
				String value = file.getValue(key);
				if (value != null) {
					// trim pour supprimer les metacharacetres genants d
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
		coll.insert(doc);
		ObjectId id = (ObjectId) doc.get("_id");
		return id.toString();
	}

	/**
	 * get a file imported pojo from dbobject file imported.
	 * 
	 * @param dbObject
	 * @return
	 */
	public static FileImported dbObjectToPojo(DBObject dbObject) {
		FileImported fImp = new FileImported();
		for (FileImported.FieldsEnum field : FileImported.FieldsEnum.values()) {
			Object value = dbObject.get(field.toString());
			if (value != null) {
				fImp.setValue(field, value.toString());
			}
		}
		return fImp;
	}

}
