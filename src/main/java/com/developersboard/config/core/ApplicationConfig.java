package com.developersboard.config.core;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import javax.sql.DataSource;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.developersboard.config.properties.AwsProperties;

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
    /**
     * A bean to be used by AmazonS3 Service.
     *
     * @param props the aws properties
     * @return instance of AmazonS3Client
     */
    @Bean
    public AmazonS3 amazonS3 (AwsProperties props){
      // Create the credentials provider
      var credentials = new BasicAWSCredentials(props.getAccessKeyId(), props.getSecretAccessKey());

      return AmazonS3ClientBuilder.standard()
              .withRegion(Regions.fromName(props.getRegion()))
              .withCredentials(new AWSStaticCredentialsProvider(credentials))
              .build();
    }
}
