/**
 * 
 */
package com.tsti.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tsti.entidades.Ciudad;



/**
 * @author Bruno
 *
 */

@Repository
public interface CiudadDAO extends JpaRepository<Ciudad, Long> {

	public boolean existsByCodAeropuerto(String codAeropuerto);
	
	public boolean existsByCodPostal(String codPostal);
	
	public boolean existsByNombreCiudadAndProvinciaAndPais(String nombreCiudad, String provincia, String pais);
	
	public boolean existsByNombreCiudadAndProvincia(String nombreCiudad, String provincia);
	
	public Ciudad findByNombreCiudad(String nombreCiudad);
	
	public Ciudad findByCodAeropuertoAndNombreCiudad(String codAeropuerto, String nombreCiudad);
	
	public Ciudad findFirstByCodAeropuertoAndNombreCiudad(String codAeropuerto, String nombreCiudad);
	
	
}
