package com.tsti.servicios;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.tsti.dao.ClienteDAO;
import com.tsti.dao.PasajeDAO;
import com.tsti.dao.VueloDAO;
import com.tsti.dto.PasajeDTO;
import com.tsti.entidades.Clientes;
import com.tsti.entidades.Pasaje;
import com.tsti.entidades.Vuelo;
import com.tsti.excepcion.CostoPasajeException;
import com.tsti.excepcion.ValidacionFallidaEnPasajeException;

import com.tsti.entidades.Vuelo.EstadoVuelo;
import com.tsti.entidades.Vuelo.TipoVuelo;
import com.tsti.excepcion.VueloException;


/**
 * @author JOA
 *
 */
@Service
public class PasajeServiceImpl implements IPasajeService {
	private final PasajeDAO pasajeDAO;
	@Autowired
	private ClienteDAO clienteDAO;

	@Autowired
	private VueloDAO vueloDAO;
	@Autowired
	private VueloServiceImpl vueloService;

    public PasajeServiceImpl(PasajeDAO pasajeDAO, VueloDAO vueloDAO, VueloServiceImpl vueloService) {
        this.pasajeDAO = pasajeDAO;
        this.vueloService= vueloService;
        this.vueloDAO = vueloDAO;
    }
	@Override
	public Pasaje crearPasaje(Vuelo vuelo, Clientes pasajero, Integer nroAsiento) throws ValidacionFallidaEnPasajeException {
		/*
		 * METODO DISEÑADO PARA SER CONTENIDO EN UN BLOQUE TRY-CATCH A LA HORA 
		 * DE LLAMARLO, ESTO PARA MANEJO DE ERRORES DE UNA FORMA MAS SENCILLA. :D
		*/
		

		//VALIDAMOS QUE EXISTAN EL CLIENTE Y EL VUELO, VIENDO SI ESTAN ALMACENADOS EN LA BASE DE DATOS
		if (!this.existeCliente(pasajero)) {
	        throw new ValidacionFallidaEnPasajeException("El cliente con DNI " + pasajero.getDni() + " no existe.");
	    }
	    if (!this.existeVuelo(vuelo)) {
	        throw new ValidacionFallidaEnPasajeException("El vuelo con número " + vuelo.getNroVuelo() + " no existe.");
	    }
	    
	    //VALIDACIONES DEL TP
	    	//SOBRE PASAJERO
	    if(vuelo.getTipoVuelo()== Vuelo.TipoVuelo.NACIONAL) {
	    	if(!pasajero.tieneDatosBasicos())
	    		throw new ValidacionFallidaEnPasajeException("El cliente no cuenta con los datos basicos!");
	    }else if(vuelo.getTipoVuelo()== Vuelo.TipoVuelo.INTERNACIONAL) {
	    	if(!pasajero.tieneDatosBasicos())
	    		throw new ValidacionFallidaEnPasajeException("El cliente no cuenta con los datos basicos!");
	    	if(!pasajero.tienePasaporte())
	    		throw new ValidacionFallidaEnPasajeException("El cliente no cuenta con pasaporte!");	
	    }
	    
	    	//SOBRE VUELO
	    		//EL VUELO ES UN VUELO FUTURO
	    if(vuelo.getEstadoVuelo() == Vuelo.EstadoVuelo.CANCELADO) {
	    	throw new ValidacionFallidaEnPasajeException("El vuelo ingresado no es un vuelo futuro, fue cancelado!");
	    }else if(vuelo.getEstadoVuelo() == Vuelo.EstadoVuelo.TERMINADO) {
	    	throw new ValidacionFallidaEnPasajeException("El vuelo ingresado no es un vuelo futuro, fue finalizado!");
	    }
	    
	    		//EL ASIENTO ELEJIDO ESTÁ DISPONIBLE
	    if(vuelo.getNroAsientos() < nroAsiento) {
	    	throw new ValidacionFallidaEnPasajeException("Ese asiento no existe para ese vuelo!");
	    }
	    if(vuelo.asientoOcupado(nroAsiento)) {
	    	throw new ValidacionFallidaEnPasajeException("Ese asiento esta ocupado!");
	    }

	    //POSTING
	    Pasaje pasaje = new Pasaje(vuelo, pasajero, nroAsiento);
	    
	    return pasajeDAO.save(pasaje);

	} 
	
	public PasajeDTO getCostoPasaje(Long nroVuelo, Long dni) throws VueloException{
		
		//Creamos el DTO en base al numero que nos pasaron
		PasajeDTO pasajeDTO = new PasajeDTO();
		
		
		//chequeamos que el vuelo exista en la BD		
		Optional<Vuelo> vueloOptional = vueloService.findById(nroVuelo);
		if(vueloOptional.isPresent()){
			
			Vuelo vuelo = vueloOptional.get();
			
			if (vuelo.getEstadoVuelo() == EstadoVuelo.CANCELADO) {
	            
				throw new VueloException("El vuelo está cancelado, no se puede obtener el costo del pasaje", HttpStatus.BAD_REQUEST.value());
				
	        }		
			TasaServiceImpl tasaService = new TasaServiceImpl();
			
			pasajeDTO.setNroVuelo(nroVuelo);
			pasajeDTO.setDni(dni);
			//Se obtiene el precio neto del pasajeDTO y se deduce tasa segun tipo de vuelo.
			BigDecimal precioNeto = vuelo.getPrecioNeto();			
			BigDecimal tasa = tasaService.getTasa(vuelo.getTipoVuelo());			
			BigDecimal precioFinal = precioNeto.add(tasa);
			
			//Chequeamos tipo de vuelo para realizar cotizacion dado el caso
			if(vuelo.getTipoVuelo() == TipoVuelo.INTERNACIONAL){
				
				CotizacionServiceImpl cotizacionService = new CotizacionServiceImpl();
				
				//multiplicamos y redondeamos a un numero de dos cifras despues de la coma
				try {
					precioFinal =  precioFinal.multiply(cotizacionService.getCotizacionDolarOficial())
															.setScale(2, RoundingMode.HALF_DOWN);
				} catch (CostoPasajeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
				
			}
			
			pasajeDTO = new PasajeDTO(vuelo.getNroVuelo(), dni, precioNeto, tasa, 
										precioFinal, vuelo.getTipoVuelo());				
			
		} else {
			
			throw new VueloException("El vuelo con numero: "+ nroVuelo +" no encontrado", HttpStatus.NOT_FOUND.value());        

		}
		
		return pasajeDTO;

	}
	
    public Boolean existeCliente(Clientes cliente) {
    	return clienteDAO.findById(cliente.getId()).isPresent();
    }
    public Boolean existeVuelo(Vuelo vuelo) {
    	return vueloDAO.findById(vuelo.getNroVuelo()).isPresent();
    }
	@Override
	public Optional<Pasaje> findById(Long id){
		return pasajeDAO.findById(id);
	}	
}
