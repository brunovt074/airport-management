package com.tsti.servicios;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.tsti.dto.TasaAeroportuariaDTO;
import com.tsti.entidades.Vuelo.TipoVuelo;

/**
 * @author Bruno
 *
 */
@Service
public class TasaServiceImpl {
	
	private BigDecimal tasa;
	private String moneda;
	
public BigDecimal getTasa(TipoVuelo tipoVuelo) {
	 		
		if(tipoVuelo == TipoVuelo.NACIONAL) {
					
			tasa = new BigDecimal("1100.00");			
			
		} else {						
			tasa = new BigDecimal("60.00");
						
		}
		
		return tasa;
	}
	
	
	public TasaAeroportuariaDTO getTasaDTO(TipoVuelo tipoVuelo) {
		
		TasaAeroportuariaDTO tasaDTO = new TasaAeroportuariaDTO();   
		
		if(tipoVuelo == TipoVuelo.NACIONAL) {
			tasa = new BigDecimal("1100.00").setScale(2);
			moneda = "AR$";
			
			tasaDTO.setTasa(tasa);
			tasaDTO.setMoneda(moneda);					
			
		} else {						
			tasa = new BigDecimal("60.00").setScale(2);
			moneda = "US$";
			
			tasaDTO.setTasa(tasa);
			tasaDTO.setMoneda(moneda);	
						
		}
		
		return tasaDTO;
		
	}
	
}
