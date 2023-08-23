package com.tsti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@SpringBootApplication
@ComponentScan("com.tsti")
@ComponentScan(basePackages = {"com.tsti.faker"})
public class Daos2023Application implements WebMvcConfigurer{
	
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
