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
	
	//ESTE ES EL METODO UTILIZADO EN LA APP
	@Query(value = "SELECT v.* FROM vuelos v JOIN ciudades c "
    		+ "ON V.destino_id = c.id WHERE c.nombre_ciudad =:destino AND v.fecha_partida=:fecha_partida", nativeQuery = true )
	public List<Vuelo> findByDestinoAndFechaPartida(@Param("destino") String destino, @Param("fecha_partida") LocalDate fechaPartida);
	
	//ESTE ES EL METODO UTILIZADO EN LA APP
		@Query(value = "SELECT v.* FROM vuelos v JOIN ciudades c "
	    		+ "ON V.destino_id = c.id WHERE c.nombre_ciudad =:destino AND v.fecha_partida=:fecha_partida AND v.hora_partida=:hora_partida", nativeQuery = true )
		public List<Vuelo> findByDestinoAndFechaPartidaAndHoraPartida(@Param("destino") String destino, @Param("fecha_partida") LocalDate fechaPartida, @Param("hora_partida") LocalTime horaPartida);
	//OPCIONALES
	@Query(value = "SELECT v.* FROM vuelos v JOIN ciudades c "
    		+ "ON V.destino_id = c.id WHERE c.nombre_ciudad =:destino", nativeQuery = true )
	public List<Vuelo> findByDestino(@Param("destino") String destino);
	
    @Query(value = "SELECT v.* FROM vuelos v JOIN ciudades c "
    		+ "ON V.destino_id = c.id WHERE v.fecha_partida=:fecha_partida", nativeQuery = true )
    public List<Vuelo> findByFechaPartida(@Param("fecha_partida") LocalDate fechaPartida);
	    
    public List<Vuelo> findByAerolinea(String aerolinea);
    
	public List<Vuelo>findByTipoVuelo(TipoVuelo tipoVuelo);
	
	public List<Vuelo> findAllByEstadoVuelo(EstadoVuelo estadoVuelo);
	
	public boolean existsById(Long id);
	
	
    
}
