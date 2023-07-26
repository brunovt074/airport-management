/**
 * 
 */
package com.tsti.servicios;

import java.math.BigDecimal;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.tsti.dto.CotizacionDolarDTO;
import com.tsti.excepcion.CostoPasajeException;

/**
 * @author Bruno
 *
 */
@Service
public class CotizacionServiceImpl {

	private static final String URL_COTIZACION_DOLAR = "https://www.dolarsi.com/api/api.php?type=valoresprincipales";
	private final RestTemplate restTemplate;	
	
    public CotizacionServiceImpl() {
		this.restTemplate = new RestTemplate();        
    }	
    
	public BigDecimal getCotizacionDolarOficial() throws CostoPasajeException{
		
		//try {
			
			ResponseEntity<CotizacionDolarDTO[]> response = restTemplate.getForEntity(URL_COTIZACION_DOLAR, CotizacionDolarDTO[].class);
			

			if(response.getStatusCode().is2xxSuccessful()) {
				
				CotizacionDolarDTO[] cotizaciones = response.getBody();				
				
				for(CotizacionDolarDTO cotizacion : cotizaciones) {
					
					CotizacionDolarDTO.CasaDTO casaDTO = cotizacion.getCasa();
					
					if(casaDTO.getNombre().equalsIgnoreCase("Dolar Oficial")) {
						
					//se recibe un string con el formato XXX,XX y se lo formatea antes de convertirlo. 	
					return BigDecimal.valueOf(
							Double.parseDouble(casaDTO.getVenta().replace(",",".")));
						
						
					}
					
					throw new CostoPasajeException("No se pudo obtener la cotización del dólar oficial");
				}
					//throw new CostoPasajeException("No se pudo obtener la cotizacion del dolar oficial");	
			
//		} catch (RestClientException e) {
//            // Error de conexión con la API de cotización
//            throw new CostoPasajeException("Error al obtener la cotización del dólar oficial");
        }else {
        	// No se pudo obtener la cotización del dólar oficial
            throw new CostoPasajeException("No se pudo obtener la cotización del dólar oficial");
        }
			return null;
		
	}
	
}
