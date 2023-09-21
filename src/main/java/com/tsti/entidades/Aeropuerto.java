package com.tsti.entidades;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
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
	private Integer elevation;
	private BigDecimal lat;
	private BigDecimal lon;
	private String tz;
	
	public Aeropuerto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Aeropuerto(String icao, String iata, String name, String state, String country, Integer elevation,
			BigDecimal lat, BigDecimal lon, String tz) {
		super();
		this.icao = icao;
		this.iata = iata;
		this.name = name;
		this.state = state;
		this.country = country;
		this.elevation = elevation;
		this.lat = lat;
		this.lon = lon;
		this.tz = tz;
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

	public Integer getElevation() {
		return elevation;
	}

	public void setElevation(Integer elevation) {
		this.elevation = elevation;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}

	public BigDecimal getLon() {
		return lon;
	}

	public void setLon(BigDecimal lon) {
		this.lon = lon;
	}

	public String getTz() {
		return tz;
	}

	public void setTz(String tz) {
		this.tz = tz;
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
