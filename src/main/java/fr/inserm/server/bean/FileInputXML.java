package fr.inserm.server.bean;

import java.util.Date;
import java.util.List;

import fr.inserm.bean.v2.EchantillonBean;

public class FileInputXML {

	private String nameFile;

	private String generatedName;

	public String getGeneratedName() {
		return generatedName;
	}

	public void setGeneratedName(String generatedName) {
		this.generatedName = generatedName;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	private Date dateImport;
	
	private String extractionId;

	private int formatVersion;

	private SiteXML site;

	private List<EchantillonBean> echantillons;

	public Date getDateImport() {
		return dateImport;
	}

	public void setDateImport(Date dateImport) {
		this.dateImport = dateImport;
	}

	public int getFormatVersion() {
		return formatVersion;
	}

	public void setFormatVersion(int formatVersion) {
		this.formatVersion = formatVersion;
	}

	public SiteXML getSite() {
		return site;
	}

	public void setSite(SiteXML site) {
		this.site = site;
	}

	public List<EchantillonBean> getEchantillons() {
		return echantillons;
	}

	public void setEchantillons(List<EchantillonBean> echantillons) {
		this.echantillons = echantillons;
	}

	public String getExtractionId() {
		return extractionId;
	}

	public void setExtractionId(String extractionId) {
		this.extractionId = extractionId;
	}

	
}
