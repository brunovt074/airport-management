package com.tsti.excepcion;

/**
 * 
 * @author cecilia
 *
 */

public class PasajeroException extends SistemaGestionComercialAeropuertoException{

	private static final long serialVersionUID = 3941221036411842318L;

	private String mensaje;
	
	private int statusCode;

	public PasajeroException() {
		super();
		
	}

	public PasajeroException(String mensaje,int statusCode) {
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

	@Override
	public String getMessage() {
		return mensaje;
	}
	
	
}
