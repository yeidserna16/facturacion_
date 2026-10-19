package co.com.castor.facturacion.aplicacion.servicio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import co.com.castor.facturacion.aplicacion.dto.FacturaCalculoDTO;
import co.com.castor.facturacion.aplicacion.dto.FacturaDTO;
import co.com.castor.facturacion.aplicacion.dto.ItemFacturaDTO;
import co.com.castor.facturacion.infraestructura.clientes.FacturaPythonCliente;
import co.com.castor.facturacion.infraestructura.excepciones.FacturacionException;
import co.com.castor.facturacion.infraestructura.persistencia.ClienteRepositorioPostgreSQLImpl;
import co.com.castor.facturacion.infraestructura.persistencia.FacturaRepositorioOracleImpl;

@Service
public class FacturaServicioImpl implements FacturaServicio {

    private final FacturaPythonCliente facturaPythonCliente;
    private final ClienteRepositorioPostgreSQLImpl clienteRepositorioPostgres;
    private final FacturaRepositorioOracleImpl facturaRepositorioOracleImpl;

    public FacturaServicioImpl(FacturaPythonCliente facturaPythonCliente,
                               ClienteRepositorioPostgreSQLImpl clienteRepositorioPostgres,
                               FacturaRepositorioOracleImpl facturaRepositorioOracle) {
        this.facturaPythonCliente = facturaPythonCliente;
        this.clienteRepositorioPostgres = clienteRepositorioPostgres;
        this.facturaRepositorioOracleImpl = facturaRepositorioOracle;
    }

    @Override
    public FacturaCalculoDTO procesarFactura(FacturaCalculoDTO request) throws Exception {

        try {
            // Validar si el cliente existe en PostgreSQL
            if (!clienteRepositorioPostgres.existeCliente(request.getIdCliente())) {
                throw new RuntimeException("Error El cliente con ID " + request.getIdCliente() + " no existe o no está activo en PostgreSQL");
            }

            // Consumo el microservicio de Python
            FacturaCalculoDTO resultado = facturaPythonCliente.calcularFactura(request);

            // Genero código único para factura
            resultado.setCodigoFactura(generarCodigoFactura(resultado.getIdCliente()));

            // Insertar la factura en Oracle y obtener el ID generado
            int idFactura = facturaRepositorioOracleImpl.insertarFacturaSP(resultado);

            // Inserto detalle de la factura
            for (ItemFacturaDTO item : request.getItems()) {
                facturaRepositorioOracleImpl.insertarDetalleFactura(idFactura, item);
            }

            // Devolver ID asignado
            resultado.setIdFactura(idFactura);
            return resultado;

        } catch (FacturacionException e) {
        	 throw new FacturacionException("No se pudo procesar la factura. " + e.getMessage(), e);
        }
    }

    
    private String generarCodigoFactura(Integer idCliente) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timestamp = LocalDateTime.now().format(formatter);
        return "FAC"+idCliente + "-" + timestamp;
    }
    
    public List<FacturaDTO> listarFacturas() {
        return facturaRepositorioOracleImpl.listarFacturas();
    }

    public FacturaDTO buscarPorId(Integer idFactura) {
        return facturaRepositorioOracleImpl.buscarPorId(idFactura);
    }

    public List<FacturaDTO> listarPorCliente(Integer idCliente) {
        return facturaRepositorioOracleImpl.listarPorCliente(idCliente);
    }

}
