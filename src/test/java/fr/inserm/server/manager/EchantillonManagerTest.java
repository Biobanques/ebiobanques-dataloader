package fr.inserm.server.manager;

import org.junit.Test;

import fr.inserm.bean.v2.EchantillonBean;
import fr.inserm.bean.v2.FormatDefinition;
import fr.inserm.server.dao.FileImportedDAO;
import fr.inserm.server.model.FileImported;
import fr.inserm.server.model.FileImported.FieldsEnum;
import fr.inserm.server.tools.AbstractTest;

/**
 * test du manager d echantillon.
 * 
 * @author nicolas
 * 
 */
public class EchantillonManagerTest extends AbstractTest {

	@Test
	public final void testAddEchantillon() {
		EchantillonBean bean = null;
		String fileId = "0";
		String biobankId = "0";
		assertNull(EchantillonManager.addEchantillon(bean, fileId, biobankId));
		// ajout d un vrai echantillon
		FileImportedDAO daof = new FileImportedDAO();
		biobankId = "5";
		FileImported fi = daof.getLastFileImported("" + biobankId);
		assertNull(EchantillonManager.addEchantillon(bean,
				fi.getValue(FieldsEnum._id), biobankId));
		bean = new EchantillonBean();
		bean.addValue(FormatDefinition.id_sample, "6667777");
		assertNull(EchantillonManager.addEchantillon(bean,
				fi.getValue(FieldsEnum._id), biobankId));
	}

	/**
	 * tests de purge d echantillons.
	 */
	@Test
	public final void testPurgeEchantillon() {
		assertFalse(EchantillonManager.purgeEchantillon(null));
		/**
		 * fake biobank mais get fichier de biobank dopnc ne doit pas planter
		 */
		String biobankId = "0";
		assertTrue(EchantillonManager.purgeEchantillon(biobankId));
		/**
		 * vraie biobank
		 */
		biobankId = "6";
		assertTrue(EchantillonManager.purgeEchantillon(biobankId));
		// pure ge de grenoble
		biobankId = "4";
		// FIXME la purge semble prendre du temps
		assertTrue(EchantillonManager.purgeEchantillon(biobankId));
	}
}
