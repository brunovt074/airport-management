package com.tsti.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;



@Route("/lista-vuelos")
public class MainLayout extends AppLayout{

	private static final long serialVersionUID = 2007966093366404191L;
	
	public MainLayout() {
		
		createNavBar();
		
		
		
	}
	
	private void createNavBar() {
		
		H1 title = new H1("Sauce Viejo Airport Management");
		//title.getStyle().set("font-size", )
		
		
	}
	
	private Tabs createTabs() {
		
		Tab vuelos = new Tab(new RouterLink("Vuelos", VuelosListView.class));
		Tab clientes = new Tab("Clientes");
		Tab ciudades = new Tab("Ciudades");
		//Tab pasajes = new Tab("Pasajes");
		
		
		return new Tabs(vuelos, clientes, ciudades);
		
	}

}
