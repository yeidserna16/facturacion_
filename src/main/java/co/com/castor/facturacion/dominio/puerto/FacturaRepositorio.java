package co.com.castor.facturacion.dominio.puerto;

import java.util.List;

import co.com.castor.facturacion.aplicacion.dto.FacturaDTO;

public interface FacturaRepositorio {

	List<FacturaDTO> listarFacturas() throws Exception;

    FacturaDTO buscarPorId(Integer idFactura) throws Exception;

    List<FacturaDTO> listarPorCliente(Integer idCliente) throws Exception;
}
