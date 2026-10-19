package co.com.castor.facturacion.infraestructura.excepciones;

public class FacturacionException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FacturacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}