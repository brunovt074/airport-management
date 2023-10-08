package com.tsti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

@SpringBootApplication
@ComponentScan("com.tsti")
@ComponentScan(basePackages = {"com.tsti.faker"})
@Theme(value="airportmanagement")
@PWA(name = "Ibera Airport Management App", 
	 shortName = "IberaAirport")
public class Daos2023Application implements WebMvcConfigurer, AppShellConfigurator{
	
	private static final long serialVersionUID = -3121037374624477448L;

	public static void main(String[] args) {
		SpringApplication.run(Daos2023Application.class, args);	
		
	}
	
}
