package co.com.castor.facturacion.aplicacion.servicio;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.castor.facturacion.aplicacion.dto.FacturaCalculoDTO;
import co.com.castor.facturacion.aplicacion.dto.FacturaDTO;
import co.com.castor.facturacion.aplicacion.dto.ItemFacturaDTO;
import co.com.castor.facturacion.dominio.puerto.ClienteRepositorio;
import co.com.castor.facturacion.dominio.puerto.FacturaRepositorio;
import co.com.castor.facturacion.infraestructura.clientes.FacturaPythonCliente;
import co.com.castor.facturacion.infraestructura.persistencia.ClienteRepositorioPostgreSQLImpl;
import co.com.castor.facturacion.infraestructura.persistencia.FacturaRepositorioOracleImpl;


@ExtendWith(MockitoExtension.class)
class FacturaServicioTest {


    @Mock
    private ClienteRepositorio clienteRepositorio;

    @Mock
    private FacturaRepositorio facturaRepositorio;

    @InjectMocks
    private FacturaServicioImpl facturaServicio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void noDebePermitirFacturarClienteInactivo() throws Exception {

        // 1) Mocks
        ClienteRepositorioPostgreSQLImpl repoPostgres = mock(ClienteRepositorioPostgreSQLImpl.class);
        FacturaPythonCliente pythonClient = mock(FacturaPythonCliente.class);
        FacturaRepositorioOracleImpl repoOracle = mock(FacturaRepositorioOracleImpl.class);

        // 2) Servicio
        FacturaServicioImpl servicio = new FacturaServicioImpl(pythonClient,repoPostgres, repoOracle);

        // 3) Request simulado
        FacturaCalculoDTO request = new FacturaCalculoDTO();
        request.setIdCliente(10);

        // 4) Simular que el cliente NO existe o está inactivo
        when(repoPostgres.existeCliente(10)).thenReturn(false);

        // 5) Validar excepción
        Exception ex = assertThrows(Exception.class, () -> servicio.procesarFactura(request));
        System.out.println("Test: Cliente inactivo NO puede facturar");
        assertTrue(ex.getMessage().contains("no existe"));
        
        // 6) Verificar que NUNCA se intentó guardar factura
        verify(repoOracle, never()).insertarFacturaSP(any());
        verify(repoOracle, never()).insertarDetalleFactura(anyInt(), any());
    }
    
    @Test
    void procesarFactura_clienteActivo_facturaExitosa() throws Exception {

        // Mocks
        ClienteRepositorioPostgreSQLImpl repoCliente = mock(ClienteRepositorioPostgreSQLImpl.class);
        FacturaRepositorioOracleImpl repoOracle = mock(FacturaRepositorioOracleImpl.class);
        FacturaPythonCliente pythonClient = mock(FacturaPythonCliente.class);

        // Servicio a probar
        FacturaServicioImpl servicio = new FacturaServicioImpl(pythonClient,repoCliente, repoOracle );

        // Request de prueba
        FacturaCalculoDTO request = new FacturaCalculoDTO();
        request.setIdCliente(10);
        request.setItems(List.of(
                new ItemFacturaDTO("Mouse", BigDecimal.valueOf(25000), 2)
        ));

        // Simula q el cliente si existe y está activo
        when(repoCliente.existeCliente(10)).thenReturn(true);

        // Simula  Python responde con cálculos
        FacturaCalculoDTO respuestaPython = new FacturaCalculoDTO();
        respuestaPython.setIdCliente(10);
        respuestaPython.setTotal(BigDecimal.valueOf(100000));
        when(pythonClient.calcularFactura(request)).thenReturn(respuestaPython);

        // Simula que oracle retorna ID de factura creada
        when(repoOracle.insertarFacturaSP(any())).thenReturn(999);

        //Ejecutar
        FacturaCalculoDTO resultado = servicio.procesarFactura(request);

        //Validaciones
        assertNotNull(resultado);
        assertEquals(10, resultado.getIdCliente());
        assertEquals(999, resultado.getIdFactura());
        assertNotNull(resultado.getCodigoFactura());

        // Verificar que realmente se insertó
        verify(repoOracle, times(1)).insertarFacturaSP(any());
        verify(repoOracle, times(1)).insertarDetalleFactura(eq(999), any());

        System.out.println("Test: Factura generada exitosamente con ID " + resultado.getIdFactura());
    }
    
    @Test
    void listarFacturasCliente_ok() {

        ClienteRepositorioPostgreSQLImpl repoCliente = mock(ClienteRepositorioPostgreSQLImpl.class);
        FacturaRepositorioOracleImpl repoFacturaOracle = mock(FacturaRepositorioOracleImpl.class);
        FacturaPythonCliente pythonClient = mock(FacturaPythonCliente.class);

        FacturaServicioImpl facturaServicioImpl = new FacturaServicioImpl(pythonClient, repoCliente, repoFacturaOracle);

        when(repoFacturaOracle.listarPorCliente(10))
                .thenReturn(List.of(new FacturaDTO()));

        List<FacturaDTO> result = facturaServicioImpl.listarPorCliente(10);

        assertFalse(result.isEmpty());
        verify(repoFacturaOracle, times(1)).listarPorCliente(10);

        System.out.println("🔎 Test listarFacturasCliente_ok: Se devolvieron "
                + result.size() + " facturas del cliente 10.");
    }

    @Test
    void consultarFactura_ok() {

        ClienteRepositorioPostgreSQLImpl repoCliente = mock(ClienteRepositorioPostgreSQLImpl.class);
        FacturaRepositorioOracleImpl repoFacturaOracleImpl = mock(FacturaRepositorioOracleImpl.class);
        FacturaPythonCliente pythonClient = mock(FacturaPythonCliente.class);

        FacturaServicioImpl servicio = new FacturaServicioImpl(pythonClient, repoCliente, repoFacturaOracleImpl);

        when(repoFacturaOracleImpl.buscarPorId(5))
                .thenReturn(new FacturaDTO());

        FacturaDTO result = servicio.buscarPorId(5);

        assertNotNull(result);
        verify(repoFacturaOracleImpl).buscarPorId(5);

        System.out.println("Test consultarFactura_ok: Consulta exitosa de la factura con ID 5.");
    }
    
    @Test
    void procesarFactura_errorPythonDebePropagar() throws Exception {

        //mocks
        ClienteRepositorioPostgreSQLImpl repoCliente = mock(ClienteRepositorioPostgreSQLImpl.class);
        FacturaRepositorioOracleImpl repoFactura = mock(FacturaRepositorioOracleImpl.class);
        FacturaPythonCliente python = mock(FacturaPythonCliente.class);

        //  El cliente existe -> pasa a Python
        when(repoCliente.existeCliente(10)).thenReturn(true);

        // Simula error en Python
        when(python.calcularFactura(any())).thenThrow(new RuntimeException("Error ocurrido por Python"));

        // Servicio con mocks
        FacturaServicioImpl servicio = new FacturaServicioImpl(python, repoCliente, repoFactura);

        // Ejecutar y verificar excepción
        FacturaCalculoDTO dto = new FacturaCalculoDTO();
        dto.setIdCliente(10);

        assertThrows(RuntimeException.class, () -> servicio.procesarFactura(dto));
    }




}
