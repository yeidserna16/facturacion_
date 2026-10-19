package co.com.castor.facturacion.aplicacion.servicio;

import co.com.castor.facturacion.dominio.modelo.Cliente;

public interface ClienteServicio {
	Cliente insertarCliente(Cliente request) throws Exception;
	
	Cliente buscarPorId(Integer idCliente) throws Exception;

	Cliente actualizarCliente(Cliente request) throws Exception;

	void eliminarCliente(Integer idCliente) throws Exception;
}
