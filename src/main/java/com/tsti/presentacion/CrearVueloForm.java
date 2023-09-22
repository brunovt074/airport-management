/**
 * 
 */
package com.tsti.presentacion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import com.tsti.entidades.Aeropuerto;
import com.tsti.entidades.Ciudad;
import com.tsti.entidades.Vuelo;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * @author Bruno
 *
 *Clase que representa un formulario de entrada para crear y editar vuelos
 */
@Validated
public class CrearVueloForm {
	
	// Atributos de vuelo
	//nroVuelo es nulo porque lo asigna la BD de forma autoincremental
	private Long nroVuelo;
	@NotBlank(message = "El campo aerolinea no puede estar vacio")
	private String aerolinea;
	private String avion;
	@NotNull(message = "El campo fecha no puede estar vacio")
	@FutureOrPresent(message = "Inserte una fecha a partir de hoy en adelante")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate fechaPartida;
	@NotNull(message = "El campo hora no puede estar vacio")
	@DateTimeFormat
	private LocalTime horaPartida;
	@NotNull(message = "El número de asientos no puede estar vacío")
	@Min(value = 1, message = "El número de filas de asientos debe ser mayor a cero")
	private Integer nroAsientos;
	@NotNull (message = "El campo precio no puede estar vacio")
	@DecimalMin(value = "0.00", inclusive = true, message = "El precio debe ser mayor a 0.00")
	@Digits(integer = Integer.MAX_VALUE, fraction=2)
	private BigDecimal precioNeto;
	
	//Atributos de ciudad
	//se buscara idDestino si es nulo se creara la ciudad
	private Long idDestino; 
	private String codAeropuerto;
    private String nombreCiudad;
    private String provincia;
    private String pais;
    private String codPostal;
	
    //Constructor de vuelo.
    public CrearVueloForm() {
		// TODO Auto-generated constructor stub
	}   
    
    public Vuelo toPojo(){
		Vuelo vuelo = new Vuelo();		
		
		vuelo.setNroVuelo(this.getNroVuelo());
		vuelo.setAerolinea(this.getAerolinea());
		vuelo.setAvion(this.getAvion());
		vuelo.setFechaPartida(this.getFechaPartida());
		vuelo.setHoraPartida(this.getHoraPartida());
		vuelo.setNroAsientos(this.getNroAsientos());				
		vuelo.setTipoVuelo();
		vuelo.setPrecioNeto(this.getPrecioNeto());
		
		return vuelo;
	}
	
	public Vuelo toPojoConCiudad(Aeropuerto origen, Aeropuerto destino){
		Vuelo vuelo = new Vuelo();
		
		vuelo.setNroVuelo(this.getNroVuelo());
		vuelo.setAerolinea(this.getAerolinea());
		vuelo.setAvion(this.getAvion());
		vuelo.setFechaPartida(this.getFechaPartida());
		vuelo.setHoraPartida(this.getHoraPartida());
		vuelo.setNroAsientos(this.getNroAsientos());
		vuelo.setOrigen(origen);
		vuelo.setDestino(destino);
		vuelo.setTipoVuelo();
		vuelo.setPrecioNeto(this.getPrecioNeto());
		
		return vuelo;

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
	
	public String getAvion() {
		return avion;
	}

	public void setAvion(String avion) {
		this.avion = avion;
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

	public int getNroAsientos() {
		return nroAsientos;
	}

	public void setNroAsientos(int nroAsientos) {
		this.nroAsientos = nroAsientos;
	}


	public BigDecimal getPrecioNeto() {
		return precioNeto;
	}

	public void setPrecioNeto(BigDecimal precioNeto) {
		this.precioNeto = precioNeto;
	}

	public Long getIdDestino() {
		return idDestino;
	}

	public void setIdDestino(Long idDestino) {
		this.idDestino = idDestino;
	}

	public String getCodAeropuerto() {
		return codAeropuerto;
	}

	public void setCodAeropuerto(String codAeropuerto) {
		this.codAeropuerto = codAeropuerto;
	}

	public String getNombreCiudad() {
		return nombreCiudad;
	}

	public void setNombreCiudad(String nombreCiudad) {
		this.nombreCiudad = nombreCiudad;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getCodPostal() {
		return codPostal;
	}

	public void setCodPostal(String codPostal) {
		this.codPostal = codPostal;
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(aerolinea, fechaPartida, horaPartida, precioNeto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CrearVueloForm other = (CrearVueloForm) obj;
		return Objects.equals(aerolinea, other.aerolinea) && Objects.equals(codAeropuerto, other.codAeropuerto)
				&& Objects.equals(nombreCiudad, other.nombreCiudad)
				&& Objects.equals(provincia, other.provincia)
				&& Objects.equals(pais, other.pais)
				&& Objects.equals(fechaPartida, other.fechaPartida) && Objects.equals(horaPartida, other.horaPartida)
				&& Objects.equals(precioNeto, other.precioNeto);
	}

	@Override
	public String toString() {
		return "CrearVueloForm [nroVuelo=" + nroVuelo + ", aerolinea=" + aerolinea + ", destino=" + nombreCiudad
				+ ", fechaPartida=" + fechaPartida + ", horaPartida=" + horaPartida + ", nroAsientos="
				+ nroAsientos + ", precioNeto=" + precioNeto
				+ "]";
	}

	
	
	
}