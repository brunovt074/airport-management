package com.tsti.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tsti.entidades.Aeropuerto;
/**
 * @author Bruno
 *
 */

@Repository
public interface AeropuertoDAO extends JpaRepository<Aeropuerto, String>{

	public boolean existsByIcao(String icao);
	public boolean existsByName(String name);
	public boolean existsByCity(String city);
	public boolean existsByState(String state);
	public boolean existsByCountry(String country);
	
	public boolean findByIcao(String icao);
	public boolean findByName(String name);
	public boolean findByCity(String city);
	public boolean findByState(String state);
	public boolean findByCountry(String country);
	
}
