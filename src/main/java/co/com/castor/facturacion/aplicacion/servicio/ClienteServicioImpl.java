package co.com.castor.facturacion.aplicacion.servicio;

import org.springframework.stereotype.Service;

import co.com.castor.facturacion.dominio.modelo.Cliente;
import co.com.castor.facturacion.dominio.puerto.ClienteRepositorio;
import co.com.castor.facturacion.infraestructura.excepciones.ClienteDuplicadoException;
import co.com.castor.facturacion.infraestructura.excepciones.NoExisteClienteException;

@Service
public class ClienteServicioImpl implements ClienteServicio{

	 private final ClienteRepositorio repositorio;

	    public ClienteServicioImpl(ClienteRepositorio repositorio) {
	        this.repositorio = repositorio;
	    }

	    @Override
	    public Cliente insertarCliente(Cliente request) throws Exception {

	        Cliente existente = repositorio.buscarPorIdentificacion(request.getIdentificacion());
	        if (existente != null) {
	            throw new ClienteDuplicadoException("La identificación ya está registrada: " + request.getIdentificacion());
	        }

	        try {
	            // Insertar
	            return repositorio.insertarCliente(request);

	        } catch (Exception e) {
	            if (e.getMessage() != null && e.getMessage().contains(request.getIdentificacion())) {
	                throw new ClienteDuplicadoException("La identificación ya está registrada: " + request.getIdentificacion());
	            }
	            throw e; 
	        }
	    }


	    @Override
	    public Cliente actualizarCliente(Cliente clienteReq) throws Exception {
	        // Validar si el cliente existe
	        Cliente clienteBD = repositorio.buscarPorId(clienteReq.getId());
	        if (clienteBD == null) {
	            throw new NoExisteClienteException("El cliente no existe en el sistema");
	        }
	        // Si existe, se actualiza
	        repositorio.actualizarCliente(clienteReq);

	        return clienteReq;
	    }
	    
	    @Override
	    public void eliminarCliente(Integer idCliente) throws Exception {
	        repositorio.eliminarCliente(idCliente);
	    }

	    @Override
	    public Cliente buscarPorId(Integer idCliente) throws Exception {
	        return repositorio.buscarPorId(idCliente);
	    }

}
