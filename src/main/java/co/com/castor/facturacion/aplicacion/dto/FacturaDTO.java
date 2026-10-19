package co.com.castor.facturacion.aplicacion.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaDTO {

	private Integer id;
    private Integer idCliente;
    private String codigoFactura;
    private String estadoFactura;
    private BigDecimal total;
    private BigDecimal impuesto;
    private BigDecimal descuento;
    private String moneda;
    private String fechaCreacion;
    
    private List<DetallesFacDTO> detalleFactura;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor 
    @Builder
    public static class DetallesFacDTO {
        private String descripcion;
        private Integer cantidad;
        private BigDecimal precio;
        private BigDecimal subtotal;
    }
}
