package com.tsti.servicios;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.tsti.entidades.Vuelo;
import com.tsti.entidades.Vuelo.TipoVuelo;
import org.springframework.stereotype.Service;

import com.tsti.dto.VueloDTO;
import com.tsti.entidades.Vuelo.EstadoVuelo;
import com.tsti.excepcion.SistemaGestionComercialAeropuertoException;
import com.tsti.excepcion.VueloException;
import com.tsti.presentacion.CrearVueloForm;
import com.tsti.presentacion.EditarVueloForm;
import com.tsti.views.FlightForm;


/**
 * Clase que contiene todos los servicios relacionados al vuelo 
 * 
 * 
 **/

@Service
public interface IVueloService{
	/**
	 *Metodo creado para la utilizacion con Vaadin 
	 * @throws SistemaGestionComercialAeropuertoException 
	 * 
	 * 
	 **/
	public void crearVuelo(FlightForm flightForm) throws VueloException, SistemaGestionComercialAeropuertoException;
	
	/**
	 *Recibe un formulario con todos los datos necesarios del vuelo.
	 * 
	 *@param vueloForm
	 *@return VueloDTO con informacion sobre el nuevo vuelo 
	 * @throws VueloException 
	 * @throws SistemaGestionComercialAeropuertoException 
	 **/
	public VueloDTO crearVuelo(CrearVueloForm vueloForm) throws VueloException, SistemaGestionComercialAeropuertoException;
	
	/**
	 * Selecciona un vuelo por su nroVuelo y realiza un soft delete 
	 * conservando los datos y relaciones del mismo pero dejando su estado en {@link EstadoVuelo.CANCELADO}
	 * 
	 * @param nroVuelo
	 * 
	 * @return VueloDTO con estado de vuelo actualizado
	 * @throws VueloException 
	 * */
	public VueloDTO cancelarVuelo(Long nroVuelo) throws VueloException;
		
	
	/**
	 *Recibe un formulario con todos los datos necesarios para actualizar
	 *el vuelo: {@link LocalDate}fecha, {@link LocalTime}hora y nroVuelo (este ultimo podria ser opcional)}.
	 *@param pathvariable nroVuelo  
	 *@param vueloForm 
	 *
	 *@return VueloDTO con informacion actualizada del nuevo vuelo 
	 * @throws VueloException 
	 **/
	public VueloDTO reprogramarVuelo(Long nroVuelo, EditarVueloForm vueloForm) throws VueloException;
	
	
	/**
	 *
	 *Version de reprogramarVuelo para la demostracion en Vaadin
	 *
	 **/
	public void reprogramarVuelo(FlightForm vueloForm) throws VueloException;
	
	
	/**
	 *Busca al vuelo por nroVuelo 
	 *
	 *@param nroVuelo
	 *
	 *@return  Optional<Vuelo> 
	 * */
	public Optional<Vuelo> findById(Long nroVuelo);
	
	/**
	 *Busca al vuelo por destino y fecha de partida
	 *
	 *@param destino String con el nombre_ciudad
	 *@param fecha de tipo LocalDate
	 *
	 *@return List<VueloDTO> con todos los resultados que coincidan con los parametros  
	 * */
	public List<VueloDTO> findByDestinoAndFechaPartida(String destino, LocalDate fecha);
	/**
	 *Busca al vuelo por destino 
	 *
	 *@param destino String con el nombre_ciudad
	 *
	 *@return List<VueloDTO> con todos los resultados que coincidan con los parametros  
	 * */
	public List<VueloDTO> findByDestino(String destino);
	/**
	 *Busca al vuelo fecha de partida
	 *
	 *
	 *@param fecha de tipo LocalDate
	 *
	 *@return List<VueloDTO> con todos los resultados que coincidan con los parametros  
	 * */
	public List<VueloDTO> findByFechaPartida(LocalDate fecha);
	/**
	 * Obtiene todos los vuelos filtrando por TipoVuelo.NACIONAL O TipoVuelo.INTERNACIONAL
	 * 
	 * @param tipoVuelo
	 * 
	 * @return List<Vuelo> con todos los resultados que coincidan con los parametros
	 * */
	public List<Vuelo> obtenerVuelosPorTipo(TipoVuelo tipoVuelo);
	
	/**
	 * Obtiene todos los vuelos
	 *
	 * @return List<VueloDTO> con todos los resultados que coincidan con los parametros
	 * */
	public List<VueloDTO> getAll();
	
	/**
	 * Obtiene todos los vuelos filtrando por EstadoVuelo.REGISTRADO O 
	 * EstadoVuelo.REPROGRAMADO, EstadoVuelo.DEMORADO, EstadoVuelo.CANCELADO.
	 * 
	 * @param EstadoVuelo
	 * 
	 * @return List<Vuelo> con todos los resultados que coincidan con los parametros
	 * */
	public List<VueloDTO> findAllByEstadoVuelo(EstadoVuelo estadoVuelo);

		

}
