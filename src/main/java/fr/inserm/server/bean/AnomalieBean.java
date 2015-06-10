package fr.inserm.server.bean;

import java.util.Date;

/**
 * bean de stockage des informations liees a une anomalie.
 * 
 * @author nicolas
 * 
 */
public class AnomalieBean {

	public AnomalieBean(LevelAnomalie lev, String mess,
			FunctionalObjectType typ, Date dat) {

		message = mess;
		level = lev;
		type = typ;
		date = dat;
	}

	public AnomalieBean(LevelAnomalie lev, String mess, FunctionalObjectType typ) {

		message = mess;
		level = lev;
		type = typ;
		date = new Date();
	}

	public String message;

	public FunctionalObjectType type;

	public LevelAnomalie level;

	public Date date;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public FunctionalObjectType getType() {
		return type;
	}

	public void setType(FunctionalObjectType type) {
		this.type = type;
	}

	public LevelAnomalie getLevel() {
		return level;
	}

	public void setLevel(LevelAnomalie level) {
		this.level = level;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
