/**
 * 
 */
package com.tsti.presentacion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import com.tsti.entidades.Vuelo;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

/**
 * @author Bruno
 *
 */
public class EditarVueloForm {
	
	//@NotNull
	private Long nroVuelo;
	@NotNull
	private LocalDate fechaPartida;
	@NotNull
	private LocalTime horaPartida;
		
	public EditarVueloForm() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EditarVueloForm(Vuelo pojo) {
		super();
		this.nroVuelo = pojo.getNroVuelo();
		this.fechaPartida = pojo.getFechaPartida();
		this.horaPartida = pojo.getHoraPartida();				
	}
	
	
	
	public EditarVueloForm(Long nroVuelo, LocalDate fechaPartida, LocalTime horaPartida) {
		this.nroVuelo = nroVuelo;
		this.fechaPartida = fechaPartida;
		this.horaPartida = horaPartida;
	}

	public Long getNroVuelo() {
		return nroVuelo;
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
	
//	@AssertTrue(message = "Debe especificar la fecha de partida o la hora de partida")
//    public boolean isFechaPartidaOrHoraPartidaPresent() {
//        return fechaPartida != null || horaPartida != null;
//    }

	
	@Override
	public int hashCode() {
		return Objects.hash(nroVuelo);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EditarVueloForm other = (EditarVueloForm) obj;
		return Objects.equals(nroVuelo, other.nroVuelo);
	}
	@Override
	public String toString() {
		return "EditarVueloForm [nroVuelo=" + nroVuelo + ", fechaPartida=" + fechaPartida + ", horaPartida="
				+ horaPartida + "]";
	}
	
	
}
