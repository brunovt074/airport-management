package com.tsti.excepcion;

public class SistemaGestionComercialAeropuertoException extends Exception{
	
	private static final long serialVersionUID = 3941221036411842318L;
	
	private String mensaje;
	
	private int statusCode;	
	
	public SistemaGestionComercialAeropuertoException() {
		super();		
	}
	
	public SistemaGestionComercialAeropuertoException(String message, Throwable cause, boolean enableSuppression,
												boolean writableStackTrace) {
		
		super(message, cause, enableSuppression, writableStackTrace);
		
	}



	public SistemaGestionComercialAeropuertoException(String message, Throwable cause) {
		
		super(message, cause);		
	
	}



	public SistemaGestionComercialAeropuertoException(String message) {
		
		super(message);
		
	}



	public SistemaGestionComercialAeropuertoException(Throwable cause) {
		
		super(cause);
		
	}
	
	public SistemaGestionComercialAeropuertoException(String mensaje, int statusCode) {
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
	public String toString() {
		return "SistemaGestionAeropuertoException [mensaje=" + mensaje + ", statusCode=" + statusCode + "]";
	}
}