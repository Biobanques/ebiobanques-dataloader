package fr.inserm.server.manager;

import fr.inserm.server.dao.BiobankDAO;
import fr.inserm.server.dao.ContactDAO;
import fr.inserm.server.dao.EchantillonDAO;
import fr.inserm.server.dao.FileDetectedDAO;
import fr.inserm.server.dao.FileImportedDAO;

/**
 * abstract pour charger les dao.
 * 
 * @author nmalservet
 * 
 */
public class AbstractManager {

	private static EchantillonDAO echantillonDao;

	private static BiobankDAO biobankDao;

	private static FileImportedDAO fileImportedDao;
	private static ContactDAO contactDao;

	private static FileDetectedDAO fileDetectedDao;

	public static BiobankDAO getBiobankDao() {
		return biobankDao;
	}

	public static EchantillonDAO getEchantillonDao() {
		return echantillonDao;
	}

	public static FileImportedDAO getFileImportedDao() {
		return fileImportedDao;
	}

	public void setBiobankDao(BiobankDAO biobankDao) {
		AbstractManager.biobankDao = biobankDao;
	}

	public void setEchantillonDao(EchantillonDAO echantillonDao) {
		AbstractManager.echantillonDao = echantillonDao;
	}

	public void setFileImportedDao(FileImportedDAO fileImportedDao) {
		AbstractManager.fileImportedDao = fileImportedDao;
	}

	public static ContactDAO getContactDao() {
		return contactDao;
	}

	public void setContactDao(ContactDAO contactDao) {
		AbstractManager.contactDao = contactDao;
	}

	public static FileDetectedDAO getFileDetectedDao() {
		return fileDetectedDao;
	}

	public static void setFileDetectedDao(FileDetectedDAO fileDetectedDao) {
		AbstractManager.fileDetectedDao = fileDetectedDao;
	}

}
