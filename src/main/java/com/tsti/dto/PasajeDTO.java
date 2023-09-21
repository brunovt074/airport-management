package com.tsti.dto;

import java.math.BigDecimal;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tsti.entidades.Clientes;
import com.tsti.entidades.Pasaje;
import com.tsti.entidades.Vuelo;
import com.tsti.entidades.Vuelo.TipoVuelo;

/**
 * @author bruno
 *
 */
public class PasajeDTO extends RepresentationModel<PasajeDTO> {
	@JsonProperty
	private Long id;
	@JsonProperty
	@JsonIgnore
	private Clientes pasajero;
	@JsonIgnore
	@JsonProperty
	private Vuelo vuelo;
	@JsonIgnore
	@JsonProperty
	private Long nroVuelo;
	private Long dni;
    private Integer numeroAsiento;
	private BigDecimal precioNeto;
	private BigDecimal tasa;
	private BigDecimal precioFinal;
	private TipoVuelo tipoVuelo;
	
	
	//CONSTRUCTORES
	public PasajeDTO() {
		super();
	}
	public PasajeDTO(Pasaje p) {
		super();
		this.vuelo = p.getVuelo();
		this.pasajero = p.getPasajero();
		this.nroVuelo = this.vuelo.getNroVuelo();
		this.numeroAsiento=p.getNumeroAsiento();
		this.dni = this.pasajero.getDni();
	}
	public PasajeDTO(Vuelo vuelo, Clientes pasajero, BigDecimal precio, TipoVuelo tipoVuelo) {
		super();
		this.vuelo = vuelo;
		this.pasajero = pasajero;
		this.nroVuelo = this.vuelo.getNroVuelo();
		this.dni = this.pasajero.getDni();
		this.precioNeto = precio;
		this.tipoVuelo = tipoVuelo;				
	}
		
	public PasajeDTO(Long nroVuelo, Long dni, BigDecimal precioNeto, BigDecimal tasa, BigDecimal precioFinal,
							TipoVuelo tipoVuelo) {
		super();
		this.nroVuelo = nroVuelo;
		this.dni = dni;		
		this.precioNeto = precioNeto;
		this.tasa = tasa;
		this.tipoVuelo = tipoVuelo;
		this.precioFinal = precioFinal;
	}
	//GETTERS
	public Long getNroVuelo() {
		return nroVuelo;
	}
	public Long getDni() {
		return dni;
	}
	public BigDecimal getPrecioNeto() {
		return precioNeto;
	}
	public BigDecimal getPrecioFinal() {
		return precioFinal;
	}
	public TipoVuelo getTipoVuelo() {
		return tipoVuelo;
	}
	public BigDecimal getTasa() {
		return tasa;
	}
	public Clientes getPasajero() {
		return pasajero;
	}
	public Vuelo getVuelo() {
		return vuelo;
	}
	
	//SETTERS
	public void setNroVuelo(Long nroVuelo) {
		this.nroVuelo = nroVuelo;
	}
	public void setDni(Long dni) {
		this.dni = dni;
	}
	public void setPrecioNeto(BigDecimal precio) {
		this.precioNeto = precio;
	}
	public void setPrecioFinal(BigDecimal precioFinal) {
		this.precioFinal = precioFinal;
	}
	public void setTipoVuelo(TipoVuelo tipoVuelo) {
		this.tipoVuelo = tipoVuelo;
	}
	public void setTasa(BigDecimal tasa) {
		this.tasa = tasa;
	}
	public void setPasajero(Clientes pasajero) {
		this.pasajero = pasajero;
	}
	public void setVuelo(Vuelo vuelo) {
		this.vuelo = vuelo;
	}
	public void setId(Long id) {
		this.id=id;
	}
	public Object getId() {
		return this.id;
	}
}
