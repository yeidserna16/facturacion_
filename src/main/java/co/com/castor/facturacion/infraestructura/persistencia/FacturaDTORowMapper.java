package co.com.castor.facturacion.infraestructura.persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import co.com.castor.facturacion.aplicacion.dto.FacturaDTO;

public class FacturaDTORowMapper implements RowMapper<FacturaDTO> {

	 @Override
	    public FacturaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
	        return FacturaDTO.builder()
	                .id(rs.getInt("ID"))
	                .idCliente(rs.getInt("ID_CLIENTE"))
	                .codigoFactura(rs.getString("CODIGO_FACTURA"))
	                .estadoFactura(rs.getString("ESTADO_FACTURA"))
	                .total(rs.getBigDecimal("TOTAL"))
	                .impuesto(rs.getBigDecimal("IMPUESTO"))
	                .descuento(rs.getBigDecimal("DESCUENTO"))
	                .moneda(rs.getString("MONEDA"))
	                .fechaCreacion(rs.getString("FECHA_CREACION"))
	                .build();
	    }
}
