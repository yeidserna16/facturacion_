package co.com.castor.facturacion.infraestructura.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient facturaWebClient() {
    	// esto es para usar el microservicio con Flask
        return WebClient.builder()
                .baseUrl("http://localhost:5001") 
                .build();
    }
}
