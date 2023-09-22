package com.tsti.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tsti.dao.VueloDAO;
import com.tsti.entidades.Vuelo;
import com.tsti.entidades.Vuelo.TipoVuelo;
import com.tsti.dao.AeropuertoDAO;
import com.tsti.dto.VueloDTO;
import com.tsti.entidades.Vuelo.EstadoVuelo;
import com.tsti.excepcion.SistemaGestionComercialAeropuertoException;
import com.tsti.excepcion.VueloException;
import com.tsti.entidades.Aeropuerto;
import com.tsti.faker.GenerarPrecioNeto;
import com.tsti.i18n.AppI18NProvider;
import com.tsti.presentacion.CrearVueloForm;
import com.tsti.presentacion.EditarVueloForm;
import com.tsti.views.FlightForm;
/**
 * @author Bruno
 * 
 * */
@Service
public class VueloServiceImpl implements IVueloService{
	
	private VueloDAO vueloDAO;	
	private final AeropuertoDAO aeropuertoDAO;
	private final AeropuertoServiceImpl aeropuertoService;
	private final AppI18NProvider i18NProvider;
	private final Locale locale = Locale.getDefault();
	private final String databaseCreateError;
	private final String databaseUpdateError;
	private final String databaseDeleteError;
	
	@Autowired
	public VueloServiceImpl(VueloDAO vueloDAO, AppI18NProvider i18NProvider, AeropuertoDAO aeropuertoDAO, AeropuertoServiceImpl aeropuertoService) {
		this.vueloDAO = vueloDAO;		
		this.aeropuertoDAO = aeropuertoDAO;
		this.aeropuertoService = aeropuertoService;		
		this.i18NProvider = i18NProvider;
		this.databaseCreateError = i18NProvider.getTranslation("database-create-error", locale);
		this.databaseUpdateError = i18NProvider.getTranslation("database-update-error", locale);
		this.databaseDeleteError = i18NProvider.getTranslation("delete-error", locale);
	}
	
	@Override
	public void crearVuelo(FlightForm vueloForm) throws SistemaGestionComercialAeropuertoException{
		Vuelo vuelo = new Vuelo();
		Aeropuerto origen = new Aeropuerto();
		Aeropuerto destino = new Aeropuerto();
				
		List<Vuelo> vuelos = vueloDAO.findByDestinoAndFechaPartidaAndHoraPartida(vueloForm.getDestinoValue().getCity(), vueloForm.getFechaPartidaValue(), vueloForm.getHoraPartidaValue());
		
		String datePattern = i18NProvider.getTranslation("date-pattern", locale);
		String timePattern = i18NProvider.getTranslation("time-pattern", locale);
					
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timePattern);
		
		String formatedDate = vueloForm.getFechaPartidaValue().format(dateFormatter);
		String formatedHour = vueloForm.getHoraPartidaValue().format(timeFormatter);
		
		if(!vuelos.isEmpty()) {
			
			String vueloExiste= i18NProvider.getTranslation("flight-exists", locale);
			
			String mensaje = String.format(vueloExiste, vueloForm.getNombreCiudadValue(),formatedDate,formatedHour);
			
			throw new VueloException(mensaje, HttpStatus.BAD_REQUEST.value());
			
		}
		
		if(vueloForm.getNroVueloValue() != null && vueloDAO.existsById(vueloForm.getNroVueloValue())) {	
			
			String idExiste= i18NProvider.getTranslation("flight-id-exists", locale);
			String mensaje = String.format(idExiste, vueloForm.getNroVueloValue());
			
			throw new VueloException (mensaje , HttpStatus.BAD_REQUEST.value());			
			
		}
		
		try {
			origen = aeropuertoService.getAeropuertoLocal();
		
		}catch(Exception e) {
			
			throw new SistemaGestionComercialAeropuertoException ("No se pudo obtener ciudad de origen ", HttpStatus.BAD_REQUEST.value());
		}		
		
		if(vueloForm.getDestinoValue().getId() != null){
			
			Optional<Aeropuerto>aeropuertoOptional = aeropuertoDAO.findById(vueloForm.getDestinoValue().getId());
			
			if(aeropuertoOptional.isPresent()) {
				
				destino = aeropuertoOptional.get();
			}				
		
			}else{
							
				destino.setIcao(vueloForm.getDestinoValue().getIcao());
				destino.setCity(vueloForm.getNombreCiudadValue());
				destino.setState(vueloForm.getDestinoValue().getState());
				destino.setCountry(vueloForm.getDestinoValue().getCountry());							
				
			}
			
