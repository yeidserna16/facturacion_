package co.com.castor.facturacion.infraestructura.controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.com.castor.facturacion.aplicacion.servicio.ClienteServicio;
import co.com.castor.facturacion.dominio.modelo.Cliente;
import co.com.castor.facturacion.infraestructura.excepciones.ClienteDuplicadoException;
import co.com.castor.facturacion.infraestructura.excepciones.NoExisteClienteException;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE })
@RequestMapping("/clientes")
public class ClientesController {
	
	private final ClienteServicio clienteServicio;

    public ClientesController(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }

	@GetMapping("/{idCliente}")
	public ResponseEntity<Cliente> buscarPorId(@PathVariable("idCliente") Integer idCliente) throws Exception {
		Cliente respuesta = new Cliente();
		respuesta = clienteServicio.buscarPorId(idCliente);
		return ResponseEntity.ok(respuesta);
	}

	@PostMapping("/")
	public ResponseEntity<?> insertarCliente(@RequestBody Cliente request) {
	    try {
	        Cliente cliente = clienteServicio.insertarCliente(request);
	        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);

	    } catch (ClienteDuplicadoException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error inesperado al insertar el cliente.");
	    }
	}

	@PutMapping("/{idCliente}")
	public ResponseEntity<?> actualizarCliente(@PathVariable Integer idCliente,@RequestBody Cliente request) {
	    try {
	        request.setId(idCliente); 
	        Cliente clienteActualizado = clienteServicio.actualizarCliente(request);
	        return ResponseEntity.ok(clienteActualizado);

	    } catch (NoExisteClienteException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error inesperado al actualizar el cliente");
	    }
	}

	@DeleteMapping("/{idCliente}")
	public ResponseEntity<Void> eliminarCliente(@PathVariable Integer idCliente) throws Exception {
	    clienteServicio.eliminarCliente(idCliente);
	    return ResponseEntity.noContent().build();
	}
}
