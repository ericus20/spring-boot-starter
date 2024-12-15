package com.developersboard.config.core;

import com.developersboard.backend.service.impl.ApplicationDateTimeProvider;
import com.developersboard.config.properties.AwsProperties;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.auditing.DateTimeProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

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

  @Bean
  public S3Presigner s3Presigner(AwsProperties props) {
    return S3Presigner.builder().region(Region.of(props.getRegion())).build();
  }
}
