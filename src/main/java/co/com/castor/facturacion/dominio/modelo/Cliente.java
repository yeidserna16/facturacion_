package co.com.castor.facturacion.dominio.modelo;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {
	
	private Integer id;
	private String nombres;
	private String tipoDoc;
	private String identificacion;
	private String email;
	private Timestamp fechaCreacion;
	private Timestamp fechaModificacion;
	private Boolean activo;
 
}
