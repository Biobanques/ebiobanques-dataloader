package fr.inserm.server.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.inserm.server.dao.BiobankDAO;
import fr.inserm.server.dao.FileDetectedDAO;
import fr.inserm.server.exception.FolderInexistException;
import fr.inserm.server.manager.AbstractManager;
import fr.inserm.server.model.Biobank;
import fr.inserm.server.model.Biobank.FieldsEnum;
import fr.inserm.server.model.Contact;
import fr.inserm.server.model.FileDetected;

public class CheckReceptionFolders extends AbstractManager {

	/**
	 * Empty constructor for job initilization
	 */
	public CheckReceptionFolders() {
	}

	private static final Logger LOGGER = Logger
			.getLogger(CheckReceptionFolders.class);

	/**
	 * <p>
	 * Called by the <code>{@link org.quartz.Scheduler}</code> when a
	 * <code>{@link org.quartz.Trigger}</code> fires that is associated with the
	 * <code>Job</code>.
	 * </p>
	 */
	public static void checkReceptionFolders() {
		LOGGER.info("checking folders for new reception");
		final List<Biobank> biobanks;
		BiobankDAO biobanksDao = getBiobankDao();
		biobanks = biobanksDao.loadAll();
		FileDetectedDAO fileDetectedDao = getFileDetectedDao();

		for (Biobank biobank : biobanks) {
			LOGGER.info("Biobanque : "
					+ biobank.getValue(Biobank.FieldsEnum.identifier));
			File[] filesReceveid;
			List<String> newFiles = new ArrayList<String>();
			try {
				filesReceveid = getFilesOnReception(biobank
						.getValue(FieldsEnum.folder_reception));
				if (filesReceveid != null && filesReceveid.length != 0) {

					for (File file : filesReceveid) {
						String name = file.getName();

						FileDetected fileDetectedPresent = fileDetectedDao
								.load(name);

						if (fileDetectedPresent == null) {
							FileDetected fileDetected = new FileDetected();
							fileDetected.setValue("file_name", name);
							fileDetectedDao.save(fileDetected);
							newFiles.add(name);

						}
					}
					/*
					 * Envoi d'un mail de confirmation de bonne reception des
					 * fichiers
					 */
					if (newFiles.size() > 0) {

						LOGGER.info("Nouveaux fichiers détectés, envoi du mail de confirmation.");
						sendConfirmation(biobank, newFiles);
					} else {
						LOGGER.info("Pas de nouveaux fichiers pour cette biobanque.");
					}

				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage());

			}
		}

		LOGGER.info("All folders checked.");

	}

	public static void sendConfirmation(Biobank biobank, List<String> newFiles)
			throws Exception {
		if (newFiles != null && newFiles.size() > 0 && biobank != null) {
			Contact contact = getContactDao().load(
					biobank.getValue(Biobank.FieldsEnum.contact_id));
			if (contact != null) {
				if (contact.getValue(Contact.FieldsEnum.email) != null
						&& contact.getValue(Contact.FieldsEnum.email) != "")
					MailTools.sendEmailConfirmReception(
							contact.getValue(Contact.FieldsEnum.email),
							newFiles);
				else
					LOGGER.warn("Pas d'email pour ce contact : "
							+ contact.getValue(Contact.FieldsEnum.first_name)
							+ " "
							+ contact.getValue(Contact.FieldsEnum.last_name));
			} else
				LOGGER.warn("Pas de contact pour la biobanque "
						+ biobank.getValue(Biobank.FieldsEnum.name));
		}

	}

	/**
	 * get an array of file into path folder reception
	 * 
	 * @param pathFolderReception
	 * @return
	 * @throws FolderInexistException
	 */
	public static File[] getFilesOnReception(String pathFolderReception)
			throws FolderInexistException {
		if (pathFolderReception == null) {
			LOGGER.error("folder null for pathFolderReception:"
					+ pathFolderReception);
			throw new FolderInexistException();
		}
		File folder = new File(pathFolderReception);
		if (!folder.exists()) {
			AnalyzerDossierReception.LOGGER
					.error("dossier de reception inexist:"
							+ pathFolderReception);
			throw new FolderInexistException();
		}

		File[] filesList = folder.listFiles();
		return filesList;

	}
}