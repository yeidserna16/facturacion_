package co.com.castor.facturacion.infraestructura.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiClienteNoExisteHandler {

	@ExceptionHandler(NoExisteClienteException.class)
	public ResponseEntity<Map<String, Object>> handleNoExisteCliente(NoExisteClienteException ex) {
	    Map<String, Object> body = new HashMap<>();
	    body.put("mensaje", ex.getMessage());
	    body.put("fecha", LocalDateTime.now());
	    body.put("status", HttpStatus.NOT_FOUND.value());

	    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}
}
