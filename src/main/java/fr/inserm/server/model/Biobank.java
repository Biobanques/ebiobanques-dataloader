package fr.inserm.server.model;

//@Table(name = "biobank")
public class Biobank extends AbstractModel {

	public enum FieldsEnum {
		id, identifier, name, collection_name, collection_id, folder_reception, folder_done, passphrase, contact_id;
	}

	public String getValue(FieldsEnum key) {
		return getValue(key.toString());
	}

	public void setValue(FieldsEnum key, String value) {
		setValue(key.toString(), value);
	}

	public Biobank() {
	}

}
