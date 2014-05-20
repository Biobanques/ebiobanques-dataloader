package fr.inserm.server.tools;

import java.util.TimeZone;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

/**
 * ConfigurationListener
 * 
 * @author nmalservet
 */
public class ApplicationContextListener extends ContextLoaderListener {
	public void contextInitialized(final ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);

		// define the application properties:
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	}
}
