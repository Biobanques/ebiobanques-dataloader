package fr.inserm.server.model;

//@Table(name = "fileDetected")
public class FileDetected extends AbstractModel {

	public enum FieldsEnum {
		file_id, file_name,
	}

	public String getValue(FieldsEnum key) {
		return getValue(key.toString());
	}

	public void setValue(FieldsEnum key, String value) {
		setValue(key.toString(), value);
	}

	public FileDetected() {
	}

}
