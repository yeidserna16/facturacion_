package co.com.castor.facturacion.infraestructura.controladores;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.com.castor.facturacion.aplicacion.dto.FacturaCalculoDTO;
import co.com.castor.facturacion.aplicacion.dto.FacturaDTO;
import co.com.castor.facturacion.aplicacion.servicio.FacturaServicio;

@RestController
@RequestMapping("/facturas")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE })
public class FacturaController {

	 private final FacturaServicio facturaServicio;

	    public FacturaController(FacturaServicio facturaServicio) {
	        this.facturaServicio = facturaServicio;
	    }

	    @PostMapping("/calcular-factura")
	    public ResponseEntity<FacturaCalculoDTO> calcularFactura(@RequestBody FacturaCalculoDTO request) throws Exception {
	        return ResponseEntity.ok(facturaServicio.procesarFactura(request));
	    }
	    
	    @GetMapping("/")
	    public ResponseEntity<List<FacturaDTO>> listarFacturas() throws Exception {
	        return ResponseEntity.ok(facturaServicio.listarFacturas());
	    }

	    @GetMapping("/{idFactura}")
	    public ResponseEntity<FacturaDTO> buscarPorId(@PathVariable Integer idFactura) throws Exception {
	        return ResponseEntity.ok(facturaServicio.buscarPorId(idFactura));
	    }

	    @GetMapping("/cliente/{idCliente}")
	    public ResponseEntity<List<FacturaDTO>> listarPorCliente(@PathVariable Integer idCliente) throws Exception {
	        return ResponseEntity.ok(facturaServicio.listarPorCliente(idCliente));
	    }
}
