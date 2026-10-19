package co.com.castor.facturacion.infraestructura.persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import co.com.castor.facturacion.dominio.modelo.Factura;

public class FacturaRowMapper implements RowMapper<Factura> {

    @Override
    public Factura mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Factura.builder()
                .id(rs.getInt("ID"))
                .idCliente(rs.getInt("ID_CLIENTE"))
                .codigoFactura(rs.getString("CODIGO_FACTURA"))
                .moneda(rs.getString("MONEDA"))
                .impuesto(rs.getBigDecimal("IMPUESTO"))
                .total(rs.getBigDecimal("TOTAL"))
                .descuento(rs.getBigDecimal("DESCUENTO"))
                .estadoFactura(rs.getString("ESTADO_FACTURA"))
                .fechaCreacion(rs.getTimestamp("FECHA_CREACION"))
                .build();
    }
}
