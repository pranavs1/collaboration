package com.niit.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

//Similar to web.xml file. WebAppInitializer will get loaded automatically and initialize DispatcherServlet
public class Webappinitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	public Webappinitializer(){
		System.out.println("WebAppInitializer class is instantiated... and DispatcherServer is initialized");
	}
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		// TODO Auto-generated method stub
		return new Class[]{Webappconfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		// TODO Auto-generated method stub
		return new String[]{"/"};//url pattern - <servlet-mapping> </servlet-mapping>
	}

}
