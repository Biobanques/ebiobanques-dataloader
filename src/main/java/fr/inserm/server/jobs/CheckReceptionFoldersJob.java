package fr.inserm.server.jobs;

import fr.inserm.server.tools.CheckReceptionFolders;

public class CheckReceptionFoldersJob {

	/**
	 * Empty constructor for job initilization
	 */
	public CheckReceptionFoldersJob() {
	}

	/**
	 * <p>
	 * Called by the <code>{@link org.quartz.Scheduler}</code> when a
	 * <code>{@link org.quartz.Trigger}</code> fires that is associated with the
	 * <code>Job</code>.
	 * </p>
	 */
	public static void checkReceptionFolders() {
		CheckReceptionFolders.checkReceptionFolders();
	}
}