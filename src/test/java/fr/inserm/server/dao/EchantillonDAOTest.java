package fr.inserm.server.dao;

import org.junit.Test;

import fr.inserm.server.model.Echantillon;
import fr.inserm.server.model.Echantillon.Fields;
import fr.inserm.server.model.FileImported;
import fr.inserm.server.model.FileImported.FieldsEnum;
import fr.inserm.server.tools.AbstractTest;

/**
 * test du dao d echantillon
 * 
 * @author nmalservet
 */
public class EchantillonDAOTest extends AbstractTest {

	/**
	 * test du delete all sur les echantillons
	 */
	@Test
	public void testRemoveByFileId() {
		EchantillonDAO dao = new EchantillonDAO();
		assertFalse(dao.removeByFileId(null));
		// get a file id echantillon
		FileImportedDAO daof = new FileImportedDAO();
		FileImported fi = daof.getLastFileImported("5");
		assertTrue(dao.removeByFileId(fi.getValue(FieldsEnum._id)));
	}

	/**
	 * test du save
	 */
	@Test
	public void testSave() {
		EchantillonDAO dao = new EchantillonDAO();
		assertNotNull(dao.save(null));
		Echantillon echantillon = new Echantillon();
		assertNotNull(dao.save(echantillon));
		echantillon.setValue(Fields.id_sample, "3");
		echantillon.setValue(Fields.age, "32");
		assertNull(dao.save(echantillon));
	}
}
