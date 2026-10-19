package co.com.castor.facturacion.aplicacion.servicio;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.castor.facturacion.dominio.modelo.Cliente;
import co.com.castor.facturacion.infraestructura.excepciones.ClienteDuplicadoException;
import co.com.castor.facturacion.infraestructura.persistencia.ClienteRepositorioPostgreSQLImpl;


@ExtendWith(MockitoExtension.class)
class ClienteServicioTest {

	 	@Mock
	    ClienteRepositorioPostgreSQLImpl repoMock;

	    @InjectMocks
	    ClienteServicioImpl servicio;

	    @Test
	    void actualizarCliente_debeFallarSiClienteNoExiste() throws Exception {
	        when(repoMock.buscarPorId(99)).thenThrow(new Exception("El Cliente no existe"));

	        Cliente cliente = new Cliente();
	        cliente.setId(99);

	        Exception ex = assertThrows(Exception.class, () -> servicio.actualizarCliente(cliente));
	        assertTrue(ex.getMessage().contains("Cliente"));
	    }
	    
	    @Test
	    void errorAlBuscarClienteDebePropagarExcepcion() throws Exception {
	        //simulamos un error en la BD
	        when(repoMock.buscarPorId(7)).thenThrow(new RuntimeException("Error en la bd"));

	        // Act + Assert
	        Exception excepcion = assertThrows(Exception.class, () -> {
	            servicio.buscarPorId(7);
	        });

	        assertTrue(excepcion.getMessage().contains("Error"), "Debe contener mensaje de error real");
	    }
	    
	    @Test
	    void insertarClienteDuplicadoDebeLanzarExcepcion() throws Exception {
	        Cliente request = new Cliente();
	        request.setIdentificacion("123");

	        // El repositorio devuelve un cliente -> DUPLICADO
	        when(repoMock.buscarPorIdentificacion("123")).thenReturn(request);

	        Exception ex = assertThrows(ClienteDuplicadoException.class, () -> {
	            servicio.insertarCliente(request);
	        });

	        assertTrue(ex.getMessage().contains("identificación"));
	    }
	    
	    @Test
	    void insertarCliente_ok() throws Exception {
	        Cliente nuevo = new Cliente();
	        nuevo.setIdentificacion("123456789");

	        when(repoMock.buscarPorIdentificacion("123456789")).thenReturn(null);
	        when(repoMock.insertarCliente(nuevo)).thenReturn(nuevo);

	        Cliente result = servicio.insertarCliente(nuevo);

	        assertNotNull(result);
	    }
	    
	   



}
