package fr.inserm.server.model;

//@Table(name = "File_imported")
public class FileImported extends AbstractModel {

	public enum FieldsEnum {
		id, biobank_id, extraction_id, given_name, generated_name, version_format, suffix_type, date_import, _id

	}

	public String getValue(FieldsEnum key) {
		return getValue(key.toString());
	}

	public void setValue(FieldsEnum key, String value) {
		setValue(key.toString(), value);
	}

	public FileImported() {
	}

}
