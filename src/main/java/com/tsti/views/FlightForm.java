package com.tsti.views;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import com.tsti.dao.AeropuertoDAO;
import com.tsti.entidades.Aeropuerto;
import com.tsti.entidades.Vuelo;
import com.tsti.i18n.AppI18NProvider;
import com.tsti.servicios.VueloServiceImpl;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;


public class FlightForm extends FormLayout{
		
	private static final long serialVersionUID = 5593602849999149819L;
	private final AppI18NProvider i18NProvider;	
	private final VueloServiceImpl vueloService;
	private final AeropuertoDAO aeropuertoDao;
	
	TextField nroVuelo = new TextField("Flight Number");
	DatePicker fechaPartida = new DatePicker("Date");
	LocalDate now = LocalDate.now();
	TimePicker horaPartida = new TimePicker("Hour");	
	ComboBox<Aeropuerto> destino = new ComboBox<>("Arrival");
	
	ComboBox<String> aerolinea = new ComboBox<>("aerolinea"); 
	BigDecimalField precioNeto = new BigDecimalField();  
	TextField avion = new TextField("Aircraft");
	
	String flightIdLabel;
	String airlineLabel;
	String aircraftLabel;
	String departureLabel;
	String arrivalLabel;
	String typeLabel;
	String dateLabel;
	String timeLabel;
	String priceLabel;
	String statusLabel;
	String seatsLabel;
	String saveButtonLabel;
	String cancelButtonLabel;
	String closeButtonLabel;
	
	Button save;
	Button close;

	Binder<Vuelo> binder = new BeanValidationBinder<>(Vuelo.class);	
	
	public FlightForm(AppI18NProvider i18nProvider, VueloServiceImpl service, AeropuertoDAO aeropuertoDao) {
		super();
		this.i18NProvider = i18nProvider;	
		this.vueloService = service;
		this.aeropuertoDao = aeropuertoDao;
		
		addClassName("flight-form");
		binder.bindInstanceFields(this);
		
		//Labels
		initializeFields();	
		//
		configureDateTimePickers(); 
		//Buttons
		save = new Button(saveButtonLabel);
		close = new Button(closeButtonLabel );		
		
		add(nroVuelo,						
			destino,			
			fechaPartida,
			horaPartida,
			precioNeto,
			aerolinea,
			createButtonsLayout());
	}
	
	private void configureDateTimePickers() {
		this.fechaPartida.setMin(now);
		this.fechaPartida.setMax(now.plusDays(330));
		this.fechaPartida.setInitialPosition(now);
		this.horaPartida.setStep(Duration.ofMinutes(1));
	}
	
	private Component createButtonsLayout() {
		
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
		close.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SUCCESS);
		
		save.addClickShortcut(Key.ENTER);
		close.addClickShortcut(Key.ESCAPE);
		
		save.addClickListener(event -> validateAndSave());
		close.addClickListener(event -> fireEvent(new CloseEvent(this)));
		
		binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
		//binder.addValueChangeListener(e -> save.setEnabled(binder.isValid()));
		
