package fr.inserm.server.bean;

public class FileImportedBean {

	public Integer id;

	/**
	 * id d'extarction
	 * 
	 */
	private String extractionId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExtractionId() {
		return extractionId;
	}

	public void setExtractionId(String extractionId) {
		this.extractionId = extractionId;
	}

}
