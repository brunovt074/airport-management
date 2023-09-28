package com.tsti.i18n;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import com.vaadin.flow.i18n.I18NProvider;

@Component
public class AppI18NProvider implements I18NProvider{
	
	private static final long serialVersionUID = -8167167151517182068L;

	@Override
	public List<Locale> getProvidedLocales() {
		
		return List.of(new Locale("en"),
				new Locale("es"),
				new Locale("pt") );
	}

	@Override
	public String getTranslation(String key, Locale locale, Object... params) {
		
		ResourceBundle bundle = ResourceBundle.getBundle("messages", locale); 
		return bundle.getString(key);
	}	

}