		return new HorizontalLayout(save, close);
	}
	
	private void initializeFields(){
		flightIdLabel = i18NProvider.getTranslation("flight-id", getLocale());
		airlineLabel = i18NProvider.getTranslation("airline", getLocale());		
		departureLabel = i18NProvider.getTranslation("departure", getLocale());
		arrivalLabel = i18NProvider.getTranslation("arrival", getLocale());
		typeLabel = i18NProvider.getTranslation("type", getLocale());
		dateLabel = i18NProvider.getTranslation("departure-date", getLocale());
		timeLabel = i18NProvider.getTranslation("departure-hour", getLocale());
		priceLabel = i18NProvider.getTranslation("price", getLocale());
		statusLabel = i18NProvider.getTranslation("flight-status", getLocale());
		seatsLabel = i18NProvider.getTranslation("seats-number", getLocale());
		saveButtonLabel = i18NProvider.getTranslation("save", getLocale());
		cancelButtonLabel = i18NProvider.getTranslation("cancel", getLocale());
		closeButtonLabel = i18NProvider.getTranslation("close", getLocale());
		//setear labels
		nroVuelo.setLabel(flightIdLabel);
		nroVuelo.setReadOnly(true);
		aerolinea.setLabel(airlineLabel);		
		destino.setLabel(arrivalLabel);
		fechaPartida.setLabel(dateLabel);
		horaPartida.setLabel(timeLabel);
		precioNeto.setLabel(priceLabel);		
		aerolinea.setItems(vueloService.getAerolineas());				
		destino.setItems(aeropuertoDao.findAll());
		destino.setItemLabelGenerator(ciudad -> ciudad.getCity() 
										+ ", " + ciudad.getCountry());
		destino.addValueChangeListener(event -> {
		    Vuelo vuelo = binder.getBean(); // Obtener el objeto Vuelo vinculado al formulario
		    if(vuelo != null) {
		    	vuelo.setDestino(event.getValue());// Actualizar la propiedad destino con la ciudad seleccionada 
		    }
		nroVuelo.addClassName("flight-id-textfield");	
	    
		});
		
		
	}

	private void validateAndSave() {
		if(binder.isValid()) {
			fireEvent(new SaveEvent(this, binder.getBean()));
		}		
	}
	
	public void setFlight(Vuelo vuelo) {
		binder.setBean(vuelo);		
	}
	
	//Events & Listeners
	public static abstract class FlightFormEvent extends ComponentEvent<FlightForm>{
		
		private static final long serialVersionUID = 337235857761915025L;
		
		private Vuelo vuelo;
		
		public FlightFormEvent(FlightForm source, Vuelo vuelo) {
			super(source, false);
			this.vuelo = vuelo;
		}
		
		public Vuelo getVuelo() {
			return vuelo;			
		}
		
	}
	
	public static class SaveEvent extends FlightFormEvent {

		private static final long serialVersionUID = 7699673655922195575L;

		public SaveEvent(FlightForm source, Vuelo vuelo) {
			super(source, vuelo);			
		}
		
	}
	
	public static class DeleteEvent extends FlightFormEvent {

		private static final long serialVersionUID = -4936623292912312293L;

		public DeleteEvent(FlightForm source, Vuelo vuelo) {
			super(source, vuelo);			
		}		
	}
	
	public static class CloseEvent extends FlightFormEvent {
		
		private static final long serialVersionUID = 177022814965118984L;

		public CloseEvent(FlightForm source) {
			super(source, null);			
		}		
	}
	
	public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
		return addListener(DeleteEvent.class, listener);		
	}
	
	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);		
	}
	
	public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
		return addListener(CloseEvent.class, listener);		
	}

	public Long getNroVueloValue() {
		try {
		
			return Long.parseLong(nroVuelo.getValue());
					
		} catch(NumberFormatException e) {
			
		  return null;
		  
		}		
		
	}

	public void setNroVuelo(TextField nroVuelo) {
		this.nroVuelo = nroVuelo;
	}

	public String getAerolineaValue() {
		return aerolinea.getValue();
	}

	public LocalDate getFechaPartidaValue() {
		return fechaPartida.getValue();
	}

	public void setFechaPartida(DatePicker fechaPartida) {
		this.fechaPartida = fechaPartida;
	}

	public LocalTime getHoraPartidaValue() {
		return horaPartida.getValue();
	}

	public void setHoraPartida(TimePicker horaPartida) {
		this.horaPartida = horaPartida;
	}

	public Aeropuerto getDestinoValue() {
		return destino.getValue();
	}
	
	public String getNombreCiudadValue() {
		
		return destino.getValue().getCity();
	}

	public void setDestino(ComboBox<Aeropuerto> destino) {
		this.destino = destino;
	}

	public BigDecimal getPrecioNetoValue() {
		return precioNeto.getValue();
	}

	public void setPrecioNeto(BigDecimalField precioNeto) {
		this.precioNeto = precioNeto;
	}

	public String getAvionValue() {
		return avion.getValue();
	}

	public void setAvion(TextField avion) {
		this.avion = avion;
	}

}
