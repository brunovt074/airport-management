package com.tsti.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

import com.tsti.dao.VueloDAO;
import com.tsti.entidades.Vuelo;
import com.tsti.i18n.AppI18NProvider;
import com.vaadin.flow.component.grid.Grid;


@Route(value="/flights-index", layout = MainLayout.class)
@PageTitle("Flights")
public class ShowVuelosView extends VerticalLayout{

	private static final long serialVersionUID = 1605332884677855208L;
	
	private final AppI18NProvider i18NProvider;
	private VueloDAO vueloDAO;
	
	Grid<Vuelo> grid = new Grid<>(Vuelo.class,false);
	TextField filterText = new TextField();
	
	public ShowVuelosView(AppI18NProvider i18NProvider, VueloDAO vueloDAO) {
	    this.i18NProvider = i18NProvider;
	    this.vueloDAO = vueloDAO;

	    addClassName("show-vuelos-view");
	    setSizeFull();
	    configureGrid();
	    
	    getUI().ifPresent(ui -> ui.getPage().setTitle(this.i18NProvider.getTranslation("page-title", getLocale())));
	}

	private void configureGrid() {
	    
		String flightIdLabel = i18NProvider.getTranslation("flight-id", getLocale());
		String airlineLabel = i18NProvider.getTranslation("airline", getLocale());
		String aircraftLabel = i18NProvider.getTranslation("aircraft", getLocale());
		String departureLabel = i18NProvider.getTranslation("departure", getLocale());
		String arrivalLabel = i18NProvider.getTranslation("arrival", getLocale());
		String typeLabel = i18NProvider.getTranslation("type", getLocale());
		String dateHourLabel = i18NProvider.getTranslation("date-hour", getLocale());
		String dateLabel = i18NProvider.getTranslation("departure-date", getLocale());
		String hourLabel = i18NProvider.getTranslation("departure-hour", getLocale());
		String statusLabel = i18NProvider.getTranslation("flight-status", getLocale());
		String seatsLabel = i18NProvider.getTranslation("seats-number", getLocale());
		
		Grid<Vuelo> grid = new Grid<>(Vuelo.class);
		grid.addClassName("show-vuelos-view");		
    	grid.setSizeFull();
    	
    	grid.setColumns();    	
    	grid.addColumn(Vuelo::getNroVuelo).setHeader(flightIdLabel);    	
        grid.addColumn(Vuelo::getAerolinea).setHeader(airlineLabel);
        grid.addColumn(Vuelo::getAvion).setHeader(aircraftLabel);
        grid.addColumn(Vuelo::getFechaPartida).setHeader(departureLabel);
        grid.addColumn(Vuelo::getDestino).setHeader(arrivalLabel);
        grid.addColumn(Vuelo::getTipoVuelo).setHeader(typeLabel);
        grid.addColumn(Vuelo::getFechaPartida).setHeader(dateHourLabel);
        grid.addColumn(Vuelo::getFechaPartida).setHeader(dateLabel);
        grid.addColumn(Vuelo::getHoraPartida).setHeader(hourLabel);
        grid.addColumn(Vuelo::getEstadoVuelo).setHeader(statusLabel);
        grid.addColumn(Vuelo::getNroAsientos).setHeader(seatsLabel);
        
        List<Vuelo> vuelos = vueloDAO.findAll();
        grid.setItems(vuelos);

        add(grid);	
        
		
	}
	
}
