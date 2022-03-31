package com.developersboard.config.core;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * This class holds application configuration settings for this application.
 *
 * @author Matthew Puentes
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class ApplicationConfig {

  @Bean
  @Primary
  public DataSource dataSource() throws Exception {

    return EmbeddedPostgres.builder().start().getPostgresDatabase();
  }
}
