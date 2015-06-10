package fr.inserm.server.tools;

import java.io.File;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Test;

import fr.inserm.server.dao.FileDetectedDAO;
import fr.inserm.server.exception.FolderInexistException;
import fr.inserm.server.model.Biobank;

public class CheckReceptionFoldersTest extends AbstractTest {
	Biobank biobank;
	File fileTest;
	FileDetectedDAO fdDao;

	@Before
	public void init() throws Exception {
		biobank = CheckReceptionFolders.getBiobankDao().loadAll().get(0);
		fileTest = new File(
				biobank.getValue(Biobank.FieldsEnum.folder_reception)
						+ "testFile.xml");
		fdDao = CheckReceptionFolders.getFileDetectedDao();

	}

	@Test
	public void testCheckReceptionFolder() {
		new CheckReceptionFolders();
	}

	@Test
	public void testCheckReceptionFolders() throws Exception {
		CheckReceptionFolders.checkReceptionFolders();
		PrintWriter pw = new PrintWriter(fileTest);
		pw.close();
		CheckReceptionFolders.checkReceptionFolders();

		CheckReceptionFolders.checkReceptionFolders();
		fdDao.remove(fileTest.getName());
		fileTest.delete();
	}

	@Test
	public void testSendConfirmation() {
		try {
			CheckReceptionFolders.sendConfirmation(null, null);
			CheckReceptionFolders.sendConfirmation(biobank, null);
		} catch (Exception e) {
			fail("All exception must be catch before");
		}
	}

	@Test
	public void testGetFilesOnReception() {
		try {
			CheckReceptionFolders.getFilesOnReception(fileTest.getName());
			fail("Exception must be sent");
		} catch (FolderInexistException e) {
			System.out.println(fileTest.getName() + " is not a valid folder.");
		}
		try {
			CheckReceptionFolders.getFilesOnReception(fileTest.getParent());
			System.out.println("ok, " + fileTest.getParent()
					+ " is a valid folder.");
		} catch (FolderInexistException e) {
			fail("Exception : ");
			e.printStackTrace();
		}
		try {
			CheckReceptionFolders.getFilesOnReception(null);
			fail("Folder can not be null, where's the exception?");
		} catch (FolderInexistException e) {
			System.out.println("Good exception, folder can not be null!");
		}
	}

}
