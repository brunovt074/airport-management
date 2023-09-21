package com.tsti.entidades;

import java.math.BigDecimal;
import java.util.Objects;

public class Aeropuerto {

	private String icao;
	private String iata;
	private String name;
	private String state;
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
