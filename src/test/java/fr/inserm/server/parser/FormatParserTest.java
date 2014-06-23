package fr.inserm.server.parser;

import java.io.File;

import org.junit.Test;

import fr.inserm.server.exception.BadFormatInsermFileException;
import fr.inserm.server.tools.AbstractTest;

public class FormatParserTest extends AbstractTest {

	@Test
	public final void testConvertXml2Bean() {
		try {
			assertNull(FormatXMLParser.convertXml2Bean(null, null));
		} catch (BadFormatInsermFileException e) {
			assertTrue(false);
		} catch (Exception ex) {
			assertTrue(false);
		}
		File file = new File("./pom.xml");
		try {
			assertNull(FormatXMLParser.convertXml2Bean(file, "bb"));
			assertTrue(false);
		} catch (Exception ex) {
		}
	}
}
