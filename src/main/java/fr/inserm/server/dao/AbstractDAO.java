package fr.inserm.server.dao;

import com.mongodb.DBCollection;

import fr.inserm.server.tools.MongoSingleton;

/**
 * classe abstraite de dao pour factorsier les methodes des daos d acces aux
 * tables.
 * 
 * @author nicolas
 * 
 */
public class AbstractDAO {
	/**
	 * get de la DBCollection = table au sens MongoDB<br>
	 * NB : si la collection nexiste pas, mongodb peut quand meme travailelr
	 * dessus et la creera au premier import.
	 * 
	 * @param collectionName
	 * @return
	 */
	public DBCollection getCollection(String collectionName) {
		return MongoSingleton.getDb().getCollection(collectionName);
	}

}
