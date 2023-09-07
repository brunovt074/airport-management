package com.tsti.presentacion;

public class PasajeForm {
    private Long vueloId;
    private Long clienteId;
    private Integer numeroAsiento;


    //CONSTRUCTORES
    public PasajeForm() {
    }
    
    public PasajeForm(Long vueloId, Long clienteId, Integer numeroAsiento) {
        this.vueloId = vueloId;
        this.clienteId = clienteId;
        this.numeroAsiento = numeroAsiento;
    }

    
    //GETTERS
    public Long getVueloId() {
        return vueloId;
    }
    public Long getClienteId() {
        return clienteId;
    }
	public Integer getNumeroAsiento() {
		return this.numeroAsiento;
	}	
	
	//	SETTERS
    public void setVueloId(Long vueloId) {
        this.vueloId = vueloId;
    }
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    public void setNumeroAsiento(Integer numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }
	
}