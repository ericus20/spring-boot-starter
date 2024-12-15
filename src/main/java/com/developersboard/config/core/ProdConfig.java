package com.developersboard.config.core;

import com.developersboard.config.properties.AwsProperties;
import com.developersboard.constant.EnvConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * This class provides every bean, and other configurations needed to be used in the production
 * phase.
 *
 * @author Matthew Puentes
 * @version 1.0
 * @since 1.0
 */
@Configuration
@Profile({EnvConstants.PRODUCTION})
public class ProdConfig {

  /**
   * A bean to be used by AmazonS3 Service.
   *
   * @param props the aws properties
   * @return instance of S3Client
   */
  @Bean
  public S3Client s3Client(AwsProperties props) {
    // Create the credential provider
    var credentials =
        AwsBasicCredentials.create(props.getAccessKeyId(), props.getSecretAccessKey());

    return S3Client.builder()
        .region(Region.of(props.getRegion()))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }
}
