package fr.inserm.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.inserm.server.model.Biobank;

/**
 * DAO for biobank access to the object biobank
 * 
 * @author nmalservet.
 * 
 */
public class BiobankDAO extends AbstractDAO {

	private static final Logger LOGGER = Logger.getLogger(BiobankDAO.class);

	/**
	 * return the entire list of biobank.
	 * 
	 * @return
	 */
	public List<Biobank> loadAll() {
		List<Biobank> list = new ArrayList<Biobank>();
		DBCollection coll = getCollection("biobank");
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
	public Biobank load(String biobankId) {
		Biobank result = null;
		DBCollection coll = getCollection("biobank");
		BasicDBObject query = new BasicDBObject(Biobank.FieldsEnum.id.toString(), biobankId);
		DBCursor cursor = coll.find(query);
		try {
			while (cursor.hasNext()) {
				DBObject dbObject = cursor.next();
				result = dbObjectToPojo(dbObject);
			}
		} catch (Exception ex) {
			LOGGER.error("pb parcours biobank");
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
	private Biobank dbObjectToPojo(DBObject dbObject) {
		if (dbObject != null) {
			Biobank bbank = new Biobank();
			for (Biobank.FieldsEnum field : Biobank.FieldsEnum.values()) {
				Object object = dbObject.get(field.toString());
				if (object != null) {
					bbank.setValue(field, object.toString());
				}
			}
			return bbank;
		} else {
			return null;
		}
	}
}
