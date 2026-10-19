package co.com.castor.facturacion.dominio.puerto;

import java.util.List;

import co.com.castor.facturacion.dominio.modelo.DetalleFactura;

public interface DetalleFacturaRepositorio {

    List<DetalleFactura> obtenerPorFactura(Integer idFactura) throws Exception;
}
