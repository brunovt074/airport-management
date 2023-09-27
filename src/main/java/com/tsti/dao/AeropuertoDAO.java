package com.tsti.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
	
	public Optional<Aeropuerto> findById(Long id);
	public Aeropuerto findByIcao(String icao);
	public Aeropuerto findByName(String name);
	public Aeropuerto findByCity(String city);
	public Aeropuerto findByState(String state);
	public Aeropuerto findByCountry(String country);
	
	@Query("SELECT a FROM Aeropuerto a WHERE a.country = 'AR' AND a.city <> '' ORDER BY FUNCTION('RANDOM') LIMIT 1")
	public Aeropuerto findAeropuertoArgentinoAleatorio();

	@Query("SELECT a FROM Aeropuerto a WHERE a.country <> 'AR' AND a.city <> '' ORDER BY FUNCTION('RANDOM') LIMIT 1")
	public Aeropuerto findAeropuertoExtranjeroAleatorio();

	@Query("SELECT a FROM Aeropuerto a WHERE a.country = 'AR' AND a.city <> ''")
	public List<Aeropuerto> findAllAeropuertosArgentinos();

	@Query("SELECT a FROM Aeropuerto a WHERE a.country <> 'AR' AND a.city <> ''")
	public List<Aeropuerto> findAllAeropuertosExtranjeros();

	@Query("SELECT a FROM Aeropuerto a WHERE a.city <> ''")
	public List<Aeropuerto> findAll();

}
