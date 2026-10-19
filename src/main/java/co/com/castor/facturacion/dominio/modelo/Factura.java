package co.com.castor.facturacion.dominio.modelo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {

	private Integer id;
	private String codigoFactura;
	private Integer idCliente;
	private String moneda;
	private BigDecimal impuesto;
	private BigDecimal descuento;
	private BigDecimal total;
	private String estadoFactura;
	private Timestamp fechaCreacion;

}
