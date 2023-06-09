package com.tsti.entidades;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Establece/Actualiza/Retorna los datos de un vuelo
 * @author cecilia
 *
 */
@Entity
@Table(name = "vuelos")
public class Vuelo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	 
	@Column(name = "nro_vuelo", unique = true)
	@NotNull	
	private Long nroVuelo; // No podrá haber dos vuelos con el mismo nro
	@Column(name = "fecha_hora_partida")
	@NotNull
	private Date fechaHoraPartida;// ver tema fechas y aca iria con hora
	
	@Column(name = "nro_fila")
	@NotNull
	private Integer nroFila;
	@Column(name = "nro_asiento")
	@NotNull
	private Integer nroAsiento;
	@Column(name = "tipo_vuelo")
	@NotNull
	private String tipoVuelo;
	@ManyToOne
	@JoinColumn(name = "origen_id")
	private Ciudad origen;
	@ManyToOne
	@JoinColumn(name = "destino_id")	
	private Ciudad destino;// creada la entidad Ciudad	
	@ManyToMany(mappedBy = "vuelos") //linkeamos al HashSet vuelos de Clientes	
	private Set<Clientes> pasajeros = new HashSet<>();
	/* 
	 * El estado es autocalculado por el sistema, no puede ser establecido por
		el usuario.
		No podrá haber dos vuelos con el mismo nro.
		Una vez registrado, solo se permitirá cambiar la fecha y hora 
		del mismo(lo cual pasa el vuelo al estado reprogramado) 
		o eliminar el mismo (lo cual pasa el vuelo al estado cancelado).
		Tanto la reprogramación como la cancelación de un vuelo dispararía la
		notificación automática del evento a todos los pasajeros aunque por 
		simplicidad, no se pide implementar el servicio de alertas. */
	@NotNull
	private String estado; // (registrado / reprogramado / cancelado) lo mismo quiza, se debe agregar en la base las opciones					
						   //Obs: Ver tipo ENUM para este caso. 	
	//CONSTRUCTOR
	public Vuelo() {
		super();
	}

	//METODOS
	public long getNroVuelo() {
		return nroVuelo;
	}


	public void setNroVuelo(long nroVuelo) {
		this.nroVuelo = nroVuelo;
	}


	public Date getFechaHoraPartida() {
		return fechaHoraPartida;
	}


	public void setFechaHoraPartida(Date fecha_HoraVuelo) {
		this.fechaHoraPartida = fecha_HoraVuelo;
	}


	public int getNroFila() {
		return nroFila;
	}


	public void setNroFila(int nroFila) {
		this.nroFila = nroFila;
	}


	public int getNroAsiento() {
		return nroAsiento;
	}


	public void setNroAsiento(int nroAsiento) {
		this.nroAsiento = nroAsiento;
	}


	public String getTipoVuelo() {
		return tipoVuelo;
	}


	public void setTipoVuelo(String tipoVuelo) {
		this.tipoVuelo = tipoVuelo;
	}


	public Ciudad getOrigen() {
		return origen;
	}


	public void setOrigen(Ciudad origen) {
		this.origen = origen;
	}


	public Ciudad getDestino() {
		return destino;
	}


	public void setDestino(Ciudad destino) {
		this.destino = destino;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public Set<Clientes>getPasajeros(){
		
		return pasajeros;
		
	}

	@Override
	public String toString() {
		return "Vuelo [nroVuelo=" + nroVuelo + ", fecha_HoraVuelo=" + fechaHoraPartida + ", nroFila=" + nroFila
				+ ", nroAsiento=" + nroAsiento + ", tipoVuelo=" + tipoVuelo + ", Origen=" + origen + ", Destino="
				+ destino + ", Estado=" + estado + "]";
	}	
	
}
