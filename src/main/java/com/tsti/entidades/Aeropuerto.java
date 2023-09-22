package com.tsti.entidades;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * @author Bruno
 *
 */
@Entity
@Table(name = "aeropuertos")
public class Aeropuerto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "icao", unique = true)
	private String icao;
	private String iata;
	@NotNull	
	private String name;
	@NotNull	
	private String city;
	@NotNull	
	private String state;
	@NotNull	
	private String country;
	private long elevation;
	private double lat;
	private double lon;
	private String tz;
	@OneToMany(mappedBy = "origen")
    private List<Vuelo> vuelosOrigen;	
	@OneToMany(mappedBy = "destino")
    private List<Vuelo> vuelosDestino;
	
	
	public Aeropuerto() {
		super();		
	}

	public Aeropuerto(String icao, String iata, String name,String city, String state, String country, long elevation,
			double lat, double lon, String tz) {
		super();
		this.icao = icao;
		this.iata = iata;
		this.name = name;
		this.city = city;
		this.state = state;
		this.country = country;
		this.elevation = elevation;
		this.lat = lat;
		this.lon = lon;
		this.tz = tz;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIcao() {
		return icao;
	}

	public void setIcao(String icao) {
		this.icao = icao;
	}

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public long getElevation() {
		return elevation;
	}
	
	public void setElevation(long elevation) {
		this.elevation = elevation;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getTz() {
		return tz;
	}

	public void setTz(String tz) {
		this.tz = tz;
	}
	
	public List<Vuelo> getVuelosOrigen() {
		return vuelosOrigen;
	}

	public void setVuelosOrigen(List<Vuelo> vuelosOrigen) {
		this.vuelosOrigen = vuelosOrigen;
	}

	public List<Vuelo> getVuelosDestino() {
		return vuelosDestino;
	}

	public void setVuelosDestino(List<Vuelo> vuelosDestino) {
		this.vuelosDestino = vuelosDestino;
	}	

	@Override
	public int hashCode() {
		return Objects.hash(icao);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aeropuerto other = (Aeropuerto) obj;
		return Objects.equals(icao, other.icao);
	}

	@Override
	public String toString() {
		return "Aeropuerto [icao=" + icao + ", iata=" + iata + ", name=" + name + ", state=" + state + ", country="
				+ country + ", elevation=" + elevation + ", lat=" + lat + ", lon=" + lon + ", tz=" + tz + "]";
	}
	
}
