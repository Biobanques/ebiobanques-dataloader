package fr.inserm.server.tools;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import fr.inserm.server.bean.AnomalieBean;

/**
 * tool de mailing.
 * 
 * @author nmalservet
 */
public class MailTools {

	private static final Logger LOGGER = Logger.getLogger(MailTools.class);

	/**
	 * classe utilitaire donc pas de constructeur.
	 */
	private MailTools() {

	}

	// private static String emailAdmin = "nicolas@malservet.eu";
	private static String emailAdmin = new ApplicationResourcesLoader().getEmailAdmin();

	private static String footerMail = "<br><i>Pour toute question ou probl&egrave;me rencontr&eacute;, veuillez contacter l'administrateur syst&egrave;me &agrave; l'adresse : <a href=mailto:\""
			+ emailAdmin + "\">" + emailAdmin + "</a>" + "</i>";

	/**
	 * envoi de mail pour indiquer le demarrage d une analyse des fichiers pour
	 * un site
	 * 
	 * @param id
	 *            : timestamp id
	 * @param fileNumber
	 *            : number of files to parse
	 * @param filesName
	 *            : liste of files in a string
	 */
	public static boolean sendMailNotifyStart(long id, int fileNumber, String filesName) {

		return MailTools.sendEmail(emailAdmin, "D&eacute;marrage de l'analyse des fichiers: AnalyseId:" + id,
				"<h1>Notification de d&eacute;marrage d'analyse des fichiers</h1><br><b>Nombre de fichiers &agrave; traiter</b>:"
						+ fileNumber + "<br> Liste des fichiers receptionn&eacute;s:<ul>" + filesName + "</ul>"
						+ footerMail);
	}

	/**
	 * envoie du mail de notification en fin de tache
	 * 
	 * @param id
	 *            : long timestamp id
	 * @param tempsTraitement
	 *            : message sur le temps de traitement
	 * @param alerteAno
	 *            : message sur lers anomalies
	 */
	public static boolean sendMailNotifyEnd(long id, String tempsTraitement, List<AnomalieBean> anomalies,
			String biobankName, int numberFiles, List<String> filesName, List<String> filesSaved,
			List<String> filesUnsaved, List<String> logsFiles, int nbech) {

		String alertAno = "<font color=\"#347235\">Aucune anomalie n'a &eacute;t&eacute; d&eacute;tect&eacute;e;</font>";

		if (anomalies != null && anomalies.size() > 0) {
			alertAno = "<font color=\"red\">Des anomalies ont &eacute;t&eacute; d&eacute;tect&eacute;es;! Nombre d'anomalies: "
					+ anomalies.size();
			alertAno += "<table><th><td>Level</td><td>Categorie</td><td>Date</td><td>Message</td></th>";
			for (AnomalieBean ano : anomalies) {
				alertAno += "<tr><td>" + ano.getLevel() + "</td><td>" + ano.getType() + "</td><td>" + ano.getDate()
						+ "</td><td>" + ano.getMessage() + "</td></tr>";
			}
			alertAno += "</table><br> Veuillez contacter l'administrateur syst&egrave;me &agrave; l'adresse : <a href=mailto:\""
					+ emailAdmin + "\">" + emailAdmin + "</a>" + "</font>";
		}
		String duree = "";
		if (tempsTraitement != null) {
			duree = tempsTraitement;
		}
		String biobank = "no biobank filled";
		if (biobankName != null) {
			biobank = biobankName;
		} else {
			LOGGER.warn("no biobank name");
		}
		Date date = new Date();
		/*
		 * Envoi du rapport à l'administrateur serveur
		 */
		String mailSubject = "Fin d'analyse de fichiers: AnalyseId:" + id;
		String mailContent = "<h1>Rapport d'analyse de fichier envoy&eacute; sur la plateforme inserm-dataloader-webapp</h1><br>"
				+ "<b>Date de traitement:</b>"
				+ date.toString()
				+ "<br>"
				+ alertAno
				+ "<br><b>Biobank Emettrice:</b>"
				+ biobank
				+ "<br>"
				+ "<br><b>Temps de traitement : </b>"
				+ duree
				+ "<br><b>Nombre de fichiers trait&eacute;s : </b>"
				+ numberFiles
				+ "<br> Liste de fichiers trait&eacute;s:<br><ul>"
				+ listToHTMLList(filesName)
				+ "</ul><br> Liste de fichiers corrects et ins&eacute;r&eacute;s:<br><ul>"
				+ listToHTMLList(filesSaved)
				+ "</ul><br><b> Nombre d'&eacute;chantillons ins&eacute;r&eacute;s : </b>"
				+ nbech
				+ "<br><b>Liste de fichiers avec erreurs:</b><br><ul>"
				+ listToHTMLList(filesUnsaved)
				+ "</ul><br><b>Liste de fichiers de log</b>:<ul>"
				+ listToHTMLList(logsFiles)
				+ "</ul>"
				+ "<br><b>Site de pr&eacute;sentation des donn&eacute;es :</b> <a href=\"http://research.biobankapps.com\">research.biobankapps.com</a><br>"
				+ footerMail;
		boolean mailToAdmin = MailTools.sendEmail(emailAdmin, mailSubject, mailContent);
		/*
		 * FIXME : fonctionnalité à débloquer si process hyper robuste Envoi du
		 * rapport à l'utilisateur si identifié dans les données de la Biobank
		 */
		/*
		 * if (contact != null && contact.getEmail() != null) { boolean
		 * mailToBiobankUser = MailTools.sendEmail(contact.getEmail(),
		 * mailSubject, mailContent);
		 * 
		 * return mailToAdmin & mailToBiobankUser; } else {
		 * LOGGER.info("Pas de contact enregistré pour envoyer le rapport"); }
		 */
		return mailToAdmin;
	}

	/**
	 * formatage des lists de nom en liste html
	 * 
	 * @param list
	 * @return
	 */
	private static String listToHTMLList(List<String> list) {
		String res = "";
		if (list != null) {
			for (String ff : list) {
				res += "<li>" + ff + "</li>";
			}
		}
		return res;
	}

	/**
	 * generic method to send mail
	 * 
	 * @param to
	 * @param from
	 * @param subject
	 * @param content
	 * @return
	 */
	public static boolean sendEmail(String to, String subject, String content) {

		Properties props = new Properties();
		final ApplicationResourcesLoader app = new ApplicationResourcesLoader();
		props.put("mail.smtp.host", app.getServerSmtp());
		props.put("mail.from", app.getSmtpEmail());
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", app.isStarttls());

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(app.getSmtpEmail(), app.getSmtpPass());
			}
		});
		MimeMessage message = new MimeMessage(session);
		try {
			InternetAddress dest = new InternetAddress(to);
			message.addRecipient(Message.RecipientType.TO, dest);
			message.setSubject(subject);
			message.setFrom(new InternetAddress(app.getSmtpEmail()));
			message.setSentDate(new Date());
			message.setContent(content, "text/html");
			Transport.send(message);
			return true;
		} catch (Exception ex) {
			LOGGER.error("Cannot send email. " + ex.getMessage());
			ex.printStackTrace();
		}
		return false;
	}

}
