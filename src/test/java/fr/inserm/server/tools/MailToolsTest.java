package fr.inserm.server.tools;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import fr.inserm.server.bean.AnomalieBean;

/**
 * test d envoi de mail.
 * 
 * @author nmalservet
 * 
 */
public class MailToolsTest extends TestCase {

	private String to = "mylord@got.com";

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
	public final void testSendMailNotifyStart() {
		assertTrue(MailTools.sendMailNotifyStart(1, 55, "fichhier1,fichier2,..."));
	}

	/**
	 * test d envoi du mail de notifictaion de debmarrage
	 */
	public final void testSendMailNotifyEnd() {
		List<AnomalieBean> list = new ArrayList<AnomalieBean>();
		assertTrue(MailTools.sendMailNotifyEnd(1, null, list, null, 1, null, null, null, null, 5));
	}
}
