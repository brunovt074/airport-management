package com.tsti.dao;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.tsti.entidades.Vuelo;
import com.tsti.entidades.Vuelo.TipoVuelo;

import com.tsti.entidades.Vuelo.EstadoVuelo;


/**
 * @author Bruno
 *
 */

@Repository
public interface VueloDAO extends JpaRepository<Vuelo, Long> {
    
	public Optional<Vuelo> findById(Long id);
	
	//ESTE ES EL METODO UTILIZADO EN LA API REST
	@Query("SELECT v FROM Vuelo v JOIN v.destino c "
			+ "WHERE c.city = :destino "
			+ "AND v.fechaPartida = :fechaPartida")
	public List<Vuelo> findByDestinoAndFechaPartida(
			@Param("destino") String destino, 
			@Param("fechaPartida") LocalDate fechaPartida);
	
	//ESTE ES EL METODO UTILIZADO EN LA API REST
	@Query("SELECT v FROM Vuelo v "
			+ "JOIN v.destino c "
			+ "WHERE c.city = :destino "
			+ "AND v.fechaPartida = :fechaPartida "
			+ "AND v.horaPartida = :horaPartida")
	public List<Vuelo> findByDestinoAndFechaPartidaAndHoraPartida(
			@Param("destino") String destino, 
			@Param("fechaPartida") LocalDate fechaPartida, 
			@Param("horaPartida") LocalTime horaPartida);


	//OPCIONALES
	//@Query("SELECT v FROM Vuelo v JOIN v.destino c WHERE c.city = :destino")
	public List<Vuelo> findByDestino(/*@Param("destino") */String destino);
	
	//@Query("SELECT v FROM Vuelo v WHERE v.fechaPartida = :fechaPartida")
	public List<Vuelo> findByFechaPartida(/*@Param("fechaPartida") */LocalDate fechaPartida);
		    
    public List<Vuelo> findByAerolinea(String aerolinea);
    
	public List<Vuelo>findByTipoVuelo(TipoVuelo tipoVuelo);
	
	public List<Vuelo> findAllByEstadoVuelo(EstadoVuelo estadoVuelo);
	
	@Query("SELECT v "
			+ "FROM Vuelo v "
			+ "JOIN v.destino c "
			+ "WHERE LOWER(v.aerolinea) "
			+ "LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
			+ "OR LOWER(c.city) "
			+ "LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
			+ "OR LOWER(c.country) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	public List<Vuelo> searchFlights(@Param("searchTerm") String searchTerm);	
	
	public boolean existsById(Long id);
    
}
