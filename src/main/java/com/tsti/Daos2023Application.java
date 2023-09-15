package com.tsti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

@SpringBootApplication
@ComponentScan("com.tsti")
@ComponentScan(basePackages = {"com.tsti.faker"})
@Theme(value="airportmanagement")
@PWA(name = "Ibera Airport Management App", 
	 shortName = "Ibera Airport")
public class Daos2023Application implements WebMvcConfigurer, AppShellConfigurator{
	
	private static final long serialVersionUID = -3121037374624477448L;

	/*
	 * private final LocaleChangeInterceptor localeChangeInterceptor;
	 * 
	 * 
	 * public Daos2023Application(LocaleChangeInterceptor localeChangeInterceptor) {
	 * super(); this.localeChangeInterceptor = localeChangeInterceptor; }
	 * 
	 * @Override public void addInterceptors(InterceptorRegistry
	 * interceptorRegistry) {
	 * interceptorRegistry.addInterceptor(localeChangeInterceptor); }
	 */	
	
	public static void main(String[] args) {
		SpringApplication.run(Daos2023Application.class, args);	
		
	}
	
}
