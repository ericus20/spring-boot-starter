package com.developersboard.config.core;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.developersboard.config.properties.AwsProperties;
import com.developersboard.constant.ProfileTypeConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * This class provides every bean, and other configurations needed to be used in the production
 * phase.
 *
 * @author Matthew Puentes
 * @version 1.0
 * @since 1.0
 */
@Configuration
@Profile(ProfileTypeConstants.PROD)
public class ProdConfig {

  /**
   * A bean to be used by AmazonS3 Service.
   *
   * @param props the aws properties
   * @return instance of AmazonS3Client
   */
  @Bean
  public AmazonS3 amazonS3(AwsProperties props) {
    // Create the credentials provider
    var credentials = new BasicAWSCredentials(props.getAccessKeyId(), props.getSecretAccessKey());

    return AmazonS3ClientBuilder.standard()
        .withRegion(Regions.fromName(props.getRegion()))
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .build();
  }
}
