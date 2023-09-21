package com.tsti.servicios;

import java.util.Map;
import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsti.dao.AeropuertoDAO;
import com.tsti.entidades.Aeropuerto;

@Service
public class AeropuertoServiceImpl implements IAeropuertoService{

	private final AeropuertoDAO aeropuertoDao;
	
	@Autowired 
    public AeropuertoServiceImpl(AeropuertoDAO aeropuertoDao) {
        this.aeropuertoDao = aeropuertoDao;
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
            aeropuertoDao.saveAll(airportsMap.values());

            System.out.println("Aeropuertos cargados en la base de datos correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
