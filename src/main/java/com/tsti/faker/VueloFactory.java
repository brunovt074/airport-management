package com.tsti.faker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tsti.dao.CiudadDAO;
import com.tsti.dao.VueloDAO;
import com.tsti.entidades.Vuelo;
import com.tsti.entidades.Vuelo.EstadoVuelo;
import com.tsti.entidades.Vuelo.TipoVuelo;
import com.tsti.excepcion.SistemaGestionComercialAeropuertoException;
import com.tsti.servicios.AeropuertoServiceImpl;
import com.tsti.entidades.Aeropuerto;

import net.datafaker.Faker;

/**
 * @author Bruno
 *
 */
@Component
public class VueloFactory {
	
	private final AeropuertoServiceImpl aeropuertoService;
	@Autowired
	private VueloDAO vueloDAO;
	private Faker faker;
	private Aeropuerto origen;
	private Aeropuerto destino;	
	
	@Autowired
	public VueloFactory(CiudadDAO ciudadDAO, AeropuertoServiceImpl aeropuertoService) {
		this.aeropuertoService = aeropuertoService;
		this.faker = new Faker(new Locale("es") );				
	}
	
	public void crearVueloPorDTO(){
		
	}	
	
	public void crearVueloOrigenLocal(EstadoVuelo estadoVuelo, TipoVuelo tipoVuelo) {
		Vuelo vuelo = new Vuelo();			
		
		try {
			origen = aeropuertoService.getAeropuertoLocal();
		
		} catch (SistemaGestionComercialAeropuertoException e) {
			System.out.print(e.getMensaje());			
		}
				
		if(tipoVuelo.equals(TipoVuelo.NACIONAL)) {
						
			destino = aeropuertoService.getAeropuertoArgentinoAleatorio();
			vuelo.setPrecioNeto(GenerarPrecioNeto.generarPrecioNetoPesos());
		
		}else {
			destino = aeropuertoService.getAeropuertoExtranjeroAleatorio();
			vuelo.setPrecioNeto(GenerarPrecioNeto.generarPrecioNetoDolares());
		}
		
		aeropuertoService.save(origen);
		aeropuertoService.save(destino);
				
		//Metodo mejorado para obtener fecha y hora en un array y evitar repetir codigo.
		//1er parametro dias de partida hacia adelante, 2do: horas +
		Object[] fechaHoraPartida = fechaHora(10,24);
				
		vuelo.setAerolinea(faker.aviation().airline());
		vuelo.setAvion(faker.aviation().airplane());
//		vuelo.setNroFilas(6);
//		vuelo.setNroColumnas(15);
		//Clientes [][] plazas  = new Clientes[vuelo.getNroFilas()][vuelo.getNroColumnas()];
		//vuelo.setPlazas(plazas);
		vuelo.setNroAsientos(90);
		vuelo.setOrigen(origen);
		vuelo.setDestino(destino);
		vuelo.setTipoVuelo();
		vuelo.setEstadoVuelo(estadoVuelo);
		vuelo.setFechaPartida((LocalDate) fechaHoraPartida[0]);
		vuelo.setHoraPartida((LocalTime) fechaHoraPartida[1]);
			
		
		System.out.println(vuelo.toString());				
		
		vueloDAO.save(vuelo);
		
	}
	

	private Object[] fechaHora(int diasFechaPartida, int horasPartida) {
		
		Object[] fechaHoraPartida = new Object[2];		
		
		//Obtener fecha.
		LocalDateTime fechaPartidaTimestamp = (faker.date().future(diasFechaPartida,0,TimeUnit.DAYS)).toLocalDateTime();
		LocalDate fechaPartida = fechaPartidaTimestamp.toLocalDate();
		//Obtener hora.
		LocalDateTime horaPartidaTimestamp = (faker.date().future(horasPartida,0,TimeUnit.HOURS)).toLocalDateTime();
		LocalTime horaPartida = horaPartidaTimestamp.toLocalTime().truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
		
		fechaHoraPartida[0] = fechaPartida; //Elemento de tipo LocalDate
		fechaHoraPartida[1] = horaPartida; //Elemento de tipo LocalTime
		
		return fechaHoraPartida;
		
	}

	public void crearVueloOrigenLocal(int nroPasajeros, EstadoVuelo estado, TipoVuelo tipo) {
		
		crearVueloOrigenLocal(estado, tipo);
		
		//logica para cargar pasajeros
	}
}
	
//	private void cargarPasajeros(Vuelo vuelo, ClienteDAO clienteDAO, CiudadDAO ciudadDAO, DomicilioDAO domicilioDAO, int nroPasajeros) {
//		int asientosDisponibles = vuelo.getNroAsientos();
//				
//		if(nroPasajeros <= asientosDisponibles) {
//			
//			if(vuelo.getTipoVuelo().equals(TipoVuelo.NACIONAL)) {
//				for (int i = 0; i < nroPasajeros; i++) {
//					//Pasaje pasaje = new Pasaje();
//					
//					//Clientes pasajero = clienteFactory.getUnPasajeroNacional(ciudadDAO, domicilioDAO);
////					vuelo.addPasajero(pasajero);
//					asientosDisponibles--;
//					
//					//System.out.println(pasajero.toString());
//					//System.out.println(vuelo.getPasajeros().toString());
//					System.out.println(vuelo.toString());		
//				}
//			}else{
//				for (int i = 0; i < nroPasajeros; i++) {
//					//Clientes pasajero = clienteFactory.getUnPasajeroInternacional(ciudadDAO, domicilioDAO);
////					vuelo.addPasajero(pasajero);
//					asientosDisponibles--;
//					
//					//System.out.println(pasajero.toString());
//					//System.out.println(vuelo.getPasajeros().toString());
//					System.out.println(vuelo.toString());	
//				}
//			}						
//		}		
//	}
//}
