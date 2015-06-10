package fr.inserm.server.bean;

public class ContactBean {

	private int id;

	private String name;

	private String firstName;

	private String email;
	private boolean inactive;
	private String adresse;
	private String codePostal;

	private int pays;

	private String ville;

	private String phone;

	public String getAdresse() {
		return adresse;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPays() {
		return pays;
	}

	public String getPhone() {
		return phone;
	}

	public String getVille() {
		return ville;
	}

	public boolean isInactive() {
		return inactive;
	}

	public void setActive(boolean inactive) {
		this.inactive = inactive;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setInactive(boolean inactive) {
		this.inactive = inactive;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPays(int pays) {
		this.pays = pays;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

}
