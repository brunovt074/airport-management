package com.tsti.views;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.text.Normalizer;
import java.util.List;

import com.tsti.dao.VueloDAO;
import com.tsti.entidades.Ciudad;
import com.tsti.entidades.Vuelo;
import com.tsti.i18n.AppI18NProvider;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;


@Route(value="/flights-index", layout = MainLayout.class)
@PageTitle("Flights")
public class ShowVuelosView extends VerticalLayout{

	private static final long serialVersionUID = 1605332884677855208L;
	
	private final AppI18NProvider i18NProvider;
	private VueloDAO vueloDAO;
	
	Grid<Vuelo> grid = new Grid<>(Vuelo.class,false);	
	TextField searchField = new TextField();
	
	public ShowVuelosView(AppI18NProvider i18NProvider, VueloDAO vueloDAO) {
	    this.i18NProvider = i18NProvider;
	    this.vueloDAO = vueloDAO;

	    addClassName("show-vuelos-view");
	    setSizeFull();
	    //add(getToolbar());
	    configureGrid();    
	    
	}

	private void configureGrid() {
		
		List<Vuelo> vuelos = vueloDAO.findAll();
		
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
		
		grid.addClassName("show-vuelos-view");		
    	grid.setSizeFull();
    	
    	grid.setColumns();
    	grid.setColumnReorderingAllowed(true);
    	
    	//Columns
    	grid.addColumn(Vuelo::getNroVuelo).setHeader(flightIdLabel)
    							.setFrozen(true)
    							.setFooter(createFooterText(vuelos))
    							.setSortable(true);    									      	
    	Grid.Column<Vuelo> airlineColumn = grid 
    			.addColumn(Vuelo::getAerolinea).setHeader(airlineLabel)
    							.setSortable(true);       
        //grid.addColumn(Vuelo::getFechaPartida).setHeader(dateHourLabel);
        grid.addColumn(Vuelo::getFechaPartida).setHeader(dateLabel)
        						.setSortable(true);
        grid.addColumn(Vuelo::getHoraPartida).setHeader(hourLabel)
        						.setSortable(true);
        Grid.Column<Vuelo> departureColumn = grid
        	.addColumn(vuelo -> vuelo.getOrigen().getNombreCiudad()
        	+ ", " + vuelo.getOrigen().getPais()).setHeader(departureLabel)
        						.setSortable(true);
        Grid.Column<Vuelo> arrivalColumn = grid.addColumn(vuelo -> vuelo.getDestino().getNombreCiudad()
        	+ ", " + vuelo.getOrigen().getPais()).setHeader(arrivalLabel)
        						.setSortable(true);
        Grid.Column<Vuelo> typeColumn = grid 
        		.addColumn(Vuelo::getTipoVuelo).setHeader(typeLabel)
        						.setSortable(true);
        Grid.Column<Vuelo> statusColumn = grid
        		.addColumn(Vuelo::getEstadoVuelo).setHeader(statusLabel)
        						.setSortable(true);
        Grid.Column<Vuelo> aircraftColumn = grid
        		.addColumn(Vuelo::getAvion).setHeader(aircraftLabel)
        						.setSortable(true);
//        Grid.Column<Vuelo> seatsColumn = grid
//        		.addColumn(Vuelo::getNroAsientos).setHeader(seatsLabel)
//        						.setSortable(true);
        
        grid.getColumns().forEach(column -> column.setAutoWidth(true)); 
        
        GridListDataView<Vuelo> dataView = grid.setItems(vuelos);
        
        searchField.setWidth("100%");
        searchField.setPlaceholder(searchPlaceholder);
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> dataView.refreshAll());
        
        dataView.addFilter(vuelo -> {
        	String searchTerm = searchField.getValue().trim();
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
                
        //Create Show Hide Menu
        Button menuButton = new Button (showHideMenuLabel);
		menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(menuButton);
		
		columnToggleContextMenu.addColumnToggleItem(airlineLabel, airlineColumn);
		columnToggleContextMenu.addColumnToggleItem(departureLabel, departureColumn);
		columnToggleContextMenu.addColumnToggleItem(arrivalLabel, arrivalColumn);
		columnToggleContextMenu.addColumnToggleItem(typeLabel, typeColumn);
		columnToggleContextMenu.addColumnToggleItem(statusLabel, statusColumn);
		columnToggleContextMenu.addColumnToggleItem(aircraftLabel, aircraftColumn);
		//columnToggleContextMenu.addColumnToggleItem(seatsLabel, seatsColumn);
                
		HorizontalLayout toolbar = new HorizontalLayout(searchField, menuButton);        
		
		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		
		add(toolbar, grid);
        
        
		
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

	private String createFooterText(List<Vuelo> vuelos){
		String totalFlightsLabel = i18NProvider.getTranslation("total-flights", getLocale());
		
		long flightCount = vuelos.stream().count();
		
		return String.format(totalFlightsLabel + "%s", flightCount);
	}
	
	private static class ColumnToggleContextMenu extends ContextMenu {
				
		private static final long serialVersionUID = -5617802348824382034L;

		public ColumnToggleContextMenu(Component target) {
			
			super(target);
			setOpenOnClick(true);
			
		}
		
		void addColumnToggleItem(String label, Grid.Column<Vuelo> column) {
			
			MenuItem menuItem = this.addItem(label, e -> {
					column.setVisible(e.getSource().isChecked());
			});
			menuItem.setCheckable(true);
			menuItem.setChecked(column.isVisible());			
		}		
	}

}
