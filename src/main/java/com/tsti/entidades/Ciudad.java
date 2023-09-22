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
@Table(name = "ciudades")
public class Ciudad implements Comparable<Ciudad>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	//COD de aeropuerto por ej EZE (ezeiza)
	@Column(name="cod_aeropuerto")	
	private String codAeropuerto;	
	@NotNull
	@Column(name="nombre_ciudad")
	private String nombreCiudad;	
	@NotNull	
	private String provincia;	
	@NotNull
	private String pais;	
	@NotNull
	@Column(name="cod_postal")
	private String codPostal;	
	//Una ciudad puede tener muchos domicilios
	//!!SI SE ELIMINA UNA CIUDAD HABRA UN ERROR CON LOS DOMICILIOS ASOCIADOS.
	//1-OMITIR QUE SE PUEDA ELIMINAR UNA CIUDAD.
	//2-EL MANEJO DE ESTO DEBERIA HACERSE CON LOGICA A LA HORA DE TAL ELIMINACION
	@OneToMany(mappedBy = "ciudad")
    private List<Domicilio> domicilios;	
	//Una ciudad puede ser el origen de muchos vuelos
//	@OneToMany(mappedBy = "origen")
//    private List<Vuelo> vuelosOrigen;	
//	//Una ciudad puede ser el destino de muchos vuelos
//	@OneToMany(mappedBy = "destino")
//    private List<Vuelo> vuelosDestino;
	
	//CONSTRUCTOR
	public Ciudad() {
		super();		
	}
	
	//METODOS
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getcodAeropuerto() {
		return codAeropuerto;
	}
	public void setcodAeropuerto(String codCiudad) {
		this.codAeropuerto = codCiudad;
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

	public List<Domicilio> getDomicilios() {
		return domicilios;
	}

	public void setDomicilios(List<Domicilio> domicilios) {
		this.domicilios = domicilios;
	}

//	public List<Vuelo> getVuelosOrigen() {
//		return vuelosOrigen;
//	}
//
//	public void setVuelosOrigen(List<Vuelo> vuelosOrigen) {
//		this.vuelosOrigen = vuelosOrigen;
//	}
//
//	public List<Vuelo> getVuelosDestino() {
//		return vuelosDestino;
//	}
//
//	public void setVuelosDestino(List<Vuelo> vuelosDestino) {
//		this.vuelosDestino = vuelosDestino;
//	}

	@Override
	public String toString() {
		return "ID = "+ id + "Ciudad [codAeropuerto=" + codAeropuerto + ", nombreCiudad=" + nombreCiudad + ", provincia=" + provincia
				+ ", pais=" + pais + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(codAeropuerto, nombreCiudad, pais, provincia);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ciudad other = (Ciudad) obj;
		return Objects.equals(codAeropuerto, other.codAeropuerto) && Objects.equals(nombreCiudad, other.nombreCiudad)
				&& Objects.equals(pais, other.pais) && Objects.equals(provincia, other.provincia);
	}

	@Override
	public int compareTo(Ciudad c) {
		int compararCiudad = this.nombreCiudad.compareTo(c.nombreCiudad);
		
		if(compararCiudad == 0)
			return this.pais.compareTo(c.pais);
		
		else
			return compararCiudad;		
	}	
	
}