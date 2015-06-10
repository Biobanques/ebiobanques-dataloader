package fr.inserm.server.model;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import fr.inserm.server.tools.AbstractTest;

/**
 * test du model echantillon.
 * 
 * @author nmalservet
 * 
 */
public class EchantillonTest extends AbstractTest {

	/**
	 * test des methodes de notes.
	 */
	@Test
	public final void testNotes() {
		Echantillon ech = new Echantillon();
		Map<String, String> notes = new HashMap<String, String>();
		notes.put("truc", "muche");
		ech.setNotes(notes);
		assertEquals(ech.getNotes(), notes);
	}

	/**
	 * test des methodes de value
	 */
	@Test
	public final void testValue() {
		Echantillon ech = new Echantillon();
		String val = "N";
		ech.setValue(Echantillon.Fields.associated_clinical_data, val);
		assertEquals(ech.getValue(Echantillon.Fields.associated_clinical_data),
				val);
	}
}
