package com.tsti.i18n;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class I18nConfig {

	
	 @Bean public AcceptHeaderLocaleResolver acceptHeaderLocaleResolver() {
	 
	 final AcceptHeaderLocaleResolver localResolver = new AcceptHeaderLocaleResolver(); 
	 localResolver.setDefaultLocale(Locale.ENGLISH);
	 
	 return localResolver; 
	 
	 }
	 
	 @Bean
	 public LocaleChangeInterceptor localeChangeInterceptor() {
		 
		 LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		 localeChangeInterceptor.setParamName("localeData");
		 
		 return localeChangeInterceptor;
		 
	 }
		
}
