package fr.inserm.server.tools;

import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * singleton pour ne pas avoir trop de "connexion ouverte" sur la base mongo et
 * obtenir une erreur de type : "too many files opened"<br>
 * TODO ameliorer via un singleton njecter avec spring?
 * 
 * @author nmalservet
 * 
 */
public class MongoSingleton {

	private static final Logger LOGGER = Logger.getLogger(MongoSingleton.class);

	public MongoSingleton() {
	}

	public static DB db;

	public static DB getDb() {
		if (db == null) {
			LOGGER.info("init singleton");
			MongoClient mongoClient;
			ApplicationResourcesLoader arl = new ApplicationResourcesLoader();
			try {
				MongoCredential credentials = MongoCredential
						.createMongoCRCredential(arl.getMongoUsername(), arl
								.getMongoDatabase(), arl.getMongoPassword()
								.toCharArray());
				mongoClient = new MongoClient(
						new ServerAddress(arl.getMongoUrl(),
								Integer.parseInt(arl.getMongoPort())),
						Arrays.asList(credentials));
				db = mongoClient.getDB(arl.getMongoDatabase());

			} catch (UnknownHostException e) {
				LOGGER.error("error get collection:" + e.getMessage());
			}
		}
		return db;
	}

}
