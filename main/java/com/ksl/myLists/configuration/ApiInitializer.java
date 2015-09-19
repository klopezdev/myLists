package com.ksl.myLists.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Get the party started!
 * 
 * @author Keith Lopez
 */
public class ApiInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	private static final int MAX_INTERVAL = 3 * 24 * 60 * 60;
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		
		servletContext.addListener(new HttpSessionListener() {
			@Override
			public void sessionDestroyed(HttpSessionEvent arg0) {}
			
			@Override
			public void sessionCreated(HttpSessionEvent sessionEvent) {
				sessionEvent.getSession().setMaxInactiveInterval(MAX_INTERVAL);
			}
		});
	}
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[]{RootContextConfig.class, SecurityConfiguration.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{ServletContextConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}
}
