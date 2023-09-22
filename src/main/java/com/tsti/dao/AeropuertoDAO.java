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
	
	@Query(value = "SELECT * FROM aeropuertos WHERE country = 'AR' AND city <> '' ORDER BY RAND() LIMIT 1", nativeQuery= true )
	public Aeropuerto findAeropuertoArgentinoAleatorio();
	@Query(value = "SELECT * FROM aeropuertos WHERE country <> 'AR' AND city <> '' ORDER BY RAND() LIMIT 1", nativeQuery= true )
	public  Aeropuerto findAeropuertoExtranjeroAleatorio();
	@Query(value = "SELECT * FROM aeropuertos WHERE country = 'AR' AND city <> ''", nativeQuery= true )
	public List<Aeropuerto>findAllAeropuertosArgentinos();
	@Query(value = "SELECT * FROM aeropuertos WHERE country <> 'AR' AND city <> ''", nativeQuery= true )
	public List<Aeropuerto>findAllAeropuertosExtranjeros();
	@Query(value = "SELECT * FROM aeropuertos WHERE city <> ''", nativeQuery= true )
	public List<Aeropuerto>findAll();
}
