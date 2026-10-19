package co.com.castor.facturacion.dominio.puerto;

import co.com.castor.facturacion.dominio.modelo.Cliente;

public interface ClienteRepositorio {

    Cliente insertarCliente(Cliente cliente) throws Exception;
    Cliente actualizarCliente(Cliente cliente) throws Exception;
    void eliminarCliente(Integer idCliente) throws Exception;
    Cliente buscarPorId(Integer idCliente) throws Exception;
    Cliente buscarPorIdentificacion(String identificacion) throws Exception;

}
