package co.com.castor.facturacion.infraestructura.persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import co.com.castor.facturacion.dominio.modelo.DetalleFactura;

public class DetalleFacturaRowMapper implements RowMapper<DetalleFactura> {

    @Override
    public DetalleFactura mapRow(ResultSet rs, int rowNum) throws SQLException {
        return DetalleFactura.builder()
                .id(rs.getInt("ID"))
                .idFactura(rs.getInt("ID_FACTURA"))
                .descripcion(rs.getString("DESCRIPCION"))
                .cantidad(rs.getInt("CANTIDAD"))
                .precio(rs.getBigDecimal("PRECIO"))
                .subtotal(rs.getBigDecimal("SUBTOTAL"))
                .build();
    }
}
