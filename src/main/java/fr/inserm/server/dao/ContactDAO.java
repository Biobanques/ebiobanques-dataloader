package fr.inserm.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import fr.inserm.server.model.Contact;

/**
 * DAO for contact access to the object contact
 * 
 * @author nmalservet.
 * 
 */
public class ContactDAO extends AbstractDAO {

	private static final Logger LOGGER = Logger.getLogger(ContactDAO.class);

	/**
	 * return the entire list of contact.
	 * 
	 * @return
	 */
	public List<Contact> loadAll() {
		List<Contact> list = new ArrayList<Contact>();
		DBCollection coll = getCollection("contact");
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
			LOGGER.error("pb parcours contact");
			ex.printStackTrace();
		} finally {
			cursor.close();
		}
		return list;
	}

	/**
	 * return the entire list of contact.
	 * 
	 * @return
	 */
	public Contact load(String contactId) {
		Contact result = null;
		DBCollection coll = getCollection("contact");
		BasicDBObject query = new BasicDBObject(
				Contact.FieldsEnum.id.toString(), contactId);
		DBCursor cursor = coll.find(query);
		try {
			while (cursor.hasNext()) {
				DBObject dbObject = cursor.next();
				result = dbObjectToPojo(dbObject);
			}
		} catch (Exception ex) {
			LOGGER.error("pb parcours contact");
		} finally {
			cursor.close();
		}
		return result;
	}

	/**
	 * get a contact pojo from dbobject contact.
	 * 
	 * @param dbObject
	 * @return
	 */
	private Contact dbObjectToPojo(DBObject dbObject) {
		if (dbObject != null) {
			Contact contact = new Contact();
			for (Contact.FieldsEnum field : Contact.FieldsEnum.values()) {
				Object object = dbObject.get(field.toString());
				if (object != null) {
					contact.setValue(field, object.toString());
				}
			}
			return contact;
		} else {
			return null;
		}
	}
}
