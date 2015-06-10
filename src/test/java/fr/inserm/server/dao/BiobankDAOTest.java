package fr.inserm.server.dao;

import java.util.List;

import org.junit.Test;

import fr.inserm.server.model.Biobank;
import fr.inserm.server.tools.AbstractTest;

/**
 * test des methodes de dao.
 * 
 * @author nmalservet
 */
public class BiobankDAOTest extends AbstractTest {

	/**
	 * test du getCollection sur collection inexistante
	 */
	@Test
	public void testGetCollection() {
		BiobankDAO dao = new BiobankDAO();
		assertNotNull(dao.getCollection(""));

	}

	@Test
	public void testLoadAll() {
		BiobankDAO dao = new BiobankDAO();
		List<Biobank> biobanks = dao.loadAll();
		assertNotNull(biobanks);
		assertTrue(biobanks.size() > 0);
		// test que les dossiers sont bien recuperes
		for (Biobank biobank : biobanks) {
			assertNotNull(
					"biobank folder done is null:"
							+ biobank.getValue(Biobank.FieldsEnum.id),
					biobank.getValue(Biobank.FieldsEnum.folder_done));
		}
	}

	@Test
	public void testLoad() {
		BiobankDAO dao = new BiobankDAO();
		Biobank biobank = dao.load("1");
		assertNotNull(biobank);
		assertEquals(biobank.getValue(Biobank.FieldsEnum.identifier),
				"LPCE BioBank");
	}

}
