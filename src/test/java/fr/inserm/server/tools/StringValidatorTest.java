package fr.inserm.server.tools;

import junit.framework.TestCase;

public class StringValidatorTest extends TestCase {

	public final void testTruncate() {
		/**
		 * tests de substring
		 */
		String tt = new String("abcde");
		assertEquals("ab", tt.substring(0, 2));
		// si end index > size alors erreur
		try {
			assertEquals("abcde", tt.substring(0, 6));
			// passe obligatoirement dans le catch
			assertTrue(false);
		} catch (Exception e) {

		}
		assertEquals("abcde", tt.substring(0, 5));
		assertEquals("a", tt.substring(0, 1));
		/**
		 * test de methode truncate
		 */
		assertEquals("abcde", StringValidator.truncate(tt, 6));
		assertEquals("ab", StringValidator.truncate(tt, 2));
		// equivalence de character
		assertEquals('a', tt.charAt(0));
		String vide = "";
		assertTrue(vide.isEmpty());
		try {
			assertNotNull(vide.charAt(0));
			assertTrue(false);
		} catch (Exception e) {

		}
	}

}
