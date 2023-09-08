package com.tsti.views;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import com.tsti.dao.CiudadDAO;
import com.tsti.dao.VueloDAO;
import com.tsti.entidades.Vuelo;
import com.tsti.excepcion.VueloException;
import com.tsti.i18n.AppI18NProvider;
import com.tsti.servicios.VueloServiceImpl;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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
	private Vuelo draggedItem;
	
	Grid<Vuelo> grid = new Grid<>(Vuelo.class,false);
	List<Vuelo> flights;
	GridListDataView<Vuelo> dataView;	
	TextField searchField = new TextField();
	FlightForm form;
	LocalDate nowDate = LocalDate.now();
	
	//Labels
	private String flightIdLabel;
	private String airlineLabel;
	private String aircraftLabel;
	//private String departureLabel;
	private String arrivalLabel;
	private String priceLabel;
	private String typeLabel;	
	private String dateLabel;
	private String hourLabel;
	private String statusLabel;
	//private String seatsLabelocale());		
	//private String showHideMenuLabelLocale());
	//private String editLabel;
	
	public ShowFlightsView(AppI18NProvider i18NProvider, VueloDAO vueloDao, CiudadDAO ciudadDao, VueloServiceImpl service) {
	    
		this.i18NProvider = i18NProvider;
	    this.vueloDao = vueloDao;
	    this.ciudadDao = ciudadDao;
	    this.service = service;
	    this.flights = new ArrayList<>(vueloDao.findAll());
	    this.dataView = grid.setItems(flights);  
	    
	    addClassName("show-flights-view");
	    setSizeFull();
	    
	    initializeLabels();
	    configureGrid();
	    configureForm();
	    
	    add(getToolbar(), getContent());
	    
	    closeEditor();
	    
	}
	
	private void addCustomFilters() {
		
		String searchPlaceholder = i18NProvider.getTranslation("search", getLocale());
        
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
		
	}
	
	private void closeEditor() {
		
		form.setFlight(null);
		form.setVisible(false);
		removeClassName("editing");		
	}
	
	private void configureForm() {

		form = new FlightForm(i18NProvider, ciudadDao);
		form.setWidth("30rem");
		form.addSaveListener(this::saveFlight);
    	form.addDeleteListener(this::deleteFlight);
    	form.addCloseListener(e -> closeEditor());
    	//form.setVisible(false);		
	}

	private void configureGrid() {
		List<Vuelo> vuelos = vueloDao.findAll();		
		
		grid.addClassName("flights-grid");		
    	grid.setSizeFull();
		    	
    	grid.setColumns();
    	grid.setColumnReorderingAllowed(true);
    	
    	initializeDragAndDropOnRows(grid);
    	
    	//NroVuelo
    	grid.addColumn(Vuelo::getNroVuelo).setHeader(flightIdLabel)
    							.setFrozen(true)
    							.setFooter(createFooterText(vuelos))
    							.setSortable(true).setKey("idColumn");
    	//Airline
    	grid.addColumn(Vuelo::getAerolinea).setHeader(airlineLabel)
    							.setSortable(true)
    							.setKey("airlineColumn");    	
    	//Date
    	grid.addColumn(Vuelo::getFechaPartida).setHeader(dateLabel)
    			  		.setSortable(true)
    			  		.setComparator((vuelo1, vuelo2) ->
    					vuelo1.getFechaPartida().compareTo(vuelo2.getFechaPartida()))
    			  		.setKey("dateColumn");
		//Hora config
    	grid.addColumn(Vuelo::getHoraPartida)
    			.setHeader(hourLabel)
    			.setKey("hourColumn");        
    	//Departure
//    	grid.addColumn(vuelo -> vuelo.getOrigen().getNombreCiudad()
//        	+ ", " + vuelo.getOrigen().getPais()).setHeader(departureLabel)
//        						.setSortable(true)
//        						.setResizable(true);        
        //Arrival
    	grid.addColumn(vuelo -> vuelo.getDestino().getNombreCiudad()
        	+ ", " + vuelo.getDestino().getPais()).setHeader(arrivalLabel)
        						.setSortable(true)
    							.setResizable(true)
    							.setKey("arrivalColumn");
    	//Price
    	grid.addColumn(Vuelo::getPrecioNeto).setHeader(priceLabel)
    										.setSortable(true)
    										.setResizable(true)
    										.setAutoWidth(true)
    										.setTextAlign(ColumnTextAlign.END)
    										.setKey("priceColumn");
    	
    	//Status
    	grid.addColumn(Vuelo::getEstadoVuelo).setHeader(statusLabel)
        						.setSortable(true)
        						.setResizable(true)
        						.setKey("statusColumn");    	
        //Type
    	grid.addColumn(Vuelo::getTipoVuelo).setHeader(typeLabel)
        						.setSortable(true)
        						.setResizable(true)        						
        						.setKey("typeColumn")
        						.setVisible(false);               
//        //Aircraft
//    	grid.addColumn(Vuelo::getAvion).setHeader(aircraftLabel)
//        						.setSortable(true)
//        						.setResizable(true)
//        						.setKey("aircraftColumn")
//        						.setVisible(false);
    	//Abrir editor con un solo click    	
//    	grid.asSingleSelect().addValueChangeListener(event ->
//    			editFlight(event.getValue()));
    	
    	//Abrir editor con boton reschedule
//    	grid.addItemDoubleClickListener();
    	//grid.addSelectionListener(null);
    	//grid.getTabIndex();    	
    	//grid.
    	
    	
    	grid.getColumns().forEach(column -> column.setAutoWidth(true));
    	grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES); 
    	
        add(grid);
		
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
			e.setMensaje(i18NProvider.getTranslation("delete-error", getLocale()));
			System.out.println(e.getMessage());
			
		} finally {
			
			updateList();
			addCustomFilters();
			closeEditor();
			
		}	
		
	}
	
	private void editFlight(Vuelo vuelo) {
		
		if(vuelo == null) {
			
			closeEditor();
			
		} else if (vuelo.getNroVuelo() == null){
		
			form.setFlight(vuelo);
			form.aerolinea.setReadOnly(false);
			form.avion.setReadOnly(false);
			form.destino.setReadOnly(false);
			form.precioNeto.setReadOnly(false);
			//form.save.setEnabled(true);
			//form.save.setVisible(true);
			form.setVisible(true);
			addClassName("editing");
			
		} else {
			form.setFlight(vuelo);
			form.aerolinea.setReadOnly(true);
			form.avion.setReadOnly(true);
			form.destino.setReadOnly(true);
			form.precioNeto.setReadOnly(true);
			//form.save.setVisible(false);
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
	
	private HorizontalLayout getToolbar() {
		//Filter
		addCustomFilters();		
		
		//Create buttons
		//Add Contact Button
		String newFlightLabel = i18NProvider.getTranslation("new-flight", getLocale());
		Button newFlightButton = new Button(newFlightLabel);
		newFlightButton.addClickListener(e -> newFlight());
		
		//Reschedule flight button
		String rescheduleFlightLabel = i18NProvider.getTranslation("edit-flight", getLocale());
		Button rescheduleFlightButton = new Button(rescheduleFlightLabel);
		rescheduleFlightButton.addClickListener(e -> rescheduleFlight());
		
		//Add Show Hide Toggle Menu
		String showHideMenuLabel = i18NProvider.getTranslation("sh-menu-title", getLocale());
		Button menuButton = new Button (showHideMenuLabel);		
		menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);	
		
		//Create show/hide menu
		setColumnToggleMenu(menuButton);
		
		//Layout
		HorizontalLayout toolbar = new HorizontalLayout(searchField, newFlightButton,rescheduleFlightButton, menuButton);
		toolbar.addClassName("toolbar");
		
		return toolbar;
		
		
	}
	private void initializeDragAndDropOnRows(Grid<Vuelo> grid) {
		
		grid.setDropMode(GridDropMode.BETWEEN);
		grid.setRowsDraggable(true);
				
		grid.addDragStartListener(
				e -> draggedItem = e.getDraggedItems().get(0)); 
		
		grid.addDropListener( e->	{
			Vuelo targetFlight = e.getDropTargetItem().orElse(null);
			GridDropLocation dropLocation = e.getDropLocation();
			
			boolean flightWasDroppedOntoItself = draggedItem.equals(targetFlight);
			
			if(targetFlight == null || flightWasDroppedOntoItself) 
				return;
			
			dataView.removeItem(draggedItem);
			
			if(dropLocation == GridDropLocation.BELOW) {
				dataView.addItemAfter(draggedItem, targetFlight);
			} else {
				dataView.addItemBefore(draggedItem, targetFlight);				
			}			
		});
		
		grid.addDragEndListener(e -> draggedItem = null);
		
	}
	private void initializeLabels() {
		//Labels
		flightIdLabel = i18NProvider.getTranslation("flight-id", getLocale());
		airlineLabel = i18NProvider.getTranslation("airline", getLocale());
		aircraftLabel = i18NProvider.getTranslation("aircraft", getLocale());
		//departureLabel = i18NProvider.getTranslation("departure", getLocale());
		arrivalLabel = i18NProvider.getTranslation("arrival", getLocale());
		priceLabel = i18NProvider.getTranslation("price", getLocale());
		typeLabel = i18NProvider.getTranslation("type", getLocale());
		//String dateHourLabel = i18NProvider.getTranslation("date-hour", getLocale());
		dateLabel = i18NProvider.getTranslation("departure-date", getLocale());
		hourLabel = i18NProvider.getTranslation("departure-hour", getLocale());
		statusLabel = i18NProvider.getTranslation("flight-status", getLocale());
		//seatsLabel = i18NProvider.getTranslation("seats-number", getLocale());		
		//showHideMenuLabel = i18NProvider.getTranslation("sh-menu-title", getLocale());
		
		
	}
	
	private boolean matchesTerm(String value, String searchTerm) {
		if(value == null)
			return false;
					
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
	
	private void rescheduleFlight() {
		Vuelo selectedVuelo = grid.asSingleSelect().getValue();
		
		if(selectedVuelo  != null) {
			
			editFlight(selectedVuelo);
		}
	}
	
	private void saveFlight(FlightForm.SaveEvent event) {
		
		FlightForm vueloForm = event.getSource();
		
		String successMessage = i18NProvider.getTranslation("save-success", getLocale());
		String updateMessage = i18NProvider.getTranslation("update-success", getLocale());
		Notification notification;
		
		try {
			
			if(vueloForm.getNroVueloValue() == null) {
				
				service.crearVuelo(event.getSource());
				
				notification = Notification
						.show(successMessage);
				notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				notification.setPosition(Notification.Position.TOP_END);
				notification.setDuration(5000);
				
			} else {
				
				service.reprogramarVuelo(vueloForm);
				
				notification = Notification
						.show(updateMessage);
				notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
				notification.setPosition(Notification.Position.TOP_END);
				notification.setDuration(5000);
				
			}					
			
		} catch(VueloException e) {
			notification = Notification
					.show(e.getMessage());
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
			notification.setPosition(Notification.Position.TOP_END);
			notification.setDuration(5000);
			
		} finally {
			
			closeEditor();
			updateList();
			addCustomFilters();
			
			
		}		
				
		
	}
	
	private void setColumnToggleMenu(Button menuButton) {
		
		ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(menuButton);
		columnToggleContextMenu.addColumnToggleItem(airlineLabel, grid.getColumnByKey("airlineColumn"), true);
		columnToggleContextMenu.addColumnToggleItem(dateLabel, grid.getColumnByKey("dateColumn"), true);
		columnToggleContextMenu.addColumnToggleItem(hourLabel, grid.getColumnByKey("hourColumn"), true);
		columnToggleContextMenu.addColumnToggleItem(arrivalLabel, grid.getColumnByKey("arrivalColumn"), true);
		columnToggleContextMenu.addColumnToggleItem(priceLabel, grid.getColumnByKey("priceColumn"), true);
		columnToggleContextMenu.addColumnToggleItem(statusLabel, grid.getColumnByKey("statusColumn"),true);
		columnToggleContextMenu.addColumnToggleItem(typeLabel, grid.getColumnByKey("typeColumn"),false);
	}

	private void updateList() {
		grid.setItems(vueloDao.findAll());				
	}
	
	private static class ColumnToggleContextMenu extends ContextMenu {

		private static final long serialVersionUID = 5951777341923056218L;
		
		public ColumnToggleContextMenu (Component target) {
			
			super(target);
			setOpenOnClick(true);
			
		}
		
		void addColumnToggleItem(String label, Grid.Column<Vuelo> column, boolean setVisibility) {
			
			MenuItem menuItem = this.addItem(label, e-> {
				column.setVisible(e.getSource().isChecked());
				column.setAutoWidth(true);
			});
			
			menuItem.setCheckable(true);
			menuItem.setChecked(setVisibility);
			//column.getParent().get().recalculateColumnWidths();
		}		
		
	}

}
