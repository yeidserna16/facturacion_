package co.com.castor.facturacion.aplicacion.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaCalculoDTO {

	private Integer idCliente;
	private List<ItemFacturaDTO> items;
	private BigDecimal subtotal;
	private BigDecimal descuento;
	private BigDecimal impuesto;
	private BigDecimal total;
	private Integer idFactura;
	private String codigoFactura;

	
}
