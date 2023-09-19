package com.tsti.views;

import com.tsti.i18n.AppI18NProvider;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
	private String aboutLabel;
	private String contactLabel;
	private String appInfoLabel;
	
	public MainLayout(AppI18NProvider i18NProvider) {
		this.i18NProvider = i18NProvider;
		this.aboutLabel = i18NProvider.getTranslation("about-tab", getLocale());
		this.contactLabel = i18NProvider.getTranslation("contact-tab", getLocale());
		this.appInfoLabel = i18NProvider.getTranslation("app-info-tab", getLocale()); 
		
		createNavBar();		
		createDrawer();
		
	}
	
	private void createNavBar() {
		String titleText = i18NProvider.getTranslation("title", getLocale());
			
		H2 title = new H2(titleText);
		H4 drawerTitle = new H4("App Info:");
		
		Image logo = new Image("themes/airportmanagement/images/logo-1-t.png", titleText);
		Image background = new Image("themes/airportmanagement/images/logo-2-background.png", titleText);
		
		logo.addClassName("logo");
		background.addClassName("background");
		
		title.addClassNames(LumoUtility.FontSize.LARGE,
				   LumoUtility.Margin.MEDIUM);
		Div logoDiv = new Div();
		Div backgroundDiv = new Div();
		Div toggleModeDiv = new Div();
		Div tabDiv = new Div();
		
		logoDiv.addClassName("logo-div");
		backgroundDiv.addClassName("background-div");
		toggleModeDiv.addClassName("toggle-div");
		tabDiv.addClassName("tab-div"); 
		
		backgroundDiv.getStyle().set("min-width", "0");
		backgroundDiv.getStyle().set("flex-grow", "1");
		
		Tabs tabs = getTabs();	
		//DrawerToggle info = new DrawerToggle();
		//Button info = new Button(new Icon("lumo","menu"));
		Button info = new Button(new Icon(VaadinIcon.INFO_CIRCLE));		
		setInfoMenu(info);
		
		info.addClassName("info-tab");		
		logoDiv.add(logo);		
		tabDiv.add(info);	
		
		Button darkLightToggleButton = getDarkLightToggleButton();
		darkLightToggleButton.addClassName("toggle-button");		
		toggleModeDiv.add(darkLightToggleButton);		
		
		HorizontalLayout header = new HorizontalLayout(logoDiv,backgroundDiv,tabDiv,/*info,*/toggleModeDiv);
		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);		
		header.setWidthFull();		
		header.addClassNames(LumoUtility.Padding.Vertical.NONE,
				 LumoUtility.Padding.Horizontal.NONE);
		
		header.addClassName("navbar");		
		
		addToNavbar(header);
		
	}	
	
	private Tabs getTabs() {
		String vuelosLabel = i18NProvider.getTranslation("flights-tab", getLocale());
		String passengersLabel= i18NProvider.getTranslation("passengers-tab", getLocale());
		String citiesLabel= i18NProvider.getTranslation("cities-tab", getLocale());
		
		Tab vuelos = new Tab(vuelosLabel);
		Tab pasajeros = new Tab(passengersLabel);
		Tab ciudades = new Tab(citiesLabel);
		Tab pasajes = new Tab("Pasajes");
		Tab info = new Tab(appInfoLabel);
		info.addClassName("info-tab");
		
		//info.add(getDropDownMenu());		
		
		vuelos.setVisible(false);
		pasajeros.setVisible(false);
		ciudades.setVisible(false);
		
		return new Tabs(info);
		
	}
	
	private Dialog aboutDialog() {
		String text = i18NProvider.getTranslation("info-dialog-text", getLocale());
		String createdBy = i18NProvider.getTranslation("created-by", getLocale());
		Paragraph p1 = new Paragraph(text);
		Paragraph p2 = new Paragraph(createdBy);
		VerticalLayout dialogLayout = new VerticalLayout(new Hr(),
														p1,
														new Hr(),
														p2);
		dialogLayout.setPadding(false);
		dialogLayout.setSpacing(false);
		dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
		dialogLayout.getStyle().set("width", "26rem");
		
		Dialog dialog = new Dialog(dialogLayout);
		Button closeButton = new Button(new Icon("lumo","cross"), e -> dialog.close());
		closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		
		
		dialog.setHeaderTitle(appInfoLabel);		
		dialog.getHeader().add(closeButton);		
		dialog.setModal(false);		
		dialog.setDraggable(true);
		dialog.setResizable(true);		
		
		return dialog;
	}
	
	private void setInfoMenu(Button button) {
		
		ContextMenu menu = new ContextMenu(button);
		Button appInfoButton = new Button(appInfoLabel, e -> aboutDialog());
		appInfoButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		
		H4 contactTitle = new H4(contactLabel);
		H5 linkedinTitle = new H5("Linkedin:");
		H5 githubTitle = new H5("Github:");		
		H5 emailTitle = new H5("Email:");
		Anchor email = new Anchor("brunodev.pdl@gmail.com","brunodev.pdl@gmail.com");
		Anchor github = new Anchor("https://github.com/brunovt074/airport-management","https://github.com/brunovt074/airport-management");
		Anchor linkedin = new Anchor("https://www.linkedin.com/in/bruno-vargas-tettamanti-developer/", "https://www.linkedin.com/in/bruno-vargas-tettamanti-developer/");
		contactTitle.getStyle().setTextDecoration("underline");
		contactTitle.getStyle().setFont("bold");		
		contactTitle.addClassName("contact-title");
		linkedinTitle.addClassName("linkedin-title");
		githubTitle.addClassName("github-title");
		emailTitle.addClassName("email-title");
		
		menu.addClassName("app-info-menu");
		
		menu.setOpenOnClick(true);
		//about
		menu.addItem(new Button(appInfoLabel), e -> aboutDialog().open());
		menu.add(new Hr());
		//ContactInfo		 
		menu.addItem(contactTitle).setEnabled(false);		
		menu.addItem(linkedinTitle).setEnabled(false);
		menu.addItem(linkedin);		
		menu.add(new Hr());
		menu.addItem(githubTitle).setEnabled(false);
		menu.addItem(github);
		menu.add(new Hr());
		menu.addItem(emailTitle).setEnabled(false);
		menu.addItem(email);
		menu.add(new Hr());	
		
		
	}
	
	private Button getDarkLightToggleButton() {
		String tooltipText = i18NProvider.getTranslation("dark-light-button", getLocale());
		Icon toggleModeIcon = new Icon(VaadinIcon.ADJUST);
		toggleModeIcon.addClassName("toggle-mode-icon"); 
		
		Button toggleButton = new Button(toggleModeIcon, click -> {
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
	
	private void createDrawer() {
		Button aboutButton = new Button(aboutLabel); 
		H4 drawerTitle = new H4("App Info"); 
		aboutButton.addClassName("about-button");
		
		addToDrawer(drawerTitle,
					aboutButton,					 
					new VerticalLayout(getDropDownMenu()));
		
	}
	
	private Details getDropDownMenu() {
		
		Details infoDropdown;
						
		H5 emailTitle = new H5("Email:");
		H5 githubTitle = new H5("Github:");
		H5 linkedinTitle = new H5("Linkedin:");
		Hr hr1 = new Hr();
		Hr hr2 = new Hr();
		Hr hr3 = new Hr();		
		
		Anchor email = new Anchor("brunodev.pdl@gmail.com","brunodev.pdl@gmail.com");
		Anchor github = new Anchor("https://github.com/brunovt074/airport-management","https://github.com/brunovt074/airport-management");
		Anchor linkedin = new Anchor("https://www.linkedin.com/in/bruno-vargas-tettamanti-developer/", "https://www.linkedin.com/in/bruno-vargas-tettamanti-developer/");
				
		VerticalLayout content = new VerticalLayout(githubTitle,
				github,
				hr1,
				linkedinTitle,
				linkedin,
				hr2,
				emailTitle,
				email,
				hr3); 
		
		content.setSpacing(false);
		content.setPadding(false);
		
		infoDropdown = new Details(contactLabel, content);
		
		infoDropdown.setOpened(true);	
		
		return infoDropdown;
	}

}