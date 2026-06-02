package com.venkat.backend.datastore;

import com.venkat.backend.datastore.nosql.NoteRepository;
import com.venkat.backend.datastore.sql.ArticleRepository;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

/**
 * Wires the datastore layer — only active with the {@code datastore} Spring profile.
 *
 * The ConnectionFactoryInitializer runs schema.sql on startup (CREATE TABLE IF NOT EXISTS),
 * which is idempotent and safe to re-run.  In production use Flyway or Liquibase instead.
 */
@Configuration
@Profile("datastore")
public class DatastoreConfig {

    /**
     * Applies schema.sql against the R2DBC ConnectionFactory at startup.
     * Spring Data R2DBC does NOT auto-apply schema files the way JPA does; this bean is
     * the equivalent of Hibernate's {@code spring.jpa.hibernate.ddl-auto=update}.
     */
    @Bean
    ConnectionFactoryInitializer schemaInitializer(ConnectionFactory connectionFactory) {
        var initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(
                new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        return initializer;
    }

    @Bean
    DatastoreController datastoreController(ArticleRepository articleRepo,
                                            NoteRepository noteRepo) {
        return new DatastoreController(articleRepo, noteRepo);
    }
}
