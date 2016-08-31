package org.dmc.services;

import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication(exclude = { SolrAutoConfiguration.class })
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public DataSource getDataSource() {
		PGPoolingDataSource ds = new PGPoolingDataSource();
		ds.setUser(Config.DB_USER);
		ds.setPassword(Config.DB_PASS);
		ds.setServerName(Config.DB_IP);
		ds.setPortNumber(Integer.parseInt(Config.DB_PORT));
		ds.setDatabaseName(Config.DB_NAME);
		return ds;
	}
}