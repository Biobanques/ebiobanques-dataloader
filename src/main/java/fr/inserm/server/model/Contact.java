package fr.inserm.server.model;

//@Table(name = "contact")
public class Contact extends AbstractModel {

	public enum FieldsEnum {
		id, first_name, last_name, email, pays, code_postal, inactive;
	}

	public String getValue(FieldsEnum key) {
		return getValue(key.toString());
	}

	public void setValue(FieldsEnum key, String value) {
		setValue(key.toString(), value);
	}

	public Contact() {
	}

}
