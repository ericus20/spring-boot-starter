package com.developersboard.config.core;

import com.developersboard.backend.service.impl.ApplicationDateTimeProvider;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.auditing.DateTimeProvider;

/**
 * This class holds application configuration settings for this application.
 *
 * @author Matthew Puentes
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class ApplicationConfig {

  /**
   * A bean to be used by Clock.
   *
   * @return instance of Clock
   */
  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }

  /**
   * A bean to be used by DateTimeProvider.
   *
   * @return instance of CurrentDateTimeProvider
   */
  @Bean
  @Primary
  public DateTimeProvider dateTimeProvider() {
    return new ApplicationDateTimeProvider(clock());
  }
}
