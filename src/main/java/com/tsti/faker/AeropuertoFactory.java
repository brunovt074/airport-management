//package com.tsti.faker;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;

//import com.tsti.dao.AeropuertoDAO;
//import com.tsti.entidades.Aeropuerto;
//
//
//import net.datafaker.Faker;
//
//@Component
//public class AeropuertoFactory {
//
//	private final Faker faker;
//	private final AeropuertoDAO dao;
//	private Aeropuerto aeropuerto;
//	
//	@Autowired
//	public AeropuertoFactory(Faker faker, AeropuertoDAO aeropuertoDAO) {
//		super();
//		this.faker = faker;
//		this.dao = aeropuertoDAO;
//	}
//	
////	public Aeropuerto getAeropuertoLocal(){
////		
////		if(dao.existsByIcao("SATM")) {
////			
////			aeropuerto = dao.findByIcao("SATM");
////		} else {
////			
////			aeropuerto = new Aeropuerto("SATM",
////										"MDX",
////										"Mercedes Airport",
////										"Mercedes",
////										"Corrientes",
////										"AR",
////										358,
////										-29.2213993073,
////										-58.0877990723,
////										"America/Argentina/Cordoba");
////		}
////				
////		return aeropuerto;
////	}
////	
////	public Aeropuerto getAeropuertoArgentinoAleatorio() {
////		
////		return aeropuerto = dao.findAeropuertoArgentinoAleatorio();
////		
////	}
////	
////	public Aeropuerto getAeropuertoArgentino(String icao) {
////			
////		return aeropuerto = dao.findByIcao(icao);	
////		
////	}
////		
////	public Aeropuerto getAeropuertoExtranjeroAleatorio() {
////		
////		return aeropuerto = dao.findAeropuertoExtranjeroAleatorio();	
////					
////	}
////	
////	public Aeropuerto getAeropuertoExtranjero(String icao) {
////		
////		return aeropuerto = dao.findByIcao(icao);	
////					
////	}	
//	
//}
