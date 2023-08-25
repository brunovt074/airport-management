package com.tsti.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import com.tsti.dao.VueloDAO;
import com.tsti.entidades.Vuelo;
import com.tsti.entidades.Vuelo.TipoVuelo;

import com.tsti.dao.CiudadDAO;
import com.tsti.dto.VueloDTO;
import com.tsti.entidades.Vuelo.EstadoVuelo;
import com.tsti.excepcion.VueloException;
import com.tsti.entidades.Ciudad;
import com.tsti.faker.CiudadFactory;
import com.tsti.faker.GenerarPrecioNeto;
import com.tsti.presentacion.CrearVueloForm;
import com.tsti.presentacion.EditarVueloForm;

/**
 * @author Bruno
 * 
 * */
@Service
public class VueloServiceImpl implements IVueloService{
	
	private VueloDAO vueloDAO;	
	private CiudadDAO ciudadDAO;
	private CiudadFactory ciudadFactory;	
	@Autowired
	public VueloServiceImpl(VueloDAO vueloDAO, CiudadDAO ciudadDAO, CiudadFactory ciudadFactory) {
		this.vueloDAO = vueloDAO;	
		this.ciudadDAO = ciudadDAO;
		this.ciudadFactory = ciudadFactory;		
	}
	@Override
	public VueloDTO crearVuelo(CrearVueloForm vueloForm) throws VueloException {
		
		Vuelo vuelo = new Vuelo();
		VueloDTO vueloDTO; 
		Ciudad origen = new Ciudad();
		Ciudad destino = new Ciudad();
		List<Vuelo> vuelos = vueloDAO.findByDestinoAndFechaPartidaAndHoraPartida(vueloForm.getNombreCiudad(), vueloForm.getFechaPartida(), vueloForm.getHoraPartida());
		
		if(!vuelos.isEmpty()) {
			
			throw new VueloException("El vuelo con destino: "+ vueloForm.getNombreCiudad() +" ya existe para la:" 
										+" Fecha: "+vueloForm.getFechaPartida()+", Hora: "+vueloForm.getHoraPartida(), HttpStatus.BAD_REQUEST.value());			
		}
		
		if(vueloForm.getNroVuelo() != null && vueloDAO.existsById(vueloForm.getNroVuelo())) {	
			
			throw new VueloException ("Vuelo con numero de vuelo: "+ vueloForm.getNroVuelo() + " ya existe.", HttpStatus.BAD_REQUEST.value());
			
		}
		
		if(ciudadDAO.existsByCodAeropuerto("SAAV")){
				
				origen = ciudadDAO.findFirstByCodAeropuertoAndNombreCiudad
														("SAAV", "Sauce Viejo");
				
			
		} else{
				
				origen = ciudadFactory.getCiudadSauceViejo();
				
				throw new VueloException ("No se pudo obtener ciudad de origen "+ vueloForm.getNroVuelo() + " ya existe.", HttpStatus.BAD_REQUEST.value());
				
			}			
					
		if(vueloForm.getIdDestino() != null){
						
				Optional<Ciudad>ciudadOptional = ciudadDAO.findById(vueloForm.getIdDestino());
				
				if(ciudadOptional.isPresent()) {
					
					destino = ciudadOptional.get();
				}				
			
		}else{
						
			destino.setcodAeropuerto(vueloForm.getCodAeropuerto());
			destino.setNombreCiudad(vueloForm.getNombreCiudad());
			destino.setProvincia(vueloForm.getProvincia());
			destino.setPais(vueloForm.getPais());
			destino.setCodPostal(vueloForm.getCodPostal());			
			
		}
		
		try {
		    ciudadDAO.save(origen);
		    ciudadDAO.save(destino);
		
		} catch (Exception e) {
		    
			throw new VueloException("Error en la Base de Datos, no se pudieron guardar las ciudades " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		vuelo = vueloForm.toPojoConCiudad(origen, destino);

		
		if(vueloForm.getPrecioNeto() == null) {
			
			if(vuelo.getTipoVuelo() == TipoVuelo.NACIONAL) {
				
			
				vuelo.setPrecioNeto(GenerarPrecioNeto.generarPrecioNetoPesos());
			
			}else {			
				
				vuelo.setPrecioNeto(GenerarPrecioNeto.generarPrecioNetoDolares());
			
			}
			
		}		
				
		vuelo.setEstadoVuelo(EstadoVuelo.REGISTRADO);
		
		try {
			
		vuelo = vueloDAO.save(vuelo);		
		
		vueloDAO.save(vuelo);
		
		} catch (Exception e) {
		    
			throw new VueloException("Error en la Base de Datos, no se pudieron crear el vuelo." + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
		vueloDTO = new VueloDTO(vuelo);		
		
		return vueloDTO;
				
	}
	
	@Override
	public VueloDTO cancelarVuelo (Long nroVuelo) throws VueloException{		
		
		Vuelo vuelo = new Vuelo();
		Optional <Vuelo> vueloOptional;
		
		vueloOptional = vueloDAO.findById(nroVuelo);
			
		if (vueloOptional.isPresent()) {
	            
			vuelo = vueloOptional.get();
			
			vuelo.setEstadoVuelo(EstadoVuelo.CANCELADO);
			vuelo.setPrecioNeto(BigDecimal.valueOf(0.00));
			
			try {
				vueloDAO.save(vuelo);
			
			} catch (Exception e) {
			    
				throw new VueloException("Error en la Base de Datos, no se pudieron crear el vuelo." + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}		
		
		}		
				
		return new VueloDTO(vuelo);
	}


	@Override
	public VueloDTO reprogramarVuelo(Long nroVuelo, EditarVueloForm vueloForm) throws VueloException{
		
		Vuelo vuelo = new Vuelo();		
		Optional <Vuelo> vueloOptional;
		
		
		vueloOptional = vueloDAO.findById(nroVuelo);
        
        if (vueloOptional.isPresent()) {
            
        	vuelo = vueloOptional.get();
                       		
	        vuelo.setFechaPartida(vueloForm.getFechaPartida());
	        vuelo.setHoraPartida(vueloForm.getHoraPartida());
        
        // Cambiar estado a reprogramado
        vuelo.setEstadoVuelo(EstadoVuelo.REPROGRAMADO);
        
	        try {
	        	
	        	vueloDAO.save(vuelo);
	        
	        	} catch (Exception e) {
			    
				throw new VueloException("Error en la Base de Datos, no se pudieron crear el vuelo." + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        	}  
        
        } 
		
		           
        return new VueloDTO(vuelo);        
    }
	 
	@Override
	public Optional<Vuelo> findById(Long nroVuelo){
		
		Optional <Vuelo> vueloOptional;		
		
		vueloOptional = vueloDAO.findById(nroVuelo);
				
		return vueloOptional;
	
	}
	
	@Override
	public List<VueloDTO> findByDestinoAndFechaPartida(String destino, LocalDate fecha) {
        
		List<Vuelo> vuelos = vueloDAO.findByDestinoAndFechaPartida(destino, fecha);
				
		List<VueloDTO> vuelosDTO = new ArrayList<>();
		
		for(Vuelo vuelo : vuelos ){			
			
			vuelosDTO.add(new VueloDTO(vuelo));
			
		}
		
		return vuelosDTO;
    }
	
	@Override
	public List<VueloDTO> findByDestino(String destino) {
		
		List<Vuelo> vuelos = vueloDAO.findByDestino(destino);			
		
		List<VueloDTO> vuelosDTO = new ArrayList<>();
		
		for(Vuelo vuelo : vuelos ){			
			
			vuelosDTO.add(new VueloDTO(vuelo));
			
		}
		
		return vuelosDTO;		
				
	}
	
	@Override
	public List<VueloDTO> findByFechaPartida(LocalDate fecha) {
		
		List<Vuelo> vuelos = vueloDAO.findByFechaPartida(fecha);		
		
		List<VueloDTO> vuelosDTO = new ArrayList<>();	
			
		for(Vuelo vuelo : vuelos ){			
			
			vuelosDTO.add(new VueloDTO(vuelo));
			
		}
		
		return vuelosDTO;			
		
    }
	
	@Override
	public List<Vuelo> obtenerVuelosPorTipo(TipoVuelo tipoVuelo) {
		
		List<Vuelo> vuelos = vueloDAO.findByTipoVuelo(tipoVuelo);		
		
		return vuelos; 
	}
	@Override
	public List<VueloDTO> getAll() {
		
		List<Vuelo> vuelos = vueloDAO.findAll();		
				
		List<VueloDTO> vuelosDTO = new ArrayList<>();
		
		for(Vuelo  vuelo : vuelos ){			
				
			vuelosDTO.add(new VueloDTO(vuelo));
			
		}		
		
		return vuelosDTO;			
		
	}
	
	@Override
	public List<VueloDTO> findAllByEstadoVuelo(EstadoVuelo estadoVuelo){
		
		List<Vuelo> vuelos = vueloDAO.findAllByEstadoVuelo(estadoVuelo);			
		
		List<VueloDTO> vuelosPorEstadoDTO = new ArrayList<>();	
			
		for(Vuelo  vuelo : vuelos ){			
			
			vuelosPorEstadoDTO.add(new VueloDTO(vuelo));
			
		}
		
		return vuelosPorEstadoDTO;
		
	}

}
