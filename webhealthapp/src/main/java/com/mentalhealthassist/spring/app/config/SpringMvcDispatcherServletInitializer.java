package com.mentalhealthassist.spring.app.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

//servlet initialisation based on all java configuration
//as directed by spring docs
public class SpringMvcDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}

	@Override				//Link to config class file
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] {MentalHealthAssistAppConfig.class};
	}

	@Override				//base mapping
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}

}
