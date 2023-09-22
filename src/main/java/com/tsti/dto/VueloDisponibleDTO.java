
package com.tsti.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.tsti.entidades.Aeropuerto;
import com.tsti.entidades.Vuelo;
import com.tsti.entidades.Vuelo.TipoVuelo;

/**
 * @author Bruno
 *
 */
public class VueloDisponibleDTO {
	private Long nroVuelo;
	private String aerolinea;
	private Aeropuerto destino;
	private TipoVuelo tipoVuelo;
	private LocalDate fechaPartida;
	private LocalTime horaPartida;	
	
	
	public VueloDisponibleDTO (Vuelo pojo, int tipoVuelo){
		
		super();
		this.nroVuelo = pojo.getNroVuelo();
		this.aerolinea = pojo.getAerolinea();
		this.destino = pojo.getDestino();
		this.tipoVuelo = pojo.getTipoVuelo();
		this.fechaPartida = pojo.getFechaPartida();
		this.horaPartida = pojo.getHoraPartida();
		
	}

	public Long getNroVuelo() {
		return nroVuelo;
	}
	
	public String getAerolinea() {
		return aerolinea;
	}

	public void setAerolinea(String aerolinea) {
		this.aerolinea = aerolinea;
	}

	public void setNroVuelo(Long nroVuelo) {
		this.nroVuelo = nroVuelo;
	}

	public LocalDate getFechaPartida() {
		return fechaPartida;
	}

	public void setFechaPartida(LocalDate fechaPartida) {
		this.fechaPartida = fechaPartida;
	}

	public LocalTime getHoraPartida() {
		return horaPartida;
	}

	public void setHoraPartida(LocalTime horaPartida) {
		this.horaPartida = horaPartida;
	}

	public Aeropuerto getDestino() {
		return destino;
	}

	public void setDestino(Aeropuerto destino) {
		this.destino = destino;
	}

	public TipoVuelo getTipoVuelo() {
		return tipoVuelo;
	}

	public void setTipoVuelo(TipoVuelo tipoVuelo) {
		this.tipoVuelo = tipoVuelo;
	}

	@Override
	public String toString() {
		return "VueloDisponibleDTO [nroVuelo=" + nroVuelo + ", fechaPartida=" + fechaPartida + ", horaPartida="
				+ horaPartida + ", destino=" + destino + "]";
	}
	
	
}
