package com.tsti.presentacion;

import java.time.LocalDate;

import com.tsti.entidades.Clientes;

import jakarta.validation.constraints.NotNull;

public class ClienteForm {
	@NotNull
	private long dni; 
	@NotNull
	private String nombre;
	@NotNull
	private String apellido;
	private String domicilio;
	private String email;
	private LocalDate fechaNacimiento;
	private long nroPasaporte;
	private LocalDate vencimientoPasaporte;
	private Long idCiudad;
	
	
	
	
	public ClienteForm() {
		super();
	}

	public Long getIdCiudad() {
		return idCiudad;
	}
	
	public void setIdCiudad(Long idCiudad) {
		this.idCiudad = idCiudad;
	}

	public long getDni() {
		return dni;
	}
	public void setDni(long dni) {
		this.dni = dni;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public long getNroPasaporte() {
		return nroPasaporte;
	}
	public void setNroPasaporte(long nroPasaporte) {
		this.nroPasaporte = nroPasaporte;
	}
	public LocalDate getVencimientoPasaporte() {
		return vencimientoPasaporte;
	}
	public void setVencimientoPasaporte(LocalDate vencimientoPasaporte) {
		this.vencimientoPasaporte = vencimientoPasaporte;
	}
	
	public Clientes toPojo() {
		Clientes c = new Clientes();
		c.setDni(this.getDni());
		c.setApellido(this.getApellido());
		c.setNombre(this.getNombre());
		c.setEmail(this.getEmail());
		c.setFechaNacimiento(this.getFechaNacimiento());
		c.setNroPasaporte(this.getNroPasaporte());
		return c;
	}
}
