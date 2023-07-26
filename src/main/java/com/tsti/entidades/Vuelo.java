package com.tsti.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
	@Column(name = "nro_vuelo")
	private Long nroVuelo;	
	@Column(name = "fecha_partida")
	@DateTimeFormat
	@NotNull
	private LocalDate fechaPartida;
	@Column(name = "hora_partida")	
	@NotNull
	private LocalTime horaPartida;	
	@NotNull	
	private String aerolinea;
	private String avion;
	@Transient
	private int nroFilas;
	@Transient
	private int nroColumnas;
	//@Transient
	//private Clientes plazas[][];
	@Column(name = "nro_asientos")
	@NotNull
	private int nroAsientos;	
	@Column(name = "tipo_vuelo")
	@NotNull
	private TipoVuelo tipoVuelo;
	private BigDecimal precioNeto;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "origen_id")
	private Ciudad origen;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "destino_id")	
	private Ciudad destino;//creada la entidad Ciudad		
	@JsonIgnore
	@OneToMany(mappedBy = "vuelo")
    private Set<Pasaje> pasajes;

	@NotNull
	private EstadoVuelo estadoVuelo; // (registrado / reprogramado / cancelado) lo mismo quiza, se debe agregar en la base las opciones					
						   			//Creado tipo ENUM para este caso. 
	
			
	//CONSTRUCTOR
	public Vuelo() {
		super();
	}
	
	//ENUM para estado de los vuelos. Se puede acceder por getEstadoVuelo(int estadoVuelo)
	public enum EstadoVuelo {
		REGISTRADO(0),
	    DEMORADO(1),
	    CANCELADO(2),
	    REPROGRAMADO(3),
	    TERMINADO(4);
		
		EstadoVuelo(int i) {
			
		}
	}
	
	//ENUM para tipo de vuelo. Se puede acceder por getTipoVuelo(int tipoVuelo)
	public enum TipoVuelo {
		NACIONAL,
		INTERNACIONAL
	}	
	
	//METODOS
	public Long getNroVuelo() {
		return nroVuelo;
	}

	public void setNroVuelo(Long nroVuelo ) {
		this.nroVuelo = nroVuelo ;

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
	
	public int getNroFilas() {
		return nroFilas;
	}


	public void setNroFilas(int nroFila) {
		this.nroFilas = nroFila;
	}

//	public Clientes[][] getPlazas() {
//		return plazas;
//	}
	public void setNroAsientos(int nroAsientos) {
		this.nroAsientos = nroAsientos;
	}
	
	public void setNroAsientos(int nroFilas, int nroColumnas) {
		this.nroAsientos = nroFilas * nroColumnas;
	}
	
	public int getNroAsientos() {
		return nroAsientos;
	}

	public int getNroColumnas() {
		return nroColumnas;
	}

	public void setNroColumnas(int nroColumnas) {
		this.nroColumnas = nroColumnas;
	}
	
//	public void setPlazas(Clientes[][] plazas) {
//		this.plazas = plazas;
//	}
	
	public TipoVuelo getTipoVuelo() {
		
		return this.tipoVuelo;
		
	}
	
	@Autowired //fuerzo a spring que utilice este metodo durante la IOC
	public void setTipoVuelo() {		
		String argentina = "Argentina";
		
		if(argentina.equalsIgnoreCase(this.destino.getPais())) {
			
			this.tipoVuelo = TipoVuelo.NACIONAL;
		}
		else {			
			this.tipoVuelo = TipoVuelo.INTERNACIONAL;			
		}		
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


	public EstadoVuelo getEstadoVuelo() {
		
		return this.estadoVuelo;
	}

	//ej de parametro: EstadoVuelo.CANCELADO
	public void setEstadoVuelo(EstadoVuelo estado) {
		this.estadoVuelo = estado;
	}
	
	public Set<Pasaje> getPasajeros(){
		
		return pasajes;
		
	}
	
	public void setPasajeros(HashSet<Pasaje> pasajes){
		this.pasajes = pasajes;	
		
	}
	
	public void addPasajero(Pasaje pasaje){
		
		this.pasajes.add(pasaje);		
		
	}

	public BigDecimal getPrecioNeto() {
		return precioNeto;
	}

	public void setPrecioNeto(BigDecimal precioNeto) {
		this.precioNeto = precioNeto.setScale(2);
	}

	@Override
	public String toString() {
		return "Vuelo [nroVuelo=" + nroVuelo + ", hora de partida= " + horaPartida + ", nroFila=" + nroFilas
				+ ", nroColumnas=" + nroColumnas + ", tipo_vuelo=" + tipoVuelo + ", Origen=" + origen + ", Destino="
				+ destino + ", Estado=" + estadoVuelo + "]";
	}

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
		Vuelo other = (Vuelo) obj;
		return Objects.equals(nroVuelo, other.nroVuelo);
	}

	public boolean asientoOcupado(Integer nroAsiento) {
		boolean r = false;
		Iterator<Pasaje> it = pasajes.iterator();
		while (it.hasNext() && r==false) {
			Pasaje i = it.next();
		    if(i.getNumeroAsiento() == nroAsiento) r = true;
		}
		return r;
	}
}