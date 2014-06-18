package fr.inserm.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.inserm.server.model.FileDetected;

/**
 * DAO for biobank access to the object biobank
 * 
 * @author nmalservet.
 * 
 */
public class FileDetectedDAO extends AbstractDAO {

	private static final Logger LOGGER = Logger
			.getLogger(FileDetectedDAO.class);
	private static String collectionName = "file_detected";

	/**
	 * return the entire list of biobank.
	 * 
	 * @return
	 */
	public List<FileDetected> loadAll() {
		List<FileDetected> list = new ArrayList<FileDetected>();
		DBCollection coll = getCollection("fileDetected");
		DBCursor cursor = coll.find();
		try {
			while (cursor.hasNext()) {
				DBObject dbObject = cursor.next();
				if (dbObject != null) {
					list.add(dbObjectToPojo(dbObject));
				} else {
					LOGGER.error("dbobject null");
				}
			}
		} catch (Exception ex) {
			LOGGER.error("pb parcours biobank");
			ex.printStackTrace();
		} finally {
			cursor.close();
		}
		return list;
	}

	/**
	 * return the entire list of biobank.
	 * 
	 * @return
	 */
	public FileDetected load(String fileDetectedName) {
		FileDetected result = null;
		DBCollection coll = getCollection("file_detected");
		BasicDBObject query = new BasicDBObject(
				FileDetected.FieldsEnum.file_name.toString(), fileDetectedName);
		DBCursor cursor = coll.find(query);

		try {
			while (cursor.hasNext()) {
				DBObject dbObject = cursor.next();
				result = dbObjectToPojo(dbObject);
			}
		} catch (Exception ex) {
			LOGGER.error("pb parcours fileDetected");
		} finally {
			cursor.close();
		}
		return result;
	}

	/**
	 * get a biobank pojo from dbobject biobank.
	 * 
	 * @param dbObject
	 * @return
	 */
	private FileDetected dbObjectToPojo(DBObject dbObject) {
		if (dbObject != null) {
			FileDetected fileDetected = new FileDetected();
			for (FileDetected.FieldsEnum field : FileDetected.FieldsEnum
					.values()) {
				Object object = dbObject.get(field.toString());
				if (object != null) {
					fileDetected.setValue(field, object.toString());
				}
			}
			return fileDetected;
		} else {
			return null;
		}
	}

	public void save(FileDetected fileDetected) {
		DBCollection coll = getCollection(collectionName);
		BasicDBObject doc = new BasicDBObject();
		for (FileDetected.FieldsEnum key : FileDetected.FieldsEnum.values()) {
			try {
				String value = fileDetected.getValue(key);
				if (value != null) {
					// trim pour supprimer les metacharacteres genants d
					// edebut et fin
					value = value.trim();
					if (value != null && value.length() > 0) {
						doc.append(key.toString(), value);
					}
					coll.insert(doc);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void remove(String name) {
		FileDetected result = null;
		DBCollection coll = getCollection("file_detected");
		BasicDBObject query = new BasicDBObject(
				FileDetected.FieldsEnum.file_name.toString(), name);
		coll.remove(query);

	}
}
