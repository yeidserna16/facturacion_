package co.com.castor.facturacion.infraestructura.persistencia;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import co.com.castor.facturacion.dominio.modelo.Cliente;
import co.com.castor.facturacion.dominio.puerto.ClienteRepositorio;
import co.com.castor.facturacion.infraestructura.excepciones.ClienteDuplicadoException;
import co.com.castor.facturacion.infraestructura.excepciones.NoExisteClienteException;

@Repository
public class ClienteRepositorioPostgreSQLImpl implements ClienteRepositorio {

	private final JdbcTemplate jdbc;

	public ClienteRepositorioPostgreSQLImpl(@Qualifier("postgresJdbc") JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Cliente insertarCliente(Cliente request) throws Exception {

		try {
			jdbc.update(
					"INSERT INTO clientes(nombres, tipo_doc, identificacion, email, activo, fecha_creacion) VALUES (?, ?, ?, ?, ?, NOW())",
					request.getNombres(), request.getTipoDoc(), request.getIdentificacion(), request.getEmail(), true);

			return request;

		} catch (DataAccessException e) {
			// si hubo Violación de restricción UNIQUE (identificación duplicada)
			if (e.getCause() instanceof org.postgresql.util.PSQLException ps && "23505".equals(ps.getSQLState())) {
				throw new ClienteDuplicadoException("Ya existe un cliente con esta identificación: " + request.getIdentificacion());
			}
			throw e;
		}
	}

	@Override
	public Cliente actualizarCliente(Cliente clienteReq) throws Exception {
		int filas = jdbc.update(
				"UPDATE clientes SET nombres=?, tipo_doc=?, identificacion=?, email=?, activo=?, fecha_modificacion=NOW() WHERE id=?",
				clienteReq.getNombres(), clienteReq.getTipoDoc(), clienteReq.getIdentificacion(), clienteReq.getEmail(),
				clienteReq.getActivo(), clienteReq.getId());

		if (filas == 0) {
			throw new NoExisteClienteException("No existe un cliente con el ID: " + clienteReq.getId());
		}
		return clienteReq;

	}

	@Override
	public void eliminarCliente(Integer idCliente) throws Exception {
	    int filas = jdbc.update("DELETE FROM clientes WHERE id=?", idCliente);

	    if (filas == 0) {
	        throw new NoExisteClienteException("El cliente con ID: " + idCliente + ", no existe");
	    }
	}


	@Override
	public Cliente buscarPorId(Integer idCliente) throws Exception {
		try {
			return jdbc.queryForObject("SELECT * FROM clientes WHERE id = ?", new ClienteRowMapper(), idCliente);
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			return null;
		}
	}

	public boolean existeCliente(Integer idCliente) {
		Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM clientes WHERE id = ? AND activo = true",
				Integer.class, idCliente);
		return count != null && count > 0;
	}

	@Override
	public Cliente buscarPorIdentificacion(String identificacion) throws Exception {
		try {
			return jdbc.queryForObject("SELECT * FROM clientes WHERE identificacion = ?", new ClienteRowMapper(),
					identificacion);
		} catch (org.springframework.dao.EmptyResultDataAccessException e) {
			return null;
		} catch (DataAccessException e) {
			// ❗ Error en BD
			throw new Exception("Error consultando el cliente por identificación", e);
		}
	}

}
