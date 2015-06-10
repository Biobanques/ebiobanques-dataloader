package fr.inserm.server.jobs;

import org.junit.Test;

import fr.inserm.server.tools.AbstractTest;

public class CheckReceptionFoldersJobTest extends AbstractTest {

	@Test
	public void testCheckReceptionFolders() {
		CheckReceptionFoldersJob.checkReceptionFolders();
	}
}
