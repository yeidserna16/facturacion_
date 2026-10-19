package co.com.castor.facturacion.aplicacion.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemFacturaDTO {

		private String descripcion;
		private BigDecimal precio;
		private Integer cantidad;
	}

