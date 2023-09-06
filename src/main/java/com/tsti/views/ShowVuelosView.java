package com.tsti.views;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.text.Normalizer;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.web.servlet.tags.EditorAwareTag;

import com.tsti.dao.VueloDAO;
import com.tsti.entidades.Ciudad;
import com.tsti.entidades.Vuelo;
import com.tsti.entidades.Vuelo.EstadoVuelo;
import com.tsti.i18n.AppI18NProvider;
import com.tsti.presentacion.EditarVueloForm;
import com.tsti.servicios.VueloServiceImpl;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Validator;


@Route(value="/flights-index", layout = MainLayout.class)
@PageTitle("Flights")
public class ShowVuelosView extends VerticalLayout{

	private static final long serialVersionUID = 1605332884677855208L;
	
	private final AppI18NProvider i18NProvider;
	private VueloDAO vueloDAO;
	private VueloServiceImpl vueloService;
	private EditarVueloForm editarVueloForm;
		
	Grid<Vuelo> grid = new Grid<>(Vuelo.class,false);
	Editor<Vuelo> editor = grid.getEditor();
	
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
		LocalDate nowDate = LocalDate.now();				
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
		String editLabel = i18NProvider.getTranslation("edit", getLocale());
		
		grid.addClassName("show-vuelos-view");		
    	grid.setSizeFull();
    	
    	grid.setColumns();
    	grid.setColumnReorderingAllowed(true);
    	
    	//Columns
    	Grid.Column<Vuelo> flightIdColumn = grid.addColumn(Vuelo::getNroVuelo).setHeader(flightIdLabel)
    							.setFrozen(true)
    							.setFooter(createFooterText(vuelos))
    							.setSortable(true);
    	
    	Grid.Column<Vuelo> airlineColumn = grid 
    			.addColumn(Vuelo::getAerolinea).setHeader(airlineLabel)
    							.setSortable(true);
    	
//      Fecha config		
		Grid.Column<Vuelo> dateColumn = grid.addComponentColumn(vuelo -> { 
			DatePicker datePicker = new DatePicker(); 
			datePicker.setValue(vuelo.getFechaPartida());			 
			datePicker.setReadOnly(true);	
			
		return datePicker;
		
		}).setHeader(dateLabel)
		  .setSortable(true)
		  .setComparator((vuelo1, vuelo2) ->
		vuelo1.getFechaPartida().compareTo(vuelo2.getFechaPartida()));
		
    	//Hora config
//        grid.addColumn(Vuelo::getHoraPartida).setHeader(hourLabel)
//        						.setSortable(true);
    	Grid.Column<Vuelo> hourColumn = grid.addComponentColumn(vuelo -> {
    		
    		TimePicker timePicker = new TimePicker();
    		timePicker.setValue(vuelo.getHoraPartida());    		
    		timePicker.setReadOnly(true);
    		
    		return timePicker;
    	}).setHeader(hourLabel);
//		.setSortable(true);
        
    	Grid.Column<Vuelo> departureColumn = grid
        	.addColumn(vuelo -> vuelo.getOrigen().getNombreCiudad()
        	+ ", " + vuelo.getOrigen().getPais()).setHeader(departureLabel)
        						.setSortable(true)
        						.setResizable(true);        
        Grid.Column<Vuelo> arrivalColumn = grid.addColumn(vuelo -> vuelo.getDestino().getNombreCiudad()
        	+ ", " + vuelo.getOrigen().getPais()).setHeader(arrivalLabel)
        						.setSortable(true);
        Grid.Column<Vuelo> typeColumn = grid 
        		.addColumn(Vuelo::getTipoVuelo).setHeader(typeLabel)
        						.setSortable(true)
        						.setResizable(true);
        Grid.Column<Vuelo> statusColumn = grid
        		.addColumn(Vuelo::getEstadoVuelo).setHeader(statusLabel)
        						.setSortable(true)
        						.setResizable(true);       
        Grid.Column<Vuelo> aircraftColumn = grid
        		.addColumn(Vuelo::getAvion).setHeader(aircraftLabel)
        						.setSortable(true)
        						.setResizable(true);
        //Edit Column
        Grid.Column<Vuelo> editColumn = grid.addComponentColumn(vuelo -> {
        	        	        	
        	Button editButton = new Button("Edit");
            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(vuelo);
            });
            return editButton;
        }).setWidth("150px").setFlexGrow(0);
      
	    //Binding y edicion
        Binder<Vuelo> binder = new Binder<>(Vuelo.class);		
		editor.setBinder(binder);
	    editor.setBuffered(true);
        
//        DatePicker.DatePickerI18n multiFormatI18n = new DatePicker.DatePickerI18n();
//        multiFormatI18n.setDateFormats("yyyy-MM-dd", "MM/dd/yyyy",
//                "dd/MM/yyyy");
//        
//        DatePicker editDate = new DatePicker();
//        editDate.setI18n(multiFormatI18n);
//	    editDate.setMin(nowDate);
//	    editDate.setMax(nowDate.plusDays(180));
//	    editDate.setInitialPosition(nowDate);
	    
