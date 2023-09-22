package com.tsti.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tsti.entidades.Vuelo.EstadoVuelo;
import com.tsti.entidades.Vuelo;
import com.tsti.servicios.VueloServiceImpl;
import com.tsti.dto.VueloDTO;
import com.tsti.excepcion.SistemaGestionComercialAeropuertoException;
import com.tsti.excepcion.VueloException;
import com.tsti.presentacion.CrearVueloForm;
import com.tsti.presentacion.EditarVueloForm;
import com.tsti.rest.error.VueloErrorInfo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

/**
 *
 *Controlador del Servicio de Gestion de Vuelos
 *Posee los metodos para: Proporciona métodos para buscar vuelos,
 *crear nuevos vuelos, actualizar vuelos existentes y cancelar vuelos. *
 *   
 **/
@Validated
@RestController
public class VueloController {

    private final VueloServiceImpl vueloService;
    //Coleccion de entidades Response Entity
    private CollectionModel<VueloDTO> vuelosCollectionModel;

    @Autowired
    public VueloController(VueloServiceImpl vueloService) {
        this.vueloService = vueloService;        
    }
    
    /**
     * Obtener todo los vuelos
     * 
     * curl --location --request GET 'http://localhost:8081/vuelos' \
	 *	--header 'Content-Type: text/plain' \
	 * 	--data '
	 * 	'
     * 
     * @return Response entitiy con CollectionModel<VueloDTO> de todos los vuelos
     *  
     **/
    @GetMapping("/vuelos")
    public ResponseEntity<CollectionModel<VueloDTO>> showVuelos() {
           	
    	List<VueloDTO> vuelosDTO = vueloService.getAll();
    	
    	if(vuelosDTO == null){
    		
    		return ResponseEntity.notFound().build();
    		
    	}else{
    		
    		//Coleccion que agrupa varias Entities
            CollectionModel<VueloDTO> vuelosCollectionModel = CollectionModel.of(vuelosDTO);
            
            //Link a si mismo
            vuelosCollectionModel.add(Link.of("/vuelos","self"));

            return ResponseEntity.ok(vuelosCollectionModel);    		
    	}   
        
    }
    
