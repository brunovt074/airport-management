package com.tsti.views;

import java.time.LocalDate;
import java.util.List;

import com.tsti.dao.CiudadDAO;
import com.tsti.dao.VueloDAO;
import com.tsti.entidades.Vuelo;
import com.tsti.excepcion.VueloException;
import com.tsti.i18n.AppI18NProvider;
import com.tsti.servicios.VueloServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value="/flights", layout = MainLayout.class)
@PageTitle("Flights")
public class ShowFlightsView extends VerticalLayout{
	
	private static final long serialVersionUID = -7236223778352535392L;
	private final AppI18NProvider i18NProvider;
	private VueloDAO vueloDao;
	private CiudadDAO ciudadDao;
	private VueloServiceImpl service;	
	
	Grid<Vuelo> grid = new Grid<>(Vuelo.class,false);
	TextField filterText = new TextField();
	FlightForm form;
	LocalDate nowDate = LocalDate.now();
	
	public ShowFlightsView(AppI18NProvider i18NProvider, VueloDAO vueloDao, CiudadDAO ciudadDao, VueloServiceImpl service) {
	    
		this.i18NProvider = i18NProvider;
	    this.vueloDao = vueloDao;
	    this.ciudadDao = ciudadDao;
	    this.service = service;
	    
	    List<Vuelo> vuelos = vueloDao.findAll();
	    
	    addClassName("show-flights-view");
	    setSizeFull();
	    configureGrid(vuelos);
	    configureForm();
	    
	    add(getToolbar(vuelos), getContent());
	    
	    updateList(vuelos);
	    closeEditor();
	    
	}
	
	private void closeEditor() {
		
		form.setFlight(null);
		form.setVisible(false);
		removeClassName("editing");
		
	}
	
	private void configureForm() {
		//INCONCLUSO
		form = new FlightForm(i18NProvider, ciudadDao);
		form.setWidth("25em");
		form.addSaveListener(this::saveFlight);
    	form.addDeleteListener(this::deleteFlight);
    	form.addCloseListener(e -> closeEditor());
		
	}

	private void configureGrid(List<Vuelo> vuelos) {
		
		//Labels
		String flightIdLabel = i18NProvider.getTranslation("flight-id", getLocale());
		String airlineLabel = i18NProvider.getTranslation("airline", getLocale());
		String aircraftLabel = i18NProvider.getTranslation("aircraft", getLocale());
		String departureLabel = i18NProvider.getTranslation("departure", getLocale());
		String arrivalLabel = i18NProvider.getTranslation("arrival", getLocale());
		String typeLabel = i18NProvider.getTranslation("type", getLocale());
		//String dateHourLabel = i18NProvider.getTranslation("date-hour", getLocale());
		String dateLabel = i18NProvider.getTranslation("departure-date", getLocale());
		String hourLabel = i18NProvider.getTranslation("departure-hour", getLocale());
		String statusLabel = i18NProvider.getTranslation("flight-status", getLocale());
		String seatsLabel = i18NProvider.getTranslation("seats-number", getLocale());
		String searchPlaceholder = i18NProvider.getTranslation("search", getLocale());
		String showHideMenuLabel = i18NProvider.getTranslation("sh-menu-title", getLocale());
		String editLabel = i18NProvider.getTranslation("edit", getLocale());
		
		grid.addClassName("flights-grid");		
    	grid.setSizeFull();
		    	
    	grid.setColumns();
    	grid.setColumnReorderingAllowed(true);
    	
    	//NroVuelo
    	grid.addColumn(Vuelo::getNroVuelo).setHeader(flightIdLabel)
    							.setFrozen(true)
    							.setFooter(createFooterText(vuelos))
    							.setSortable(true);
    	//Airline
    	grid.addColumn(Vuelo::getAerolinea).setHeader(airlineLabel)
    							.setSortable(true);    	
    	//Date
    	grid.addColumn(Vuelo::getFechaPartida).setHeader(dateLabel)
    			  		.setSortable(true);
		//Hora config
    	grid.addColumn(Vuelo::getHoraPartida)
    			.setHeader(hourLabel)
    			.setSortable(true);
        
    	//Departure
//    	grid.addColumn(vuelo -> vuelo.getOrigen().getNombreCiudad()
//        	+ ", " + vuelo.getOrigen().getPais()).setHeader(departureLabel)
//        						.setSortable(true)
//        						.setResizable(true);        
        //Arrival
    	grid.addColumn(vuelo -> vuelo.getDestino().getNombreCiudad()
        	+ ", " + vuelo.getDestino().getPais()).setHeader(arrivalLabel)
        						.setSortable(true);
        //Type
    	grid.addColumn(Vuelo::getTipoVuelo).setHeader(typeLabel)
        						.setSortable(true)
        						.setResizable(true);
        //Status
    	grid.addColumn(Vuelo::getEstadoVuelo).setHeader(statusLabel)
        						.setSortable(true)
        						.setResizable(true);       
        //Aircraft
    	grid.addColumn(Vuelo::getAvion).setHeader(aircraftLabel)
        						.setSortable(true)
        						.setResizable(true);
    	
    	grid.asSingleSelect().addValueChangeListener(event ->
    			editFlight(event.getValue()));
    	
    	grid.getColumns().forEach(column -> column.setAutoWidth(true));
		
	}

