package co.com.castor.facturacion.infraestructura.controladores;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import co.com.castor.facturacion.aplicacion.servicio.ClienteServicio;
import co.com.castor.facturacion.dominio.modelo.Cliente;
import co.com.castor.facturacion.infraestructura.excepciones.ClienteDuplicadoException;
import co.com.castor.facturacion.infraestructura.excepciones.NoExisteClienteException;

@WebMvcTest(controllers = ClientesController.class)
@Import(ClienteControllerTest.MockConfig.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteServicio servicio;

    @TestConfiguration
    static class MockConfig {
        @Bean
        ClienteServicio clienteServicio() {
            return mock(ClienteServicio.class);
        }
    }
    
    @BeforeEach
    void resetMocks() {
        Mockito.reset(servicio);
    }

    //--------------------------------------------------

    @Test
    void eliminarCliente_OK() throws Exception {
        doNothing().when(servicio).eliminarCliente(1);

        mockMvc.perform(delete("/clientes/1"))
        	.andExpect(status().isNoContent());
    }

    @Test
    void eliminarCliente_NoExisteDebeRetornar404() throws Exception {
        doThrow(new NoExisteClienteException("Cliente no encontrado"))
                .when(servicio).eliminarCliente(20);

        mockMvc.perform(delete("/clientes/20"))
        	.andExpect(status().isNotFound());
    }

    //--------------------------------------------------

    @Test
    void insertarCliente_Created() throws Exception {
        Cliente request = new Cliente();
        request.setNombres("Juan");
        request.setIdentificacion("123456");

        when(servicio.insertarCliente(any())).thenReturn(request);

        mockMvc.perform(post("/clientes/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"nombres":"Juan Lopez","identificacion":"123456"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.identificacion").value("123456"));
    }

    @Test
    void insertarCliente_DuplicadoDebeRetornar400() throws Exception {
        doThrow(new ClienteDuplicadoException("Cliente duplicado, no puede ser insertado"))
                .when(servicio).insertarCliente(any());

        mockMvc.perform(post("/clientes/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"nombres":"Juan Arango","identificacion":"123456"}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cliente duplicado, no puede ser insertado"));
    }

    @Test
    void insertarCliente_ErrorInesperadoDebeRetornar500() throws Exception {
        doThrow(new RuntimeException("Error inesperado"))
                .when(servicio).insertarCliente(any());

        mockMvc.perform(post("/clientes/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"nombres":"Felipe Cuadros","identificacion":"123"}
                        """))
                .andExpect(status().isInternalServerError());
    }
    
    //--------------------------------------------------------------
    @Test
    void actualizarCliente_OK() throws Exception {

        Cliente cliente = new Cliente();
        cliente.setId(5);
        cliente.setNombres("Carlos");
        cliente.setIdentificacion("555");

        when(servicio.actualizarCliente(any())).thenReturn(cliente);

        mockMvc.perform(put("/clientes/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"nombres":"Carlos Aguilar","identificacion":"22"}
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombres").value("Carlos"));
    }
    
    @Test
    void actualizarCliente_NoExisteDebeRetornar404() throws Exception {

        doThrow(new NoExisteClienteException("El cliente no existe"))
                .when(servicio).actualizarCliente(any());

        mockMvc.perform(put("/clientes/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"nombres":"Pedro Picapiedra","identificacion":"9999999"}
                """))
                .andExpect(status().isNotFound())
                .andExpect(content().string("El cliente no existe"));
    }

    @Test
    void actualizarCliente_ErrorInesperadoDebeRetornar500() throws Exception {

        doThrow(new RuntimeException("Error"))
                .when(servicio).actualizarCliente(any());

        mockMvc.perform(put("/clientes/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"nombres":"Mario Bross","identificacion":"11111"}
                """))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error inesperado al actualizar el cliente"));
    }

    
}
