/**
 * 
 */
package com.tsti.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import com.tsti.entidades.Aeropuerto;
import com.tsti.entidades.Ciudad;
import com.tsti.entidades.Vuelo;
import com.tsti.entidades.Vuelo.TipoVuelo;
import com.tsti.entidades.Vuelo.EstadoVuelo;
import com.tsti.serializer.CiudadSerializer;

/**
 * @author Bruno
 *
 */
public class VueloDTO {

	private Long nroVuelo;
	private String aerolinea;	
	private Aeropuerto destino;
	private LocalDate fechaPartida;
	private LocalTime horaPartida;
	private int nroFilasAsientos;
	private int nroColumnasAsientos;
	private BigDecimal precioNeto;
	private TipoVuelo tipoVuelo;
	private EstadoVuelo estadoVuelo;	
	
	public VueloDTO() {
		// TODO Auto-generated constructor stub
	}	

	
	public VueloDTO(Vuelo pojo) {
		super();
		this.nroVuelo = pojo.getNroVuelo();
		this.aerolinea = pojo.getAerolinea();
		this.destino = pojo.getDestino();
		this.fechaPartida = pojo.getFechaPartida();
		this.horaPartida = pojo.getHoraPartida();
		this.nroFilasAsientos = pojo.getNroFilas();
		this.nroColumnasAsientos = pojo.getNroColumnas();
		this.tipoVuelo = pojo.getTipoVuelo();
		this.estadoVuelo = pojo.getEstadoVuelo();
		this.precioNeto = pojo.getPrecioNeto(); 
	}

	public Long getNroVuelo() {
		return nroVuelo;
	}

	public void setNroVuelo(Long nroVuelo) {
		this.nroVuelo = nroVuelo;
	}

	public String getAerolinea() {
		return aerolinea;
	}

	public void setAerolinea(String aerolinea) {
		this.aerolinea = aerolinea;
	}

	public Aeropuerto getDestino() {
		return destino;
	}

	public void setDestino(Aeropuerto destino) {
		this.destino = destino;
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

	public int getNroFilas() {
		return getNroFilasAsientos();
	}


	public int getNroFilasAsientos() {
		return nroFilasAsientos;
	}

	public void setNroFilas(int nroFilas) {
		setNroFilasAsientos(nroFilas);
	}


	public void setNroFilasAsientos(int nroFilas) {
		this.nroFilasAsientos = nroFilas;
	}

	public int getNroColumnas() {
		return getNroColumnasAsientos();
	}


	public int getNroColumnasAsientos() {
		return nroColumnasAsientos;
	}

	public void setNroColumnas(int nroColumnas) {
		setNroColumnasAsientos(nroColumnas);
	}

	public void setNroColumnasAsientos(int nroColumnas) {
		this.nroColumnasAsientos = nroColumnas;
	}

	public BigDecimal getPrecioNeto() {
		return precioNeto;
	}

	public void setPrecioNeto(BigDecimal precioNeto) {
		this.precioNeto = precioNeto;
	}


	public TipoVuelo getTipoVuelo() {
		return tipoVuelo;
	}


	public void setTipoVuelo(TipoVuelo tipoVuelo) {
		this.tipoVuelo = tipoVuelo;
	}


	public EstadoVuelo getEstadoVuelo() {
		return estadoVuelo;
	}


	public void setEstadoVuelo(EstadoVuelo estadoVuelo) {
		this.estadoVuelo = estadoVuelo;
	}
	@Override
	public int hashCode() {
		return Objects.hash(aerolinea, destino, fechaPartida, horaPartida, nroVuelo);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) 
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VueloDTO other = (VueloDTO) obj;
		return Objects.equals(aerolinea, other.aerolinea) && Objects.equals(destino, other.destino)
				&& Objects.equals(fechaPartida, other.fechaPartida) && Objects.equals(horaPartida, other.horaPartida)
				&& Objects.equals(nroVuelo, other.nroVuelo);
	}
	
	
	
}