    /**
     * Obtener vuelos por estado del vuelo
     * 
     * curl --location 'http://localhost:8081/vuelos/estado-vuelo?estado=RegIsTrado'
     * 
     * @param estado: "registrado" - "reprogramado" - "demorado" - "cancelado". Las cadenas pueden ser en mayuscula o minuscula.  
     * 
     * @return Response Entity con CollectionModel<VueloDTO> de todos los vuelos y links relacionados. 
     * @throws VueloException 
    **/
    @GetMapping("/vuelos/estado-vuelo")
    public ResponseEntity<?> showVuelosPorEstadoVuelo(
    							@RequestParam("estado") String estado, HttpServletRequest request) throws VueloException{
    	
    	try {
            if (!isValidEstado(estado)) {
                
            	throw new VueloException("Estado inválido: " + estado, HttpStatus.BAD_REQUEST);
            	
            }
    	
			List<VueloDTO> vuelosDTO;
			
			if(estado.equalsIgnoreCase("registrado")){
				vuelosDTO = vueloService.findAllByEstadoVuelo(EstadoVuelo.REGISTRADO);
			
			}else if(estado.equalsIgnoreCase("reprogramado")) {
				
				vuelosDTO = vueloService.findAllByEstadoVuelo(EstadoVuelo.REPROGRAMADO);
			
			}else if(estado.equalsIgnoreCase("demorado")){
				
				vuelosDTO = vueloService.findAllByEstadoVuelo(EstadoVuelo.DEMORADO);
			
			}else if(estado.equalsIgnoreCase("cancelado")) { 
			
				vuelosDTO = vueloService.findAllByEstadoVuelo(EstadoVuelo.CANCELADO);
			
			} else {
				
				return ResponseEntity.badRequest().build();
				
			}  		
			
			if(vuelosDTO == null){
				
				return ResponseEntity.notFound().build();
			
			}else{
				
				//Coleccion que agrupa varias Entities
		        CollectionModel<VueloDTO> vuelosCollectionModel = CollectionModel.of(vuelosDTO);
		        
		        //Link a si mismo
		        vuelosCollectionModel.add(Link.of("/vuelos/estado-vuelo?estado="+estado,"self"))
		        					 .add(AppLinks.showVuelosLink());
		
		        return ResponseEntity.ok(vuelosCollectionModel);    		
			}
    	
    	} catch (VueloException ex) {
            
    		VueloErrorInfo errorInfo = new VueloErrorInfo(ex.getStatusCode(), ex.getMessage(), request.getRequestURI());
            
    		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
    		
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInfo);
        }
    }    
    /**
     * Obtener vuelo por ID
     * curl --location 'http://localhost:8081/vuelos/2'
     * 
     * @param id con nro_vuelo de tipo {@link Long}
     * @param request 
     * 
     * @return ResponseEntity con EntityModel<VueloDTO> que contiene el vuelo buscado.
     **/
    @GetMapping("/vuelos/{id}")
    public ResponseEntity<?> getVueloById(@PathVariable @Min(value= 1, message="id debe ser mayor 0") Long id, HttpServletRequest request) {

        try {
            if (id == null) {
                throw new VueloException("ID de vuelo inválido", HttpStatus.BAD_REQUEST);
            } else if (id < 1) {
                throw new VueloException("ID de vuelo inválido: debe ser mayor o igual a 1", HttpStatus.BAD_REQUEST);
            } else {
                Optional<Vuelo> vueloOptional = vueloService.findById(id);

                if (vueloOptional.isPresent()) {
                    VueloDTO vueloDTO = new VueloDTO(vueloOptional.get());
                    EntityModel<VueloDTO> vueloEntityModel = EntityModel.of(vueloDTO)
                            .add(Link.of("/vuelos/" + id, "self"))
                            .add(AppLinks.showVuelosLink());

                    return ResponseEntity.ok().body(vueloEntityModel);
                } else {
                    throw new VueloException("Vuelo no encontrado", HttpStatus.NOT_FOUND);
                }
            }
        } catch (VueloException ex) {
            VueloErrorInfo errorInfo = new VueloErrorInfo(ex.getStatusCode(), ex.getMessage(), request.getRequestURI());
            errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInfo);
        } catch (Exception ex) {
            VueloErrorInfo errorInfo = new VueloErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), request.getRequestURI());
            errorInfo.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorInfo);
        }
    }


    
    /**
     *curl --location --request GET 'http://localhost:8081/vuelos/filtrar-destino-fecha?destino=inca&fecha=2023-07-06' \
		--header 'Content-Type: text/plain' \
		--data '
'
     * 
     *Considerando que es una consulta frecuente obtiene todos los vuelos 
     *que coincidan con los parametros
     *
     *@param destino de tipo {@link String} correspondiente al nombre_ciudad de destino
     *@param fecha de tipo {@link LocalDate} correspondiente a fecha de partida en formato (yyyy-MM-dd)
     *
     *@return Response Entity con CollectionModel<VueloDTO> de todos los vuelos que satisfacen los parametros, y links relacionados
     **/
    @GetMapping("/vuelos/filtrar-destino-fecha")
    public ResponseEntity<CollectionModel<VueloDTO>> getVuelosByDestinoAndFecha(
    		@RequestParam("destino")String destino,
    		@RequestParam("fecha")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha) {
    	
    	List<VueloDTO> vuelosDTO = vueloService.findByDestinoAndFechaPartida(destino, fecha);
    	
    	if (vuelosDTO.isEmpty()) {
        
    		return ResponseEntity.notFound().build();
    	
    	} else {	        
	        
	        vuelosCollectionModel = CollectionModel.of(vuelosDTO)
	        						.add(AppLinks.getLinksVuelos(2, destino, fecha));
	                    
	        return ResponseEntity.ok(vuelosCollectionModel);        	
        }
    }
    
    
    /**
     *Obtener vuelos por destino. 
     *
     *curl --location 'http://localhost:8081/vuelos/filtrar-por-destino?destino=inca'
     *
     *@param destino de tipo {@link String} correspondiente al nombre_ciudad de destino     
     *
     *@return Response Entity con CollectionModel<VueloDTO> de todos los vuelos que satisfacen los parametros.
     **/
    @GetMapping("/vuelos/filtrar-por-destino")
    public ResponseEntity<CollectionModel<VueloDTO>> getVuelosByDestino(
    												@RequestParam String destino) {
    	
    	List<VueloDTO> vuelosDTO = vueloService.findByDestino(destino);
        
        if(vuelosDTO.isEmpty()){
        	
        	return ResponseEntity.notFound().build();
        	
        } else {
	        	        
	        vuelosCollectionModel = CollectionModel.of(vuelosDTO)
	        						.add(AppLinks.getLinksVuelos(3, destino, null));
	
	        return ResponseEntity.ok(vuelosCollectionModel);
        }
    }    
    
    /**
     *Obtener vuelos fecha. 
     *curl --location 'http://localhost:8081/vuelos/filtrar-por-fecha?fecha=2023-07-06' \
		--data ''
     *
     *@param fecha de tipo {@link LocalDate}correspondiente a fecha de partida en formato (yyyy-MM-dd)
     *
     *@return Response Entity con CollectionModel<VueloDTO> de todos los vuelos que satisfacen los parametros.
     **/
	@GetMapping("/vuelos/filtrar-por-fecha")
    public ResponseEntity<CollectionModel<VueloDTO>> getVuelosByFecha(
    		@RequestParam("fecha") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate fecha){
        
		
        List<VueloDTO> vuelosDTO = vueloService.findByFechaPartida(fecha);
    	
        if(vuelosDTO.isEmpty()){
        	
        	return ResponseEntity.notFound().build();
        	
        } else {
	                
	        vuelosCollectionModel = CollectionModel.of(vuelosDTO)
	        							.add(AppLinks.getLinksVuelos(4, null, fecha));
	        
	        return ResponseEntity.ok(vuelosCollectionModel);
        }
    }
	
    /**
     *Crear un vuelo.
     *
     * curl --location 'http://localhost:8081/vuelos' \
	--header 'Content-Type: application/json' \
	--data '{
	  "aerolinea": "Lufthansa",
	  "avion": "Airbus A380",
	  "fechaPartida": "2023-07-16",
	  "horaPartida": "11:30",
	  "nroAsientos": "12",
	  "precioNeto": "5321",
	  "codAeropuerto": "BER",
	  "nombreCiudad": "Berlin",
	  "provincia": "Berlin",
	  "pais": "Alemania",
	  "codPostal": "60311"
	}
     *Recibe un formulario con todos los datos necesarios. 
     *La ciudad de origen se setea como Sauce Viejo por defecto. 
     *La ciudad de destino se busca en la BD 
     *y si no existe se crea una nueva con los datos del formulario.
     *
     * @param vueloForm formulario de tipo {@link CrearVueloForm}
     * @return ResponseEntity<EntityModel<VueloDTO>> con la informacion del nuevo vuelo creado
     * @throws VueloException 
     **/
	@PostMapping("/vuelos")    
	public ResponseEntity<?> crearVuelo(@Valid @RequestBody CrearVueloForm vueloForm, HttpServletRequest request) {
	    try {
	        VueloDTO vueloDTO = vueloService.crearVuelo(vueloForm);

	        EntityModel<VueloDTO> nuevoVueloEntity = EntityModel.of(vueloDTO)
	                .add(WebMvcLinkBuilder.linkTo(methodOn(VueloController.class).getVueloById(vueloDTO.getNroVuelo(), request))
	                        .withSelfRel())
	                .add(AppLinks.showVuelosLink());

	        return ResponseEntity.ok().body(nuevoVueloEntity);
	    
	    } catch (SistemaGestionComercialAeropuertoException ex) {
	        
	    	SistemaGestionComercialAeropuertoException errorInfo = new SistemaGestionComercialAeropuertoException(ex.getMessage(), ex.getStatusCode());
	        
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInfo);
	    }
	}

    
    /**
     * Actualizar fecha y hora de vuelo.
     * 
     * curl --location --request PATCH 'http://localhost:8081/vuelos/11' \
		--header 'Content-Type: application/json' \
		--data '{
		    "fechaPartida": "2023-07-15",
		    "horaPartida": "12:45"
}'
     * Se recibe un formulario con la fecha, hora de vuelo o ambas.
     * 
     * @param id como PathVariable correspondiente al nroVuelo de tipo {@link Long}
     * @param vueloForm del tipo {@link EditarVueloForm} con los datos necesarios para la actualizacion 
     * 
     * @return ResponseEntity<EntityModel<VueloDTO>> con los datos del vuelo actualizados. 
     * @throws VueloException 
     ***/
    @PatchMapping("/vuelos/{id}")
    public ResponseEntity<EntityModel<VueloDTO>> actualizarFechaHoraVuelo(
            									@PathVariable("id") Long vueloId,
            									@RequestBody EditarVueloForm vueloForm) 
            											throws VueloException {
    	
    	VueloDTO vueloDTO;
    	
    	if (vueloForm.getFechaPartida() == null && vueloForm.getHoraPartida() == null) {
    		
    		throw new VueloException ("Debe ingresar al menos un campo para actualizar ", HttpStatus.BAD_REQUEST.value());
    		//return ResponseEntity.badRequest().build();
    		
    	} else {
    		
    		vueloDTO = vueloService.reprogramarVuelo(vueloId, vueloForm);
    		
    		if (vueloDTO.getNroVuelo() == null) {
    	    	
    			throw new VueloException("El vuelo con numero: "+ vueloId +" no encontrado", HttpStatus.NOT_FOUND.value());
        	
    		}else {
    			
    			EntityModel<VueloDTO> vueloReprogramadoEntity = EntityModel.of(vueloDTO)
											    					.add(WebMvcLinkBuilder.linkTo(
													    					methodOn(VueloController.class)
													    					.getVueloById(vueloDTO.getNroVuelo(), null))
													    					.withSelfRel())							
											    					.add(AppLinks.showVuelosLink());																	
                
                return ResponseEntity.ok().body(vueloReprogramadoEntity);
    			
    		}
    	}	
    } 
    
    /**
     *Cancelar vuelo.
     *
     *curl --location --request DELETE 'http://localhost:8081/vuelos/2'
     *
     *Consiste en realizar un soft delete cambiando el estado del vuelo a {@link EstadoVuelo.CANCELADO}
     *y colocando el atributo {@link BigDecimal} precioNeto = 0.00
     * 
     *@param id en PathVariable correspondiente al nro_vuelo
     *
     *@return ResponseEntity<EntityModel<VueloDTO>> con los datos del vuelo actualizado y links relacionados. 
     * @throws VueloException 
     **/
    @DeleteMapping("/vuelos/{id}")
    public ResponseEntity<EntityModel<VueloDTO>> cancelarVuelo(
            							@PathVariable("id") Long vueloId) throws VueloException {
        
        // Realizar soft delete (cambiar estado a CANCELADO)            
        VueloDTO vueloCanceladoDTO = vueloService.cancelarVuelo(vueloId);
        
        if(vueloCanceladoDTO.getNroVuelo() == null){
        	
        	throw new VueloException("El vuelo con numero: "+ vueloId +" no encontrado", HttpStatus.NOT_FOUND.value());
        	
        }else {
        	EntityModel<VueloDTO> vueloCanceladoEntity = EntityModel.of(vueloCanceladoDTO)
								        			.add(WebMvcLinkBuilder.linkTo(
									    					methodOn(VueloController.class)
									    					.getVueloById(vueloCanceladoDTO.getNroVuelo(), null))
									    					.withSelfRel())
													.add(Link.of("/vuelos/estado-vuelo?estado=CANCELADO","self"))
													.add(AppLinks.showVuelosLink());	    				

        	return ResponseEntity.ok().body(vueloCanceladoEntity);
        	
        }
    }
    
    private boolean isValidEstado(String estado) {
        
    	return estado.equalsIgnoreCase("registrado") ||
                estado.equalsIgnoreCase("reprogramado") ||
                estado.equalsIgnoreCase("demorado") ||
                estado.equalsIgnoreCase("cancelado");
    }
    
}
 