			try {
				aeropuertoDAO.save(origen);
			    aeropuertoDAO.save(destino);
			
			} catch (Exception e) {
			    
				throw new VueloException(databaseUpdateError 
						+ ": " + origen.getCity() 
						+ "or :" + destino.getCity(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			vuelo.setAerolinea(vueloForm.getAerolineaValue());
			vuelo.setDestino(vueloForm.getDestinoValue());
			vuelo.setFechaPartida(vueloForm.getFechaPartidaValue());
			vuelo.setHoraPartida(vueloForm.getHoraPartidaValue());
			vuelo.setPrecioNeto(vueloForm.getPrecioNetoValue());
			vuelo.setEstadoVuelo(EstadoVuelo.REGISTRADO);
			vuelo.setTipoVuelo();
			
			try {
				
				vueloDAO.save(vuelo);
				System.out.println("Se ha creado un nuevo registro:" + vuelo.toString());
				
			} catch (Exception e) {
			    throw new VueloException(databaseCreateError, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
	}
	
	//API REST
	@Override
	public VueloDTO crearVuelo(CrearVueloForm vueloForm) throws SistemaGestionComercialAeropuertoException {
		
		Vuelo vuelo = new Vuelo();
		VueloDTO vueloDTO; 
		Aeropuerto origen = new Aeropuerto();
		Aeropuerto destino = new Aeropuerto();
		
		//Verificar si no existe vuelo con mismo destino y fecha-hora
		List<Vuelo> vuelos = vueloDAO.findByDestinoAndFechaPartidaAndHoraPartida(vueloForm.getNombreCiudad(), vueloForm.getFechaPartida(), vueloForm.getHoraPartida());
		
		if(!vuelos.isEmpty()) {
			
			throw new VueloException("El vuelo con destino: "+ vueloForm.getNombreCiudad() +" ya existe para la:" 
										+" Fecha: "+vueloForm.getFechaPartida()+", Hora: "+vueloForm.getHoraPartida(), HttpStatus.BAD_REQUEST.value());			
		}
		
		if(vueloForm.getNroVuelo() != null && vueloDAO.existsById(vueloForm.getNroVuelo())) {	
			
			
			throw new VueloException ("Vuelo con numero de vuelo: "+ vueloForm.getNroVuelo() + " ya existe.", HttpStatus.BAD_REQUEST.value());
			
		}
		
		try {
			origen = aeropuertoService.getAeropuertoLocal();
		
		}catch(Exception e) {
			
			throw new SistemaGestionComercialAeropuertoException ("No se pudo obtener ciudad de origen ", HttpStatus.BAD_REQUEST.value());
		}			
					
		if(vueloForm.getIdDestino() != null){
						
				Optional<Aeropuerto>ciudadOptional = aeropuertoDAO.findById(vueloForm.getIdDestino());
				
				if(ciudadOptional.isPresent()) {
					
					destino = ciudadOptional.get();
				}				
			
		}else{
						
			destino.setIcao(vueloForm.getCodAeropuerto());
			destino.setCity(vueloForm.getNombreCiudad());
			destino.setState(vueloForm.getProvincia());
			destino.setCountry(vueloForm.getPais());						
			
		}
		
		try {
			aeropuertoDAO.save(origen);
			aeropuertoDAO.save(destino);
		
		} catch (Exception e) {
		    
			throw new VueloException("Error en la Base de Datos, no se pudieron guardar las ciudades " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
		vuelo = vueloForm.toPojoConCiudad(origen, destino);

		
		if(vueloForm.getPrecioNeto() == null) {
			
			if(vuelo.getTipoVuelo() == TipoVuelo.NACIONAL) {
				
			
				vuelo.setPrecioNeto(GenerarPrecioNeto.generarPrecioNetoPesos());
			
			} else {			
				
				vuelo.setPrecioNeto(GenerarPrecioNeto.generarPrecioNetoDolares());
			
			}
			
		}		
				
		vuelo.setEstadoVuelo(EstadoVuelo.REGISTRADO);
		
		try {
			
		vuelo = vueloDAO.save(vuelo);		
		
		vueloDAO.save(vuelo);
		
		} catch (Exception e) {
		    
			throw new VueloException(databaseCreateError, HttpStatus.INTERNAL_SERVER_ERROR);
			
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
			    
				throw new VueloException(databaseDeleteError, HttpStatus.INTERNAL_SERVER_ERROR);
			}		
		
		}		
				
		return new VueloDTO(vuelo);
	}
	
	/**
	 *Version del metodo para Vaadin 
	 **/
	@Override
	public void reprogramarVuelo(FlightForm vueloForm) throws VueloException{
		
		Vuelo vuelo = new Vuelo();		
		Optional <Vuelo> vueloOptional;
		
		
		vueloOptional = vueloDAO.findById(vueloForm.getNroVueloValue());
        
        if (vueloOptional.isPresent()) {
            
        	vuelo = vueloOptional.get();
                       		
	        vuelo.setFechaPartida(vueloForm.getFechaPartidaValue());
	        vuelo.setHoraPartida(vueloForm.getHoraPartidaValue());
	        vuelo.setPrecioNeto(vueloForm.getPrecioNetoValue());
        
        // Cambiar estado a reprogramado
        vuelo.setEstadoVuelo(EstadoVuelo.REPROGRAMADO);
        
	        try {
	        	
	        	vueloDAO.save(vuelo);
	        
	        	} catch (Exception e) {
			    
				throw new VueloException(databaseUpdateError+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
				
	        	}  
        
        }		
		           
                
    }

	/**
	 *Version del metodo para la API REST 
	 **/

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
			    
				throw new VueloException(databaseUpdateError, HttpStatus.INTERNAL_SERVER_ERROR);
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
	public TreeSet<String> getAerolineas(){
		
		TreeSet<String> aerolineas = new TreeSet<>();		
		
		List<VueloDTO> vuelos = getAll();
		
		for(VueloDTO vuelo : vuelos) {
			
			aerolineas.add(vuelo.getAerolinea());
		}
		
		return aerolineas;		
		
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
