package fr.inserm.server.bean;

import java.beans.IntrospectionException;

import org.junit.Test;

public class AllBeansTest {

	@Test
	public void testContact() {
		try {
			JavaBeanTester.test(ContactBean.class, "");
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFileImportedBean() {
		try {
			JavaBeanTester.test(FileImportedBean.class, "");
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFileInputXML() {
		try {
			JavaBeanTester.test(FileInputXML.class, "echantillons");
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNoteBean() {
		try {
			JavaBeanTester.test(NoteBean.class, "");
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSiteXML() {
		try {
			JavaBeanTester.test(SiteXML.class, "");
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}

}
