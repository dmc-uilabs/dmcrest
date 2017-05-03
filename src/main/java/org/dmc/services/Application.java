package org.dmc.services;

import javax.sql.DataSource;

import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = { SolrAutoConfiguration.class })
@EnableScheduling
public class Application extends SpringBootServletInitializer {

	@Value("#{environment.DBport}")
	private Integer dbPort;// = System.getenv("DBport");//"5432";//System.getenv("DBport");

	@Value("#{environment.DBip}")
	private String dbServerName; //54.237.192.205

	private String dbName = "gforge";

	@Value("#{environment.DBuser}")
	private String dbUser;//"gforge";//System.getenv("DBuser");

	@Value("#{environment.DBpass}")
	private String dbPass;//"gforge";//System.getenv("DBpass");

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	@Bean(destroyMethod = "close")
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
