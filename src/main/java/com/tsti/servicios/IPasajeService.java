package com.tsti.servicios;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tsti.entidades.Clientes;
import com.tsti.entidades.Pasaje;
import com.tsti.entidades.Vuelo;
import com.tsti.excepcion.ValidacionFallidaEnPasajeException;
/**
 * @author bruno
 *
 */

@Service
public interface IPasajeService {
    Pasaje crearPasaje(Vuelo vuelo, Clientes pasajero, Integer nroAsiento) throws ValidacionFallidaEnPasajeException;
	Optional<Pasaje> findById(Long id);
}
