package fr.inserm.server.tools;

import org.junit.Test;

import fr.inserm.server.exception.FolderInexistException;

/**
 * test de l analyse du dossier de reception.
 * 
 * @author nmalservet
 * 
 */
public class AnalyzerDossierReceptionTest extends AbstractTest {

	/**
	 * test de l analyse du dossier de reception.
	 */
	@Test
	public final void testAnalyze() {
		AnalyzerDossierReception.analyze();
	}

	@Test
	public final void testGetFilesOnReception() {
		try {
			AnalyzerDossierReception.getFilesOnReception(null, "");
			assertTrue(false);
		} catch (FolderInexistException e) {
			// cas normal exception generee
			// e.printStackTrace();
		}
		try {
			AnalyzerDossierReception.getFilesOnReception("", "");
			assertTrue(false);
		} catch (FolderInexistException e) {
			// cas normal exception generee
			// e.printStackTrace();
		}
	}
}
