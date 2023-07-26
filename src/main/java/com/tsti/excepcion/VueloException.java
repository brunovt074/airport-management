/**
 * 
 */
package com.tsti.excepcion;

import org.springframework.http.HttpStatusCode;

/**
 * @author Bruno
 *
 */
public class VueloException extends SistemaGestionComercialAeropuertoException{
	
	private static final long serialVersionUID = 6285496079638261754L;

	private String mensaje;
	private int statusCode;	

	public VueloException() {
		super();
		
	}

	public VueloException (String mensaje,HttpStatusCode statusCode) {
		super();
		this.mensaje = mensaje;		
	}
	
	public VueloException (String mensaje,int statusCode) {
		super();
		this.mensaje = mensaje;
		this.statusCode = statusCode;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String getMessage() {
		return mensaje;
	}	

}

