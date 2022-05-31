package com.developersboard.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * System configuration properties groups all properties prefixed with "system.".
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "system")
public class SystemProperties {

  private String name;
  private String email;
  private String phone;
  private String address;
}
