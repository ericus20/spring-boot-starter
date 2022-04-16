package com.developersboard.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Aws configuration properties groups all properties prefixed with "aws.".
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {

  // AWS credentials
  private String region;
  private String accessKeyId;
  private String secretAccessKey;
  private String s3BucketName;
  private String serviceEndpoint;
  private String servicePort;
}
