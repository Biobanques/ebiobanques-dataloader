package fr.inserm.server;

import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Ignore;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import fr.inserm.server.tools.AbstractTest;
import fr.inserm.server.tools.MongoSingleton;
import fr.inserm.tools.DurationAnalyzer;

@Ignore
public class Test extends AbstractTest {

	private static final Logger LOGGER = Logger.getLogger(Test.class);

	/**
	 * @param args
	 */

	public void insertMassiveSamplesTest() {
		DurationAnalyzer da = new DurationAnalyzer();
		int biobankid = 0;
		insertMassiveSamples(1000);
		findDocuments(biobankid);
		updateSample();
		findDocuments(biobankid);
		removeSamples(biobankid);
		findDocuments(biobankid);
		da.endAnalyse();
	}

	/**
	 * insertion massive d echantillons
	 * 
	 * @param n
	 */

	private void insertMassiveSamples(int n) {
		DBCollection coll = getCollection();
		Date date = new Date();
		LOGGER.debug("start:" + date.toString());
		for (int i = 0; i < n; i++) {
			String randomid = "nm" + date.getTime() + "_" + i;
			String consent = "Y";
			if ((i % 3) == 0) {
				consent = "N";
			}
			int idbiobank = i % 2;
			BasicDBObject doc = new BasicDBObject("id_depositor", "nm_java")
					.append("id_sample", randomid)
					.append("consent_ethical", consent)
					.append("biobank_id", idbiobank)
					.append("notes",
							new BasicDBObject("maladie", "alz").append("note2",
									"352"));

			coll.insert(doc);
		}
		Date date2 = new Date();
		LOGGER.debug("end:" + date2.toString());
	}

	public void findDocuments(int biobankid) {
		LOGGER.debug("--find");
		Date date = new Date();
		LOGGER.debug("start search:" + date.toString());
		DBCollection coll = getCollection();
		LOGGER.debug("count echantillons:" + coll.getCount());
		BasicDBObject query = new BasicDBObject("consent_ethical", "N");
		DBCursor cursor = coll.find(query);
		LOGGER.debug("nb  d echa sans consent:" + cursor.count());
		// echa ss consent et de la biobank 1
		BasicDBObject query2 = new BasicDBObject("consent_ethical", "N");
		query2.append("biobank_id", biobankid);
		DBCursor cursor2 = coll.find(query2);
		LOGGER.debug("nb  d echa sans consent et de la biobank :"
				+ cursor2.count());
		Date date2 = new Date();
		LOGGER.debug("end search:" + date2.toString());
	}

	public void removeSamples(int biobankid) {
		LOGGER.debug("--remove");
		Date date = new Date();
		LOGGER.debug("start remove:" + date.toString());
		// select des echantillons
		DBCollection coll = getCollection();
		BasicDBObject query2 = new BasicDBObject("consent_ethical", "N");
		query2.append("biobank_id", biobankid);
		coll.remove(query2);
		Date date2 = new Date();
		LOGGER.debug("end remove:" + date2.toString());
	}

	private DBCollection getCollection() {
		// MongoClient mongoClient;
		try {
			return MongoSingleton.getDb().getCollection("echantillons");
			// mongoClient = new MongoClient();
			// DB db = mongoClient.getDB("interop");
			// return db.getCollection("echantillons");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void updateSample() {
		LOGGER.debug("--update");
		Date date = new Date();
		LOGGER.debug("start update:" + date.toString());
		DBCollection coll = getCollection();
		BasicDBObject query = new BasicDBObject("consent_ethical", "N");
		BasicDBObject update = new BasicDBObject("consent_ethical", "Y");
		coll.update(query, update);// update one only
		/**
		 * update multi est specifique et doit etre effectué avec des criteres
		 * specifiques
		 */
		// coll.updateMulti(query, update);// update all
		Date date2 = new Date();
		LOGGER.debug("end update:" + date2.toString());

	}
}
