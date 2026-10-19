package co.com.castor.facturacion.dominio.modelo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleFactura {

	private Integer id;
	private Integer idFactura;
	private String descripcion;
	private Integer cantidad;
	private BigDecimal precio;
	private BigDecimal subtotal;
}
