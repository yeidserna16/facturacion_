package co.com.castor.facturacion.infraestructura.configuracion;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceConfig {

	 @Bean(name = "postgresDataSource")
	    @Primary
	    @ConfigurationProperties(prefix = "spring.datasource.postgresql")
	    public DataSource postgresDataSource() {
	        return DataSourceBuilder.create().build();
	    }

	    @Bean(name = "oracleDataSource")
	    @ConfigurationProperties(prefix = "spring.datasource.oracle")
	    public DataSource oracleDataSource() {
	        return DataSourceBuilder.create().build();
	    }

	    @Bean(name = "postgresJdbc")
	    public JdbcTemplate postgresJdbc(@Qualifier("postgresDataSource") DataSource dsPos) {
	        return new JdbcTemplate(dsPos);
	    }

	    @Bean(name = "oracleJdbc")
	    public JdbcTemplate oracleJdbc(@Qualifier("oracleDataSource") DataSource dsOrac) {
	        return new JdbcTemplate(dsOrac);
	    }
	}