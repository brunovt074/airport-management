package com.tsti.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
	private CiudadDAO ciudadDAO;
	private CiudadFactory ciudadFactory;
	private final AppI18NProvider i18NProvider;
	private final Locale locale = Locale.getDefault();
	private final String databaseCreateError;
	private final String databaseUpdateError;
	private final String databaseDeleteError;
	
	@Autowired
	public VueloServiceImpl(VueloDAO vueloDAO, CiudadDAO ciudadDAO, CiudadFactory ciudadFactory, AppI18NProvider i18NProvider) {
		this.vueloDAO = vueloDAO;	
		this.ciudadDAO = ciudadDAO;
		this.ciudadFactory = ciudadFactory;		
		this.i18NProvider = i18NProvider;
		this.databaseCreateError = i18NProvider.getTranslation("database-create-error", locale);
		this.databaseUpdateError = i18NProvider.getTranslation("database-update-error", locale);
		this.databaseDeleteError = i18NProvider.getTranslation("delete-error", locale);
	}
	
	@Override
	public void crearVuelo(FlightForm vueloForm) throws VueloException{
		Vuelo vuelo = new Vuelo();
		Ciudad origen = new Ciudad();
		Ciudad destino = new Ciudad();
				
		List<Vuelo> vuelos = vueloDAO.findByDestinoAndFechaPartidaAndHoraPartida(vueloForm.getDestinoValue().getNombreCiudad(), vueloForm.getFechaPartidaValue(), vueloForm.getHoraPartidaValue());
		
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
//			throw new VueloException("El vuelo con destino: "+ vueloForm.getNombreCiudadValue() 
//			+" ya existe para la fecha: "+vueloForm.getFechaPartidaValue()
//			+"- hora: "+vueloForm.getHoraPartidaValue(), HttpStatus.BAD_REQUEST.value());			
		}
		
		if(vueloForm.getNroVueloValue() != null && vueloDAO.existsById(vueloForm.getNroVueloValue())) {	
			
			String idExiste= i18NProvider.getTranslation("flight-id-exists", locale);
			String mensaje = String.format(idExiste, vueloForm.getNroVueloValue());
			
			throw new VueloException (mensaje , HttpStatus.BAD_REQUEST.value());
			//throw new VueloException ("Vuelo con numero de vuelo: "+ vueloForm.getNroVueloValue() + " ya existe.", HttpStatus.BAD_REQUEST.value());
			
		}
		
		if(ciudadDAO.existsByCodAeropuerto("SAAV")){
				
				origen = ciudadDAO.findFirstByCodAeropuertoAndNombreCiudad
														("SAAV", "Sauce Viejo");				
			
		} else{
				
				origen = ciudadFactory.getCiudadSauceViejo();
				
				throw new VueloException ("No se pudo obtener ciudad de origen ", HttpStatus.BAD_REQUEST.value());
				
		}
		
		if(vueloForm.getDestinoValue().getId() != null){
			
			Optional<Ciudad>ciudadOptional = ciudadDAO.findById(vueloForm.getDestinoValue().getId());
			
			if(ciudadOptional.isPresent()) {
				
				destino = ciudadOptional.get();
			}				
		
			}else{
							
				destino.setcodAeropuerto(vueloForm.getDestinoValue().getcodAeropuerto());
				destino.setNombreCiudad(vueloForm.getNombreCiudadValue());
				destino.setProvincia(vueloForm.getDestinoValue().getProvincia());
				destino.setPais(vueloForm.getDestinoValue().getPais());
				destino.setCodPostal(vueloForm.getDestinoValue().getCodPostal());			
				
			}
			
			try {
			    ciudadDAO.save(origen);
			    ciudadDAO.save(destino);
			
			} catch (Exception e) {
			    
				throw new VueloException(databaseUpdateError 
						+ ": " + origen.getNombreCiudad() 
						+ "or :" + destino.getNombreCiudad(),
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
	public VueloDTO crearVuelo(CrearVueloForm vueloForm) throws VueloException {
		
		Vuelo vuelo = new Vuelo();
		VueloDTO vueloDTO; 
		Ciudad origen = new Ciudad();
		Ciudad destino = new Ciudad();
		
		//Verificar si no existe vuelo con mismo destino y fecha-hora
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
