package org.dmc.services;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {SolrAutoConfiguration.class})
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
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUser(Config.DB_USER);
        ds.setPassword(Config.DB_PASS);
        String dbURL = Config.DB_HOST + ":" + Config.DB_PORT + "/" + Config.DB_NAME;
        ds.setUrl(dbURL);
        return ds;
    }

}