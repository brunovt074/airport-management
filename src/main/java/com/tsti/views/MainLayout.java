package com.tsti.views;

import com.tsti.i18n.AppI18NProvider;
import com.tsti.security.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;

import jakarta.annotation.security.PermitAll;

/**
 * Main Layout de la aplicación / Application's Main Layout
 **/
//PermitAll()
//@Route("")
//@JsModule("prefers-color-scheme.js")
public class MainLayout extends AppLayout{

	private static final long serialVersionUID = 2007966093366404191L;
	private final SecurityService securityService;
	private final AppI18NProvider i18NProvider;	
	private String contactLabel;
	private String appInfoLabel;
	
	public MainLayout(AppI18NProvider i18NProvider, SecurityService securityService) {
		this.securityService = securityService;
		this.i18NProvider = i18NProvider;		
		this.contactLabel = i18NProvider.getTranslation("contact-tab", getLocale());
		this.appInfoLabel = i18NProvider.getTranslation("app-info-tab", getLocale()); 
		
		createNavBar();	
		
	}
	
	private Dialog aboutDialog() {
		String text = i18NProvider.getTranslation("info-dialog-text", getLocale());
		String createdBy = i18NProvider.getTranslation("created-by", getLocale());
		Paragraph p1 = new Paragraph(text);
		Paragraph p2 = new Paragraph(createdBy);
		Anchor appGithub = new Anchor("https://github.com/brunovt074/airport-management","Project's Github: https://github.com/brunovt074/airport-management");
		VerticalLayout dialogLayout = new VerticalLayout(new Hr(),
														p1,
														new Hr(),
														p2, 
														appGithub);
		dialogLayout.setPadding(false);
		dialogLayout.setSpacing(false);
		dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
		dialogLayout.getStyle().set("width", "24rem");
		
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
	
	private void createNavBar() {
		String u = securityService.getAuthenticatedUser().getUsername();
		String titleText = i18NProvider.getTranslation("title", getLocale());
		H2 title = new H2(titleText);		
		Image logo = new Image("themes/airportmanagement/images/logo-1-t.png", titleText);		
		logo.addClassName("logo");		
		title.addClassNames(LumoUtility.FontSize.LARGE,
				   LumoUtility.Margin.MEDIUM);
		Button logout = new Button("Log out", e -> securityService.logout());
		
		Div logoDiv = new Div();
		Div backgroundDiv = new Div();
		Div toggleModeDiv = new Div();
		Div infoDiv = new Div();
		
		logoDiv.addClassName("logo-div");
		backgroundDiv.addClassName("background-div");
		toggleModeDiv.addClassName("toggle-div");
		infoDiv.addClassName("tab-div"); 
		
		backgroundDiv.getStyle().set("min-width", "0");
		backgroundDiv.getStyle().set("flex-grow", "1");
				
		Button info = new Button(new Icon(VaadinIcon.INFO_CIRCLE));		
		setInfoMenu(info);
		
		info.addClassName("info-tab");		
		logoDiv.add(logo);		
		infoDiv.add(info);	
		
		Button darkLightToggleButton = getDarkLightToggleButton();
		darkLightToggleButton.addClassName("toggle-button");		
		toggleModeDiv.add(darkLightToggleButton);		
		
		HorizontalLayout header = new HorizontalLayout(logoDiv,backgroundDiv,infoDiv,toggleModeDiv, logout);
		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);		
		header.setWidthFull();		
		header.addClassNames(LumoUtility.Padding.Vertical.NONE,
				 LumoUtility.Padding.Horizontal.NONE);
		
		header.addClassName("navbar");		
		
		addToNavbar(header);
		
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
	
	private void setInfoMenu(Button button) {
		
		ContextMenu menu = new ContextMenu(button);
		Button appInfoButton = new Button(appInfoLabel, e -> aboutDialog());
		appInfoButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		
		H4 contactTitle = new H4(contactLabel);
		H5 linkedinTitle = new H5("Linkedin:");
		H5 githubTitle = new H5("Github:");		
		H5 emailTitle = new H5("Email:");
		Span email = new Span("brunodev.pdl@gmail.com");
		Anchor github = new Anchor("https://github.com/brunovt074","https://github.com/brunovt074");
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

}