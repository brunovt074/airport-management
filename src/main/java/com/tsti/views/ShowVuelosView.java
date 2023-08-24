package com.tsti.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.tsti.entidades.Vuelo;
import com.vaadin.flow.component.grid.Grid;


@Route(value="/vuelos")
@PageTitle("Listado de Vuelos")
public class VuelosListView extends VerticalLayout{

	private static final long serialVersionUID = 1605332884677855208L;
	
	Grid<Vuelo> grid = new Grid<>(Vuelo.class);
	TextField filterText = new TextField();
	public VuelosListView(Grid<Vuelo> grid, TextField filterText) {
		super();
		this.grid = grid;
		this.filterText = filterText;
		
		addClassName("vuelos-list");
		setSizeFull();		
	}
	
	
	
}
