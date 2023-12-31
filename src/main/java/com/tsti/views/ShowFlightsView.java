package com.tsti.views;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import com.tsti.dao.AeropuertoDAO;
import com.tsti.dao.VueloDAO;
import com.tsti.entidades.Vuelo;
import com.tsti.entidades.Vuelo.EstadoVuelo;
import com.tsti.entidades.Vuelo.TipoVuelo;
import com.tsti.excepcion.SistemaGestionComercialAeropuertoException;
import com.tsti.excepcion.VueloException;
import com.tsti.i18n.AppI18NProvider;
import com.tsti.servicios.VueloServiceImpl;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

/**
 *Flights Index Layout 
 **/
@PermitAll
@Route(value="", layout = MainLayout.class)
@PageTitle("Flights")
public class ShowFlightsView extends VerticalLayout{
	
	private static final long serialVersionUID = -7236223778352535392L;
	private static final AppI18NProvider i18NProvider = new AppI18NProvider();
	private final VueloServiceImpl vueloService;
	private final VueloDAO vueloDao;
	private final AeropuertoDAO aeropuertoDao;	
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
	private String arrivalLabel;
	private String priceLabel;
	private String typeLabel;	
	private String dateLabel;
	private String hourLabel;
	private String statusLabel;
	private String nullFlightErrorMessage;	
	
	public ShowFlightsView(AppI18NProvider i18NProvider, VueloDAO vueloDao, VueloServiceImpl vueloService, AeropuertoDAO aeropuertoDao) {
	    
		this.vueloDao = vueloDao;	    
	    this.vueloService = vueloService;
	    this.flights = new ArrayList<>(vueloDao.findAll());
	    this.dataView = grid.setItems(flights);
		this.aeropuertoDao = aeropuertoDao;	    
	    
		addClassName("show-flights-view");
	    setSizeFull();
	    
	    initializeLabels();
	    configureGrid();
	    configureForm();    
	    
	    H2 departuresTitle = new H2(i18NProvider.getTranslation("departures", getLocale()));	    
	    departuresTitle.addClassName("departures-title");
	    
	    add(departuresTitle,
	    	getToolbar(),
	    	getContent());
	    
	    closeEditor();	    
	    
	}
	
	private void addCustomFilters() {
		
		String searchPlaceholder = i18NProvider.getTranslation("search", getLocale());
        
        searchField.setWidth("100%");
        searchField.setPlaceholder(searchPlaceholder);
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> dataView.refreshAll());
        searchField.addClassName("flights-search-field");
        
