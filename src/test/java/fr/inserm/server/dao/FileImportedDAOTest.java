package fr.inserm.server.dao;

import java.util.List;

import org.junit.Test;

import fr.inserm.server.model.FileImported;
import fr.inserm.server.tools.AbstractTest;

/**
 * tests unitaires sur le dao fileimported.
 * 
 * @author nmalservet
 * 
 */
public class FileImportedDAOTest extends AbstractTest {

	/**
	 * nice biobank id.
	 */
	private String biobankId = "1";

	/**
	 * biobank fictive.
	 */
	private String biobankInexisting = "66";

	@Test
	public void testGetLastFileImported() {
		FileImportedDAO dao = new FileImportedDAO();
		FileImported fi = dao.getLastFileImported(biobankId);
		assertNotNull(fi);
		assertEquals(fi.getValue(FileImported.FieldsEnum.biobank_id), biobankId);
	}

	@Test
	public void testGetFilesImportedByExtractionId() {
		FileImportedDAO dao = new FileImportedDAO();
		// cas qui marche
		List<FileImported> files = dao.getFilesImportedByExtractionId("2013-07-26T02:00:54-0700");
		assertNotNull(files);
		assertEquals(4, files.size());
		// cas extraction id inexistant
		files = dao.getFilesImportedByExtractionId(biobankId);
		assertNotNull(files);
		assertEquals(files.size(), 0);
	}

	@Test
	public void testGetFilesImportedByBiobankExceptExtractionId() {
		FileImportedDAO dao = new FileImportedDAO();
		// cas qui retourne des fichiers
		String extractionID = "2013-07-26T02:00:54-0700";
		List<FileImported> files = dao.getFilesImportedByBiobankExceptExtractionId(biobankId, extractionID);
		assertNotNull(files);
		assertTrue(files.size() > 0);
		for (FileImported file : files) {
			assertEquals(biobankId, file.getValue(FileImported.FieldsEnum.biobank_id));
			assertNotSame(extractionID, file.getValue(FileImported.FieldsEnum.extraction_id));
		}
		// cas qui ne retourne aucun fichier
		files = dao.getFilesImportedByBiobankExceptExtractionId("18", "2013-07-26T02:07:54-0700");
		assertNotNull(files);
		assertEquals(0, files.size());
	}

	/**
	 * test du save. <br>
	 * TODO : ajouter des controles pre insert pour ameliorer le contenu des données. <br>
	 * NB : les données sont ajoutées en base donc ne pas effectuer de tests sur uen base de prod car ke jeu de données
	 * sera modifié.
	 */
	@Test
	public void testSave() {
		FileImportedDAO dao = new FileImportedDAO();
		FileImported file = new FileImported();
		file.setValue(FileImported.FieldsEnum.biobank_id, biobankInexisting);
		String id = dao.save(file);
		assertNotNull(id);
		FileImported fi = dao.getLastFileImported(biobankInexisting);
		assertNotNull(fi);
		assertEquals(fi.getValue(FileImported.FieldsEnum.biobank_id), biobankInexisting);
	}
}
