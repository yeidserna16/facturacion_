package co.com.castor.facturacion.aplicacion.servicio;

import java.util.List;

import co.com.castor.facturacion.aplicacion.dto.FacturaCalculoDTO;
import co.com.castor.facturacion.aplicacion.dto.FacturaDTO;

public interface FacturaServicio {

    FacturaCalculoDTO procesarFactura(FacturaCalculoDTO request) throws Exception;
    
    List<FacturaDTO> listarFacturas() throws Exception;

    FacturaDTO buscarPorId(Integer idFactura) throws Exception;

    List<FacturaDTO> listarPorCliente(Integer idCliente) throws Exception;
}
