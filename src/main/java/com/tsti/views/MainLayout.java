package com.tsti.views;

import org.springframework.hateoas.Link;

import com.tsti.i18n.AppI18NProvider;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;



@Route("")
//@JsModule("prefers-color-scheme.js")
public class MainLayout extends AppLayout{

	private static final long serialVersionUID = 2007966093366404191L;
	
	private final AppI18NProvider i18NProvider;
	
	public MainLayout(AppI18NProvider i18NProvider) {
		this.i18NProvider = i18NProvider;	
		 
		createNavBar();		
		
	}
	
	private void createNavBar() {
		String titleText = i18NProvider.getTranslation("title", getLocale());
			
		H2 title = new H2(titleText);
		
		Image logo = new Image("themes/airportmanagement/images/logo-1-t.png", titleText);
		Image background = new Image("themes/airportmanagement/images/logo-2-background.png", titleText);
		
		logo.addClassName("logo");
		background.addClassName("background");
		
		title.addClassNames(LumoUtility.FontSize.LARGE,
				   LumoUtility.Margin.MEDIUM);
		Div logoDiv = new Div(logo);
		Div backgroundDiv = new Div();
		Div toggleModeDiv = new Div();
		logoDiv.addClassName("logo-div");
		backgroundDiv.addClassName("background-div");
		toggleModeDiv.addClassName("toggle-div"); 
		
		backgroundDiv.getStyle().set("min-width", "0");
		backgroundDiv.getStyle().set("flex-grow", "1");
		Tabs tabs = createTabs();		

		Button darkLightToggleButton = createDarkLightToggleButton();
		darkLightToggleButton.addClassName("toggle-button");
		toggleModeDiv.add(darkLightToggleButton);
		
		HorizontalLayout header = new HorizontalLayout(logoDiv,backgroundDiv, toggleModeDiv/*,tabs*/);
		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);		
		header.setWidthFull();		
		header.addClassNames(LumoUtility.Padding.Vertical.NONE,
				 LumoUtility.Padding.Horizontal.NONE);
		
		header.addClassName("navbar");		
		
		addToNavbar(header);
		
	}
	
	private Tabs createTabs() {
		String vuelosLabel = i18NProvider.getTranslation("flights-tab", getLocale());
		String passengersLabel= i18NProvider.getTranslation("passengers-tab", getLocale());
		String citiesLabel= i18NProvider.getTranslation("cities-tab", getLocale());
		
		Tab vuelos = new Tab(vuelosLabel);
		Tab pasajeros = new Tab(passengersLabel);
		Tab ciudades = new Tab(citiesLabel);
		Tab pasajes = new Tab("Pasajes");
		vuelos.setVisible(false);
		pasajeros.setVisible(false);
		ciudades.setVisible(false);
		
		return new Tabs(vuelos, pasajes, pasajeros, ciudades);
		
	}
	
	private Button createDarkLightToggleButton() {
		String tooltipText = i18NProvider.getTranslation("dark-light-button", getLocale());
		
		Button toggleButton = new Button(new Icon(VaadinIcon.ADJUST), click -> {
			ThemeList themeList = UI.getCurrent().getElement().getThemeList();
			
			if(themeList.contains(Lumo.DARK)) {
				themeList.remove(Lumo.DARK);				
			} else {
				themeList.add(Lumo.DARK);				
			}		
			
		});
		
		toggleButton.setAriaLabel(tooltipText);
		toggleButton.setTooltipText(tooltipText);
		
		return toggleButton;		
		
	}

}