//	    TimePicker editTime = new TimePicker();
//	    editTime.setStep(Duration.ofMinutes(1));
	    
	    TextField editAirline = new TextField();        
	        
	    binder.forField(editAirline)
	    .withValidator(airline -> airline.length() >= 3, "Nombre muy corto")
        .bind(Vuelo::getAerolinea, Vuelo::setAerolinea);
	    airlineColumn.setEditorComponent(editAirline);
	    //Button saveButton = new Button("Save", e -> editor.save());
//	    binder.forField(editDate)
//        .bind(Vuelo::getFechaPartida, Vuelo::setFechaPartida);
//	    
//	    dateColumn.setEditorComponent(editDate);
//	    
//	    binder.forField(editTime)
//        .bind(Vuelo::getHoraPartida, Vuelo::setHoraPartida);
//	    
//	    hourColumn.setEditorComponent(editTime);    
	    
	    Button saveButton = new Button("Save", e -> {
	    	try {
	    		//editarVueloForm = new EditarVueloForm(binder.getBean());
	    		//System.out.println("Binder para EditarVueloForm is null");
    		System.out.println("Binder Bean: " + binder.getBean().getAerolinea().toString()); 		
		    		    	
	    	}catch(Exception ex) {
	    		System.out.println("Binder para Aerolinea is null");
	    	}    	
	    	if(editor.getItem() != null) {
	    		System.out.println("Editor antes de save(): "+ editor.getItem().getAerolinea());
	    		
	    	}
	    	if(editor.getItem() == null) {
	    		System.out.println("Editor is null");
	    	
	    	}
	    	
	    	editor.save();    	
	    	
	    	if(editor.getItem() != null) {
	    	 	System.out.println("Editor despues de save(): "+ editor.getItem().getAerolinea());
	    		
	    	}
	    	if(editor.getItem() == null) {
	    		System.out.println("Editor despues de save() es nulo ");
	    	
	    	}	    	
	    	
	    	if(binder.getBean() == null) {
	    		System.out.println("Binder despues de save() is null");
	    	
	    	}
	    	else {
	    		System.out.println("Editor Item: " + editor.getItem().toString());
	    		System.out.println("Binder Bean: " + binder.getBean().toString());
	    	}
	    	
	    });
	    Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
	    						e -> editor.cancel());
	    cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
	    HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton);
	    actions.setPadding(false);
	    editColumn.setEditorComponent(actions);	    
        
        //set hidden columns 
        departureColumn.setVisible(false);
        typeColumn.setVisible(false);
        aircraftColumn.setVisible(false);
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
		
		columnToggleContextMenu.addColumnToggleItem(airlineLabel, airlineColumn, true);
		columnToggleContextMenu.addColumnToggleItem(departureLabel, departureColumn, false);
		columnToggleContextMenu.addColumnToggleItem(arrivalLabel, arrivalColumn, true);
		columnToggleContextMenu.addColumnToggleItem(typeLabel, typeColumn,false);
		columnToggleContextMenu.addColumnToggleItem(statusLabel, statusColumn,true);
		columnToggleContextMenu.addColumnToggleItem(aircraftLabel, aircraftColumn, false);
		//columnToggleContextMenu.addColumnToggleItem(seatsLabel, seatsColumn);
                
		HorizontalLayout toolbar = new HorizontalLayout(searchField, menuButton);        
		 
		grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		
		add(toolbar, grid);
        
        
		
	}
	
	private static class ColumnToggleContextMenu extends ContextMenu {
				
		private static final long serialVersionUID = -5617802348824382034L;

		public ColumnToggleContextMenu(Component target) {
			
			super(target);
			setOpenOnClick(true);
			
			
		}
		
		void addColumnToggleItem(String label, Grid.Column<Vuelo> column, boolean setVisibility) {
			
			MenuItem menuItem = this.addItem(label, e -> {
					column.setVisible(e.getSource().isChecked());
					column.setAutoWidth(true);
			});
			
			menuItem.setCheckable(true);
			menuItem.setChecked(setVisibility);	
						
		}		
	}
	
	private String createFooterText(List<Vuelo> vuelos){
		String totalFlightsLabel = i18NProvider.getTranslation("total-flights", getLocale());
		
		long flightCount = vuelos.stream().count();
		
		return String.format(totalFlightsLabel + "%s", flightCount);
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
	
	private void editFlight(Vuelo vuelo, DatePicker datePicker) {
		
		if(vuelo == null) {
			
			//closeEditor();
			
		} else {
			datePicker.setReadOnly(false);			
		}	
		
	}
	
	private static VerticalLayout createDialogLayout(Vuelo vuelo) {
		
		DatePicker newDatePicker = new DatePicker(vuelo.getFechaPartida());
		newDatePicker.setInitialPosition(LocalDate.now());
		newDatePicker.setMax(null);
		
		TimePicker newTimePicker = new TimePicker(vuelo.getHoraPartida());
		
		VerticalLayout dialogLayout = new VerticalLayout(newDatePicker, newTimePicker);
		dialogLayout.setPadding(false);
		dialogLayout.setSpacing(false);
		dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
		dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");
		
		return dialogLayout;
	}
	
	private static Button createSaveButton(Dialog dialog) {
		
		Button saveButton = new Button("Update", e -> dialog.close());
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		return saveButton;
	}

}
