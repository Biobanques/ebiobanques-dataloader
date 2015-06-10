package fr.inserm.server.model;

import java.util.Map;

//@Table(name = "echantillon")
public class Echantillon extends AbstractModel {

	/**
	 * la plupart des fields sont identiques a ceux dans le connecteur. A l
	 * exception de ceux supplementaires : "file_imported_id",
	 * 
	 * @author nicolas
	 * 
	 */
	public enum Fields {
		id_depositor, id_family, id_donor, id_sample, consent_ethical, gender, age, pathology, status_sample, collect_date, nature_sample_dna, storage_conditions, quantity, disease_diagnosis, origin, hazard_status, nature_sample_tissue, processing_method, nature_sample_cells, culture_condition, biobank_id, biobank_date_entry, biobank_collection_id, consent, family_tree, available_relatives_samples, supply, max_delay_delivery, karyotype, quantity_families, detail_treatment, disease_outcome, associated_clinical_data, associated_molecular_data, associated_imagin_data, life_style, family_history, authentication_method, details_diagnosis, related_biological_material, quantity_available, concentration_available, samples_characteristics, delay_freezing, cell_characterization, number_of_passage, morphology_and_growth_characteristics, reference_paper, biobank_name, biobank_collection_name, patient_birth_date, tumor_diagnosis, file_imported_id,
	}

	public Map<String, String> notes;

	/**
	 * gender = M,F, H, U ( male, female, hermaphrodite, unknown)
	 */

	/**
	 * storage_conditions = fixed values LN = liquid nitrogen, 80 = -80, RT =
	 * room temperature varchar size=2
	 */
	/**
	 * value Y,N pour consent
	 */

	public Echantillon() {
	}

	public Map<String, String> getNotes() {
		return notes;
	}

	public void setNotes(Map<String, String> notes) {
		this.notes = notes;
	}

	public String getValue(Fields key) {
		return getValue(key.toString());
	}

	public void setValue(Fields key, String value) {
		setValue(key.toString(), value);
	}
}
