package com.tsti.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.tsti.dao.ClienteDAO;
import com.tsti.dao.VueloDAO;
import com.tsti.dto.ClienteResponseDTO;
import com.tsti.dto.PasajeResDTO;
import com.tsti.dto.VueloDTO;
import com.tsti.entidades.Clientes;
import com.tsti.entidades.Pasaje;
import com.tsti.entidades.Vuelo;
import com.tsti.presentacion.PasajeForm;
import com.tsti.servicios.PasajeServiceImpl;

import jakarta.validation.Valid;

@RestController
public class PasajeController {

	private final PasajeServiceImpl pasajeServiceImpl;
	@Autowired
	private VueloDAO vueloDAO;
	@Autowired
	private ClienteDAO clienteDAO;

    public PasajeController(PasajeServiceImpl pasajeServiceImpl) {
        this.pasajeServiceImpl = pasajeServiceImpl;
    }
    
    //    RECIBE UN FORMULARIO CON LOS DATOS NECESARIOS, GENERA EL PASAJE Y RETORNA UN MENSAJE CON LOS DATOS
    //	  LA URL ES 'http://localhost:8081/pasajes' SE DEBE USAR POST
    @PostMapping("/pasajes")
    public ResponseEntity<?> crearPasaje(@Valid @RequestBody PasajeForm pasajeForm) {
        try {
            Vuelo vuelo = vueloDAO.findById(pasajeForm.getVueloId())
                    .orElseThrow(() -> new RuntimeException("Vuelo no encontrado"));
            Clientes cliente = clienteDAO.findById(pasajeForm.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            Pasaje pasaje = pasajeServiceImpl.crearPasaje(vuelo, cliente, pasajeForm.getNumeroAsiento());
            return ResponseEntity.status(HttpStatus.CREATED).body(pasaje);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }
    //curl --location --request GET 'http://localhost:8081/pasajes/1' 
    @GetMapping("/pasajes/{id}")
    public ResponseEntity<EntityModel<PasajeResDTO>> getPasajeById(@PathVariable Long id) {
        try {
            Optional<Pasaje> pasajeOptional = pasajeServiceImpl.findById(id);

            if (pasajeOptional.isPresent()) {
                Pasaje pasaje = pasajeOptional.get();

                ClienteResponseDTO clienteDTO = new ClienteResponseDTO(pasaje.getPasajero());
                VueloDTO vueloDTO = new VueloDTO(pasaje.getVuelo());
                PasajeResDTO pasajeResDTO = new PasajeResDTO(pasaje.getId(), clienteDTO, vueloDTO, pasaje.getNumeroAsiento());
                
                EntityModel<PasajeResDTO> entityModel = EntityModel.of(pasajeResDTO);

                Link selfLink = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PasajeController.class).getPasajeById(id))
                        .withSelfRel();
                entityModel.add(selfLink);

                Link vueloLink = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(VueloController.class).getVueloById(pasaje.getVuelo().getNroVuelo(), null))
                        .withRel("vuelo");
                entityModel.add(vueloLink);

                Link pasajeroLink = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(ClienteController.class).getById(pasaje.getPasajero().getId()))
                        .withRel("pasajero");
                entityModel.add(pasajeroLink);

                return ResponseEntity.ok(entityModel);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}