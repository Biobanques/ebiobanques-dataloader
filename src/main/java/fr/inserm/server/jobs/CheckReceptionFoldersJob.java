package fr.inserm.server.jobs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.inserm.server.dao.BiobankDAO;
import fr.inserm.server.dao.ContactDAO;
import fr.inserm.server.dao.FileDetectedDAO;
import fr.inserm.server.model.Biobank;
import fr.inserm.server.model.Biobank.FieldsEnum;
import fr.inserm.server.model.Contact;
import fr.inserm.server.model.FileDetected;
import fr.inserm.server.tools.AnalyzerDossierReception;
import fr.inserm.server.tools.MailTools;

public class CheckReceptionFoldersJob {

	/**
	 * Empty constructor for job initilization
	 */
	public CheckReceptionFoldersJob() {
	}

	private static final Logger LOGGER = Logger
			.getLogger(CheckReceptionFoldersJob.class);

	/**
	 * <p>
	 * Called by the <code>{@link org.quartz.Scheduler}</code> when a
	 * <code>{@link org.quartz.Trigger}</code> fires that is associated with the
	 * <code>Job</code>.
	 * </p>
	 */
	public synchronized void checkReceptionFolders() {
		LOGGER.info("checking folders for new reception");
		final List<Biobank> biobanks;
		BiobankDAO biobanksDao = AnalyzerDossierReception.getBiobankDao();
		biobanks = biobanksDao.loadAll();
		ContactDAO contactDao = AnalyzerDossierReception.getContactDao();
		List<FileDetected> filesDetected;
		FileDetectedDAO fileDetectedDao = AnalyzerDossierReception
				.getFileDetectedDao();
		// filesDetected = fileDetectedDao.loadAll();
		for (Biobank biobank : biobanks) {
			File[] filesReceveid;
			List<String> newFiles = new ArrayList<String>();
			try {
				filesReceveid = AnalyzerDossierReception.getFilesOnReception(
						biobank.getValue(FieldsEnum.folder_reception),
						biobank.getValue(FieldsEnum.folder_done));
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
						Contact contact = contactDao.load(biobank
								.getValue(Biobank.FieldsEnum.contact_id));
						if (contact != null) {
							if (contact.getValue(Contact.FieldsEnum.email) != null
									&& contact
											.getValue(Contact.FieldsEnum.email) != "")
								MailTools.sendEmailConfirmReception(contact
										.getValue(Contact.FieldsEnum.email),
										newFiles);
							else
								LOGGER.warn("Pas d'email pour ce contact : "
										+ contact
												.getValue(Contact.FieldsEnum.first_name)
										+ " "
										+ contact
												.getValue(Contact.FieldsEnum.last_name));
						} else
							LOGGER.warn("Pas de contact pour la biobanque "
									+ biobank.getValue(Biobank.FieldsEnum.name));
					}

				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage());

			}
		}

		LOGGER.warn("end");

	}
}