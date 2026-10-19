package co.com.castor.facturacion.infraestructura.persistencia;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import co.com.castor.facturacion.dominio.modelo.DetalleFactura;
import co.com.castor.facturacion.dominio.puerto.DetalleFacturaRepositorio;

public class DetalleFacturaRepositorioOracleImpl implements DetalleFacturaRepositorio {

    private final JdbcTemplate jdbc;

    public DetalleFacturaRepositorioOracleImpl(JdbcTemplate oracleJdbc) {
        this.jdbc = oracleJdbc;
    }

   
    @Override
    public List<DetalleFactura> obtenerPorFactura(Integer idFactura) throws Exception {
        return jdbc.query(
                "SELECT * FROM DETALLE_FACTURA WHERE ID_FACTURA = ?",
                new DetalleFacturaRowMapper(),
                idFactura
        );
    }
}
