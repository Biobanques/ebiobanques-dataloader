package fr.inserm.server.tools;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * classe de chargement des proprietes du application.properties<br>
 * 
 */
public class ApplicationResourcesLoader {

	private final String serverSmtp;
	private String smtpEmail;
	private final String smtpUsername;
	private final String smtpPass;
	private String emailAdmin;
	private String emailTest;
	private final String version;
	private final boolean mailActif;
	private final boolean starttls;

	private final String mongoDatabase;
	private final String mongoUsername;
	private final String mongoPassword;
	private final String mongoPort;
	private final String mongoUrl;

	public String getMongoDatabase() {
		return mongoDatabase;
	}

	public String getMongoUsername() {
		return mongoUsername;
	}

	public String getMongoPassword() {
		return mongoPassword;
	}

	public String getMongoPort() {
		return mongoPort;
	}

	public String getMongoUrl() {
		return mongoUrl;
	}

	private static final Logger LOGGER = Logger
			.getLogger(ApplicationResourcesLoader.class);

	public ApplicationResourcesLoader() {
		Properties resourcesProps = new Properties();
		try {
			resourcesProps.load(this.getClass().getClassLoader()
					.getResourceAsStream("application.properties"));
		} catch (IOException e) {
			LOGGER.error("pb loading application.properties" + e.getMessage());
		}
		mongoUrl = resourcesProps.getProperty("application.mongo.url");
		mongoDatabase = resourcesProps.getProperty("application.mongo.db");
		mongoUsername = resourcesProps
				.getProperty("application.mongo.username");
		mongoPort = resourcesProps.getProperty("application.mongo.port");
		mongoPassword = resourcesProps
				.getProperty("application.mongo.password");

		serverSmtp = resourcesProps.getProperty("application.server.smtp");
		smtpEmail = resourcesProps.getProperty("application.server.smtp.email");
		smtpUsername = resourcesProps
				.getProperty("application.server.smtp.username");
		smtpPass = resourcesProps.getProperty("application.server.smtp.pass");
		emailAdmin = resourcesProps.getProperty("emailAdmin");
		setEmailTest(resourcesProps.getProperty("emailTest"));
		version = resourcesProps.getProperty("dataloader.version");
		mailActif = resourcesProps.getProperty("mail.actif").equals("true") ? true
				: false;
		starttls = resourcesProps.getProperty(
				"application.server.smtp.starttls.enable").equals("true") ? true
				: false;
	}

	public String getVersion() {
		return version;
	}

	public String getServerSmtp() {
		return serverSmtp;
	}

	public String getSmtpUsername() {
		return smtpUsername;
	}

	public String getSmtpPass() {
		return smtpPass;
	}

	public static Logger getLogger() {
		return LOGGER;
	}

	public String getSmtpEmail() {
		return smtpEmail;
	}

	public void setSmtpEmail(String smtpEmail) {
		this.smtpEmail = smtpEmail;
	}

	public String getEmailAdmin() {
		return emailAdmin;
	}

	public void setEmailAdmin(String emailAdmin) {
		this.emailAdmin = emailAdmin;
	}

	public boolean isMailActif() {
		return mailActif;
	}

	public boolean isStarttls() {
		return starttls;
	}

	public String getEmailTest() {
		return emailTest;
	}

	public void setEmailTest(String emailTest) {
		this.emailTest = emailTest;
	}

}
