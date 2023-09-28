package com.tsti.views;
import com.tsti.i18n.AppLocaleResolver;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.tsti.i18n.AppI18NProvider;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import jakarta.servlet.http.HttpServletRequest;


/*
 *Map the view to the "loginForm" path. LoginView should encompass the 
 *entire browser window, so don’t use MainLayout as the parent. 
 * 
 **/
@Route(value="loginForm",layout = MainLayout.class)
@PageTitle("Login | Ibera Airport")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver{
	
	private static final long serialVersionUID = -4573410522892153365L;
	private final HttpServletRequest request;	
	private final AppI18NProvider i18nProvider = new AppI18NProvider();	
	private final AppLocaleResolver appLocaleResolver;
	private Locale locale; 
	
	//Instantiate a LoginForm component to capture username and password.
	private final LoginForm loginForm = new LoginForm();
	private LoginI18n loginI18n = LoginI18n.createDefault();	
	
	@Autowired
	public LoginView(HttpServletRequest request, AppLocaleResolver appLocaleResolver) {
		this.request = request;
		this.appLocaleResolver = appLocaleResolver;
		this.locale = appLocaleResolver.resolveLocale(request);
		
		addClassName("login-view");		
		getElement().getStyle().set("background-image", "url('themes/airportmanagement/images/login-bg.webp')");
		
		String language = appLocaleResolver.resolveLocale(request).toString();
		String loginDescription = i18nProvider.getTranslation("login-description", locale);
		//Image bgImage = new Image("themes/airportmanagement/images/login-bg.webp", "bg");
		
		if(language.equalsIgnoreCase("es") || language.equalsIgnoreCase("pt")) {	
			
			String loginTitleLabel = i18nProvider.getTranslation("login-title", locale);
			String loginUsernameLabel = i18nProvider.getTranslation("login-username", locale);  
			String loginPasswordLabel = i18nProvider.getTranslation("login-password", locale);  
			String loginSubmitLabel = i18nProvider.getTranslation("login-submit", locale);
			String loginForgotPasswordLabel = i18nProvider.getTranslation("login-forgot-password",locale);
			String loginErrorTitleLabel = i18nProvider.getTranslation("login-error-title", locale); 
			String loginErrorMessage = i18nProvider.getTranslation("login-error-message", locale);
			String loginErrorUserRequired = i18nProvider.getTranslation("login-user-required",locale);
			String loginPasswordRequired = i18nProvider.getTranslation("login-password-required", locale);
			
			LoginI18n.Form loginI18nForm = loginI18n.getForm();			
			loginI18nForm.setTitle(loginTitleLabel);
			loginI18nForm.setUsername(loginUsernameLabel);
			loginI18nForm.setPassword(loginPasswordLabel);
			loginI18nForm.setSubmit(loginSubmitLabel);
			loginI18nForm.setForgotPassword(loginForgotPasswordLabel);			
			
			LoginI18n.ErrorMessage loginI18nErrorMessage = loginI18n.getErrorMessage();
			loginI18nErrorMessage.setTitle(loginErrorTitleLabel);
			loginI18nErrorMessage.setUsername(loginErrorUserRequired);
			loginI18nErrorMessage.setPassword(loginPasswordRequired);
			loginI18nErrorMessage.setMessage(loginErrorMessage);		
			
			loginI18n.setForm(loginI18nForm);
		}		
		
		loginI18n.setAdditionalInformation(loginDescription);		
		loginForm.setI18n(loginI18n);
		
		/*Make LoginView full size and center its content — both horizontally 
		 *and vertically — by calling setAlignItems(`Alignment.CENTER)` 
		 *and setJustifyContentMode(`JustifyContentMode.CENTER)`.
		 **/
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		
		//Set the LoginForm action to "loginForm" to post the loginForm form 
		//to Spring Security.		
		loginForm.setAction("loginForm");		
				
		add(//bgImage,
				loginForm);
		
	}
	
	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		//Read query parameters and show an error if a loginForm attempt fails.
		if(beforeEnterEvent.getLocation()
						   .getQueryParameters()
						   .getParameters()
						   .containsKey("error")) {
				
			loginForm.setError(true);
		}
		
	}

}
