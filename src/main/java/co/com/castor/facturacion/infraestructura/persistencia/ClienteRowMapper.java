package co.com.castor.facturacion.infraestructura.persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import co.com.castor.facturacion.dominio.modelo.Cliente;

public class ClienteRowMapper implements RowMapper<Cliente>{

	 @Override
	    public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
	        Cliente cliente = new Cliente();

	        cliente.setId(rs.getInt("id"));
	        cliente.setNombres(rs.getString("nombres"));
	        cliente.setTipoDoc(rs.getString("tipo_doc"));
	        cliente.setIdentificacion(rs.getString("identificacion"));
	        cliente.setEmail(rs.getString("email"));
	        Timestamp tsc = rs.getTimestamp("fecha_creacion");
	        cliente.setFechaCreacion(tsc != null ? tsc : null);
	        Timestamp tsm = rs.getTimestamp("fecha_modificacion");
	        cliente.setFechaModificacion(tsm != null ? tsm : null);
	        cliente.setActivo(rs.getBoolean("activo"));
	      
	        return cliente;
	    }
}