        dataView.addFilter(vuelo -> {
        	String searchTerm = searchField.getValue().trim();
        	searchTerm = searchTerm.replace("","");
        	
        	if(searchTerm.isEmpty())
        		return true;
        	
        	boolean matchesDestino = matchesTerm(vuelo.getDestino().getCity(), searchTerm);
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
	
	private void cancelFlight() {
		Vuelo selectedFlight = grid.asSingleSelect().getValue();
				
		if(selectedFlight  != null) {
			
			if(selectedFlight.getEstadoVuelo().equals(EstadoVuelo.CANCELADO)  ) {
				String alreadyCanceled =  i18NProvider.getTranslation("already-canceled", getLocale());
				String alreadyCanceledMessage = String.format(alreadyCanceled, selectedFlight.getNroVuelo()); 
				
				Notification notification = Notification.show(alreadyCanceledMessage);
				notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
				notification.setPosition(Notification.Position.TOP_END);
				notification.setDuration(5000);
				
			} else {
				
				cancelFlightDialog(selectedFlight);
			}		
			
		} else {
			
			Notification notification = Notification.show(nullFlightErrorMessage);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
			notification.setPosition(Notification.Position.TOP_END);
			notification.setDuration(5000);			
		}		
		
	}
	
	private void cancelFlightDialog(Vuelo vuelo) {
		String title = i18NProvider.getTranslation("cancel-dialog-title", getLocale());
		String confirmation = i18NProvider.getTranslation("cancel-dialog-confirmation", getLocale());
		String confirmationMessage = String.format(confirmation, vuelo.getNroVuelo()) ;//
		String cancel = i18NProvider.getTranslation("cancel-flight", getLocale());
		String close = i18NProvider.getTranslation("close", getLocale());
		String successfulCancelationMessage = i18NProvider.getTranslation("canceled-success", getLocale());
		String canceledMessageFormatted = String.format(successfulCancelationMessage, vuelo.getNroVuelo());
		
		Dialog dialog = new Dialog();		
				
        dialog.setHeaderTitle(title);
        dialog.add(confirmationMessage);

        Button cancelFlightButton = new Button(cancel, (e) -> 
	        	{
	        		try{
	        			vueloService.cancelarVuelo(vuelo.getNroVuelo());
	        			Notification notification = Notification
	        					.show(canceledMessageFormatted);
	        			notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
	        			notification.setPosition(Notification.Position.TOP_END);
	        			notification.setDuration(5000);
	        		
	        		}catch(VueloException ex) {
	        			
	        			ex.setMensaje(i18NProvider.getTranslation("delete-error", getLocale()));
	        			
	        			Notification notification = Notification.show(ex.getMessage());
	        			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
	        			notification.setPosition(Notification.Position.TOP_END);
	        			notification.setDuration(5000);
	        		}
	        		finally {
	        			closeEditor();
	        			updateList();
	        			addCustomFilters();
	        			dialog.close();
	        		}
	        	}
        	);
        
        cancelFlightButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        cancelFlightButton.getStyle().set("margin-right", "auto");
        dialog.getFooter().add(cancelFlightButton);

        Button closeButton = new Button(close, (e) -> dialog.close());
        
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getFooter().add(closeButton);
        
        dialog.open();        
	}
	
	private void closeEditor() {
		
		form.setFlight(null);
		form.setVisible(false);
		removeClassName("editing");		
	}
	
	private void configureForm() {

		form = new FlightForm(i18NProvider, vueloService, aeropuertoDao);
		form.setWidth("25rem");
		form.addSaveListener(this::saveFlight);    	
    	form.addCloseListener(e -> closeEditor());
    			
	}

	private void configureGrid() {
		
		grid.addClassName("flights-grid");		
    	grid.setSizeFull();
    	grid.setMaxHeight("100%");
    	grid.setColumns();
    	grid.setColumnReorderingAllowed(true);
    	
    	initializeDragAndDropOnRows(grid);
    	
    	//NroVuelo
    	grid.addColumn(Vuelo::getNroVuelo).setHeader(flightIdLabel)
    							.setFrozen(true)    							
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
    	        
        //Arrival
    	grid.addColumn(vuelo -> vuelo.getDestino().getCity()
        	+ ", " + vuelo.getDestino().getCountry()).setHeader(arrivalLabel)
        						.setSortable(true)
    							.setResizable(true)
    							.setKey("arrivalColumn");
    	//Price
    	grid.addColumn(vuelo -> {
    		if(vuelo.getTipoVuelo().equals(TipoVuelo.NACIONAL)) {
    			return "AR$ " + vuelo.getPrecioNeto();
    		}else {
    			return "US$ " + vuelo.getPrecioNeto();
    			
    		}
    	})
//    	grid.addColumn(Vuelo::getPrecioNeto)
    	
    	.setHeader(priceLabel)
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
    	
    	
    	grid.getColumns().forEach(column -> column.setAutoWidth(true));
    	grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES); 
    		
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
			form.setVisible(true);
			addClassName("editing");
			
		} else {
			if(!vuelo.getEstadoVuelo().equals(EstadoVuelo.CANCELADO)) {
				form.setFlight(vuelo);
				form.aerolinea.setReadOnly(true);
				form.avion.setReadOnly(true);
				form.destino.setReadOnly(true);
				form.precioNeto.setReadOnly(false);			
				form.setVisible(true);
				addClassName("editing");
			
			} else {
				String alreadyCanceled =  i18NProvider.getTranslation("already-canceled", getLocale());
				String alreadyCanceledMessage = String.format(alreadyCanceled, vuelo.getNroVuelo()); 
				
				Notification notification = Notification.show(alreadyCanceledMessage);
				notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
				notification.setPosition(Notification.Position.TOP_END);
				notification.setDuration(5000);
			}
			
			
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
		String newFlightLabel = i18NProvider.getTranslation("new-flight", getLocale());
		String rescheduleFlightLabel = i18NProvider.getTranslation("edit-flight", getLocale());
		String cancelFlightLabel = i18NProvider.getTranslation("cancel-flight", getLocale());
		String showHideMenuLabel = i18NProvider.getTranslation("sh-menu-title", getLocale());
				
		//Create buttons
		//Add Contact Button		
		Button newFlightButton = new Button(newFlightLabel);
		newFlightButton.addClickListener(e -> newFlight());		
		newFlightButton.setIcon(new Icon(VaadinIcon.FLIGHT_TAKEOFF));
		//nullFlightErrorMessage
		newFlightButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		newFlightButton.addClassName("options-nav-button");
		//Reschedule flight button		
		Button rescheduleFlightButton = new Button(rescheduleFlightLabel);
		rescheduleFlightButton.addClickListener(e -> rescheduleFlight());
		rescheduleFlightButton.setIcon(new Icon(VaadinIcon.CALENDAR_CLOCK));
		rescheduleFlightButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		rescheduleFlightButton.addClassName("options-nav-button");
		
		Button cancelFlightButton = new Button(cancelFlightLabel);
		cancelFlightButton.addClickListener(e -> cancelFlight());		
		cancelFlightButton.setIcon(new Icon("lumo","cross"));
		cancelFlightButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancelFlightButton.addClassName("options-nav-button");
		
		//Add Show Hide Toggle Menu		
		Button menuButton = new Button (showHideMenuLabel);
		menuButton.setIcon(new Icon("lumo", "menu"));
		menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SUCCESS);		
				
		//Create show/hide menu
		setColumnToggleMenu(menuButton);
		
		
		//optionsBar.addItem(menuButton);
		HorizontalLayout optionsNav =  new HorizontalLayout(newFlightButton,
				rescheduleFlightButton,
				cancelFlightButton,
				menuButton);
		
		optionsNav.addClassName("options-nav");
		//Layout		
		HorizontalLayout toolbar = new HorizontalLayout(searchField, optionsNav);//, newFlightButton,rescheduleFlightButton, menuButton);
		
		toolbar.addClassName("toolbar");
		toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);		
		
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
		arrivalLabel = i18NProvider.getTranslation("arrival", getLocale());
		priceLabel = i18NProvider.getTranslation("price", getLocale());
		typeLabel = i18NProvider.getTranslation("type", getLocale());
		dateLabel = i18NProvider.getTranslation("departure-date", getLocale());
		hourLabel = i18NProvider.getTranslation("departure-hour", getLocale());
		statusLabel = i18NProvider.getTranslation("flight-status", getLocale());
		nullFlightErrorMessage = i18NProvider.getTranslation("null-flight", getLocale());		
		
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
		} else {
			Notification notification = Notification.show(nullFlightErrorMessage);
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
			notification.setPosition(Notification.Position.TOP_END);
			notification.setDuration(5000);
			
		}
	}
	
	private void saveFlight(FlightForm.SaveEvent event) {
		
		FlightForm vueloForm = event.getSource();
		String success = i18NProvider.getTranslation("save-success", getLocale());
		String successFormattedMessage = String.format(success, event.getVuelo().getNroVuelo());
		String update = i18NProvider.getTranslation("update-success", getLocale());
		String updateFormattedMessage = String.format(update, event.getVuelo().getNroVuelo());
		Notification notification;
		
		try {
			
			if(vueloForm.getNroVueloValue() == null) {
				
				try {
					vueloService.crearVuelo(event.getSource());
					notification = Notification
							.show(successFormattedMessage);
					notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
					notification.setPosition(Notification.Position.TOP_END);
					notification.setDuration(5000);
				
				} catch (SistemaGestionComercialAeropuertoException e) {
					e.getMensaje();					
				}
				
				
				
			} else {
				
				vueloService.reprogramarVuelo(vueloForm);
				
				notification = Notification
						.show(updateFormattedMessage);
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
		}		
		
	}
	
}