	private String createFooterText(List<Vuelo> vuelos) {
		String totalFlightsLabel = i18NProvider.getTranslation("total-flights", getLocale());
		long flightCount = vuelos.stream().count();
		
		return String.format(totalFlightsLabel + "%s", flightCount);
	}
	
	private void deleteFlight(FlightForm.DeleteEvent event) {
		try {
			
			service.cancelarVuelo(event.getVuelo().getNroVuelo());
		
		} catch (VueloException e) {
			e.setMensaje("No se pudo completar la operacion");
			System.out.println(e.getMessage());
			
		}
		updateList();
		closeEditor();
	}
	
	private void editFlight(Vuelo vuelo) {
		
		if(vuelo == null) {
			
			closeEditor();
			
		} else {
			
			form.setFlight(vuelo);
			form.setVisible(true);
			addClassName("editing");
			
		}
		
		
	}

	private HorizontalLayout getContent() {
		
		HorizontalLayout content = new HorizontalLayout(grid, form);
		
		content.setFlexGrow(2, grid);
		content.setFlexGrow(1, form);
		content.addClassName("content");
		content.setSizeFull();
		
		return content;
	}
	
	
	private HorizontalLayout getToolbar(List<Vuelo> vuelos) {
		//Filter
		String searchPlaceholder = i18NProvider.getTranslation("search", getLocale());
		String newFlightLabel = i18NProvider.getTranslation("new-flight", getLocale());
		
		filterText.setPlaceholder(searchPlaceholder);
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e->updateList(vuelos));
		
		//Add Contact Button
		Button newFlightButton = new Button(newFlightLabel);
		newFlightButton.addClickListener(e -> newFlight());
		
		//Layout
		HorizontalLayout toolbar = new HorizontalLayout(filterText,
				newFlightButton);
		toolbar.addClassName("toolbar");
		
		return toolbar;
		
		
	}
	
	private boolean matchesTerm(String value, String searchTerm) {
		
		String normalizedValue = value.toLowerCase()
	            .replaceAll("[á]", "a")
	            .replaceAll("[é]", "e")
	            .replaceAll("[í]", "i")
	            .replaceAll("[ó]", "o")
	            .replaceAll("[ú]", "u");
		
		String normalizedSearchTerm = searchTerm.toLowerCase()
	            .replaceAll("[á]", "a")
	            .replaceAll("[é]", "e")
	            .replaceAll("[í]", "i")
	            .replaceAll("[ó]", "o")
	            .replaceAll("[ú]", "u");
				
		return normalizedValue.contains(normalizedSearchTerm);
	}
	
	private void newFlight() {
		
		grid.asSingleSelect().clear();
		editFlight(new Vuelo());
		
	}
	
	private void saveFlight(FlightForm.SaveEvent event) {
		vueloDao.save(event.getVuelo());
		updateList();
		closeEditor();
	}

	private void updateList() {
		vueloDao.findAll();		
	}

	private void updateList(List<Vuelo> vuelos) {
		GridListDataView<Vuelo> dataView = grid.setItems(vuelos);
		
		dataView.addFilter(vuelo -> {
        	String searchTerm = filterText.getValue().trim();
        	searchTerm = searchTerm.replace("","");
        	
        	if(searchTerm.isEmpty())
        		return true;
        	
        	boolean matchesDestino = matchesTerm(vuelo.getDestino().getNombreCiudad(), searchTerm);
        	boolean matchesAerolinea = matchesTerm(vuelo.getAerolinea(), searchTerm);
        	boolean matchesAeronave = matchesTerm(vuelo.getAvion(), searchTerm);
        	boolean matchesTipo = matchesTerm(vuelo.getTipoVuelo().toString(), searchTerm);
        	boolean matchesStatus = matchesTerm(vuelo.getEstadoVuelo().toString(), searchTerm);
        	boolean matchesId = matchesTerm(vuelo.getNroVuelo().toString(), searchTerm);
        	boolean matchesFechaPartida = matchesTerm(vuelo.getFechaPartida().toString(), searchTerm);
        	boolean matchesHoraPartida = matchesTerm(vuelo.getHoraPartida().toString(), searchTerm);
        	
        	
        	return matchesDestino || matchesAerolinea || matchesAeronave ||matchesTipo 
        			|| matchesStatus || matchesId 
        			|| matchesFechaPartida || matchesHoraPartida;
        });
		//grid.setItems(service.findByDestino(filterText.getValue()));
	}
	
}
