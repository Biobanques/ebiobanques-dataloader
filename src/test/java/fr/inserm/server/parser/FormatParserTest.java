package fr.inserm.server.parser;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Test;

import fr.inserm.server.exception.BadFormatInsermFileException;

public class FormatParserTest extends TestCase {

	@Test
	public final void testConvertXml2Bean() {
		try {
			assertNull(FormatXMLParser.convertXml2Bean(null,null));
		} catch (BadFormatInsermFileException e) {
			assertTrue(false);
		} catch (Exception ex) {
			assertTrue(false);
		}
		File file = new File("./pom.xml");
		try {
			assertNull(FormatXMLParser.convertXml2Bean(file,"bb"));
			assertTrue(false);
		}  catch (Exception ex) {
		}
	}
}
