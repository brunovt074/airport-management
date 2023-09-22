package com.tsti.servicios;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsti.dao.AeropuertoDAO;
import com.tsti.entidades.Aeropuerto;
import com.tsti.excepcion.SistemaGestionComercialAeropuertoException;

@Service
public interface IAeropuertoService {
	
	public Aeropuerto getAeropuertoLocal() throws SistemaGestionComercialAeropuertoException;
	public Aeropuerto getAeropuertoArgentinoAleatorio();
	public Aeropuerto getAeropuertoArgentino(String icao);	
	public Aeropuerto getAeropuertoExtranjeroAleatorio();
	public Aeropuerto getAeropuertoExtranjero(String icao);
	public List<Aeropuerto>getAllAeropuertos();
	public List<Aeropuerto>getAllAeropuertosArgentinos();
	public List<Aeropuerto>getAllAeropuertosExtranjeros();
	public void loadAirportsFromJsonFile();
	public void loadAirportsFromJsonFile(String jsonFilePath);
	public void save(Aeropuerto aeropuerto);
	
}
