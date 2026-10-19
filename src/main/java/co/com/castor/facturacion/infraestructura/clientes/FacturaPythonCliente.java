package co.com.castor.facturacion.infraestructura.clientes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import co.com.castor.facturacion.aplicacion.dto.FacturaCalculoDTO;

@Component
public class FacturaPythonCliente {


    @Value("${python.factura.url}")
    private String pythonUrl;
    
    //NOTA:  Para instalar flask con python se hace asi: python -m pip install flask
    public FacturaCalculoDTO calcularFactura(FacturaCalculoDTO request) {
        RestTemplate rest = new RestTemplate();
        return rest.postForObject(pythonUrl + "/calcular-factura", request, FacturaCalculoDTO.class);
    }
}
