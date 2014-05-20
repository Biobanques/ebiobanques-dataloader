package fr.inserm.server.jobs;

import org.apache.log4j.Logger;

import fr.inserm.server.tools.AnalyzerDossierReception;

public class AnalyseDossierReceptionJob {

	/**
	 * Empty constructor for job initilization
	 */
	public AnalyseDossierReceptionJob() {
	}

	private static final Logger LOGGER = Logger.getLogger(AnalyseDossierReceptionJob.class);

	/**
	 * <p>
	 * Called by the <code>{@link org.quartz.Scheduler}</code> when a
	 * <code>{@link org.quartz.Trigger}</code> fires that is associated with the
	 * <code>Job</code>.
	 * </p>
	 */
	public synchronized void analyseDossierReception() {
		LOGGER.info("Quartz job : analyseDossierReception...");
		AnalyzerDossierReception.analyze();
		LOGGER.debug("ending analyseDossierReception");

	}

}