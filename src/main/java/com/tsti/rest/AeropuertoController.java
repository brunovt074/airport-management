package com.tsti.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsti.servicios.AeropuertoServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/aeropuertos")
public class AeropuertoController {
  @Autowired
  private AeropuertoServiceImpl airportService;  
	
	@PostMapping()
	public ResponseEntity<?> cargarAeropuertos(HttpServletRequest request) {
		
		try {
			
			airportService.loadAirportsFromJsonFile("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\airports.json");
			
			return ResponseEntity.ok("Base de datos cargada correctamente");
		
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
		}
	}
}
