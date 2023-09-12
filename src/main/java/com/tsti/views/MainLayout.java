package com.tsti.views;

import org.springframework.hateoas.Link;

import com.tsti.i18n.AppI18NProvider;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
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
public class MainLayout extends AppLayout{

	private static final long serialVersionUID = 2007966093366404191L;
	
	private final AppI18NProvider i18NProvider;
	
	public MainLayout(AppI18NProvider i18NProvider) {
		this.i18NProvider = i18NProvider;
		
		Button darkLightToggleButton;
		
		createNavBar();		
		
	}
	
	private void createNavBar() {
		String title = i18NProvider.getTranslation("title", getLocale());
		
		H1 logo = new H1(title);
		logo.addClassNames(LumoUtility.FontSize.LARGE,
				   LumoUtility.Margin.MEDIUM);
		
		Tabs tabs = createTabs();		

		Button darkLightToggleButton = createDarkLightToggleButton();
		
		HorizontalLayout header = new HorizontalLayout(logo, darkLightToggleButton/*tabs*/);
		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		//header.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		header.expand(logo);
		header.setWidthFull();
		header.addClassNames(LumoUtility.Padding.Vertical.NONE,
				 LumoUtility.Padding.Horizontal.MEDIUM);
		
		addToNavbar(header);
		
	}
	
	private Tabs createTabs() {
		String vuelosLabel = i18NProvider.getTranslation("flights-tab", getLocale());
		String passengersLabel= i18NProvider.getTranslation("passengers-tab", getLocale());
		String citiesLabel= i18NProvider.getTranslation("cities-tab", getLocale());
		
		Tab vuelos = new Tab(vuelosLabel/*new RouterLink("Vuelos", ShowVuelosView.class)*/);
		Tab pasajeros = new Tab(passengersLabel);
		Tab ciudades = new Tab(citiesLabel);
		//Tab pasajes = new Tab("Pasajes");
		vuelos.setVisible(false);
		pasajeros.setVisible(false);
		ciudades.setVisible(false);
		
		return new Tabs(vuelos, pasajeros, ciudades);
		
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
