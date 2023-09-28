package com.tsti.servicios;

import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsti.dao.AeropuertoDAO;
import com.tsti.entidades.Aeropuerto;
import com.tsti.excepcion.SistemaGestionComercialAeropuertoException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.ClassPathResource;

@Service
public class AeropuertoServiceImpl implements IAeropuertoService{
	private final AeropuertoDAO dao;
	private final ResourceLoader resourceLoader;
	private Aeropuerto aeropuerto;
	
	@Autowired 
    public AeropuertoServiceImpl(AeropuertoDAO aeropuertoDao, ResourceLoader resourceLoader) {
        this.dao = aeropuertoDao;
		this.resourceLoader = resourceLoader;
    }
	
	public Aeropuerto getAeropuertoLocal() throws SistemaGestionComercialAeropuertoException{
		 
		try {
			if(dao.existsByIcao("SATM")) {
				
				aeropuerto = dao.findByIcao("SATM");
			
			} else {
				
				aeropuerto = new Aeropuerto("SATM",
											"MDX",
											"Mercedes Airport",
											"Mercedes",
											"Corrientes",
											"AR",
											358,
											-29.2213993073,
											-58.0877990723,
											"America/Argentina/Cordoba");
			}
		
		} catch(Exception e) {
			
			throw new SistemaGestionComercialAeropuertoException ("No se pudo obtener ciudad de origen ", HttpStatus.BAD_REQUEST.value());
		}
		
				
		return aeropuerto;
	}
	
	public Aeropuerto getAeropuertoArgentinoAleatorio() {
		
		return aeropuerto = dao.findAeropuertoArgentinoAleatorio();
		
	}
	
	public Aeropuerto getAeropuertoArgentino(String icao) {
			
		return aeropuerto = dao.findByIcao(icao);	
		
	}
		
	public Aeropuerto getAeropuertoExtranjeroAleatorio() {
		
		return aeropuerto = dao.findAeropuertoExtranjeroAleatorio();	
					
	}
	
	public Aeropuerto getAeropuertoExtranjero(String icao) {
		
		return aeropuerto = dao.findByIcao(icao);	
					
	}
	public List<Aeropuerto>getAllAeropuertos() {
		
		List<Aeropuerto> aeropuertos = dao.findAll();
		
		return aeropuertos; 	
					
	}
	public List<Aeropuerto>getAllAeropuertosArgentinos() {
		
		List<Aeropuerto> aeropuertos = dao.findAllAeropuertosExtranjeros();
		
		return aeropuertos; 	
					
	}
	public List<Aeropuerto>getAllAeropuertosExtranjeros() {
		
		List<Aeropuerto> aeropuertos = dao.findAllAeropuertosExtranjeros();
		
		return aeropuertos; 	
					
	}

	public void loadAirportsFromJsonFile() {
		ObjectMapper objectMapper = new ObjectMapper();		
		 
		try {
			Resource resource = resourceLoader.getResource("classpath:data/airports.json");
			File jsonFile = resource.getFile();
			
			Map<String, Aeropuerto> airportsMap = objectMapper.readValue(
                    jsonFile,
                    objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Aeropuerto.class)
                    
            );
			
			dao.saveAll(airportsMap.values());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadAirportsFromJsonFile(String jsonFilePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            // Lee el archivo JSON y conviértelo en un mapa de aeropuertos
            Map<String, Aeropuerto> airportsMap = objectMapper.readValue(
                    new File(jsonFilePath),
                    objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Aeropuerto.class)
                        
            );

            // Guarda los aeropuertos en la base de datos
            dao.saveAll(airportsMap.values());

            System.out.println("Aeropuertos cargados en la base de datos correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void save(Aeropuerto aeropuerto) {
		dao.save(aeropuerto);
	}
	
}
