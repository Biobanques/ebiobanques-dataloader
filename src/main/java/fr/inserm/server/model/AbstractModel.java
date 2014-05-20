package fr.inserm.server.model;

import java.util.HashMap;

/**
 * classe abstraite pour model type mongodb avec liste de champs sous form d enum.
 * 
 * @author nmalservet
 * 
 */
public class AbstractModel {

	HashMap<String, String> map = new HashMap<String, String>();

	public void setValue(String key, String value) {
		map.put(key.toString(), value);
	}

	public String getValue(String key) {
		return map.get(key.toString());
	}
}
