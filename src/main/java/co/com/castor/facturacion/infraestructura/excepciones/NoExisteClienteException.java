package co.com.castor.facturacion.infraestructura.excepciones;

public class NoExisteClienteException extends Exception {    

	private static final long serialVersionUID = 1L;
    public NoExisteClienteException(String mensaje) {
        super(mensaje);
    }
}
