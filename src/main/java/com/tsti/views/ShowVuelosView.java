package com.tsti.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

import com.tsti.dao.VueloDAO;
import com.tsti.entidades.Ciudad;
import com.tsti.entidades.Vuelo;
import com.tsti.i18n.AppI18NProvider;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;


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
	    
	}

	private void configureGrid() {
		List<Vuelo> vuelos = vueloDAO.findAll();
		
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
				
		grid.addClassName("show-vuelos-view");		
    	grid.setSizeFull();
    	
    	grid.setColumns();
    	grid.setColumnReorderingAllowed(true);
    	
    	grid.addColumn(Vuelo::getNroVuelo).setHeader(flightIdLabel)
    							.setFrozen(true)
    							.setFooter(createFooterText(vuelos));    									      	
    	Grid.Column<Vuelo> airlineColumn = grid 
    			.addColumn(Vuelo::getAerolinea).setHeader(airlineLabel);       
        //grid.addColumn(Vuelo::getFechaPartida).setHeader(dateHourLabel);
        grid.addColumn(Vuelo::getFechaPartida).setHeader(dateLabel);
        grid.addColumn(Vuelo::getHoraPartida).setHeader(hourLabel);
        Grid.Column<Vuelo> departureColumn = grid
        	.addColumn(vuelo -> vuelo.getOrigen().getNombreCiudad()
        	+ ", " + vuelo.getOrigen().getPais()).setHeader(departureLabel);
        grid.addColumn(vuelo -> vuelo.getDestino().getNombreCiudad()
        	+ ", " + vuelo.getOrigen().getPais()).setHeader(arrivalLabel);
        Grid.Column<Vuelo> typeColumn = 
        grid.addColumn(Vuelo::getTipoVuelo).setHeader(typeLabel);
        Grid.Column<Vuelo> statusColumn = grid
        		.addColumn(Vuelo::getEstadoVuelo).setHeader(statusLabel);
        Grid.Column<Vuelo> aircraftColumn = grid
        		.addColumn(Vuelo::getAvion).setHeader(aircraftLabel);
        Grid.Column<Vuelo> seatsColumn = grid
        		.addColumn(Vuelo::getNroAsientos).setHeader(seatsLabel);        								
                
        grid.getColumns().forEach(column -> column.setAutoWidth(true));       
        
        
        grid.setItems(vuelos);

        add(grid);        
		
	}
	
	private String createFooterText(List<Vuelo> vuelos){
		String totalFlightsLabel = i18NProvider.getTranslation("total-flights", getLocale());
		
		long flightCount = vuelos.stream().count();
		
		return String.format(totalFlightsLabel + "%s", flightCount);
	}	
}
