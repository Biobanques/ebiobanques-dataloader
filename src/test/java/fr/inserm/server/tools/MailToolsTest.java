package fr.inserm.server.tools;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fr.inserm.server.bean.AnomalieBean;

/**
 * test d envoi de mail.
 * 
 * @author nmalservet
 * 
 */
public class MailToolsTest extends AbstractTest {
	private String to;

	@Override
	public void setUp() {
		ApplicationResourcesLoader app = new ApplicationResourcesLoader();
		to = app.getEmailTest();
	}

	@Test
	public final void testSendEmail() {
		String subject = "Test Envoi Email";
		String msgContent = "Bonjour,\n Ceci est un email de test sans html.";
		assertTrue(MailTools.sendEmail(to, subject, msgContent));
		// test envoi de mail html
		msgContent = "<h1>Bonjour,</h1><br> Ceci est un message HTML, le texte gras doit être <b>gras</b>.";
		assertTrue(MailTools.sendEmail(to, subject, msgContent));
	}

	/**
	 * test d envoi du mail de notifictaion de debmarrage
	 */
	@Test
	public final void testSendMailNotifyStart() {
		assertTrue(MailTools
				.sendMailNotifyStart(1, 55, "fichier1,fichier2,..."));
	}

	/**
	 * test d envoi du mail de notifictaion de debmarrage
	 */
	@Test
	public final void testSendMailNotifyEnd() {
		List<AnomalieBean> list = new ArrayList<AnomalieBean>();
		assertTrue(MailTools.sendMailNotifyEnd(1, null, list, null, 1, null,
				null, null, null, 5));
	}

	@Test
	public final void testSendsendEmailConfirmReception() {
		List<String> files = new ArrayList<String>();

		files.add("TestFiles1.xml");
		files.add("TestFiles2.xml");
		files.add("TestFiles3.xml");
		try {
			MailTools.sendEmailConfirmReception(null, null);
			fail("an Exception must have been sent");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			MailTools.sendEmailConfirmReception(to, null);
			fail("an Exception must have been sent");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			MailTools.sendEmailConfirmReception(null, files);

		} catch (Exception e) {
			fail("No exception here");
		}
		try {
			MailTools.sendEmailConfirmReception(to, files);
		} catch (Exception e) {
			fail("No exception here");
		}

	}
}
