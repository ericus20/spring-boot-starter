package com.developersboard.config.core;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.developersboard.backend.service.mail.EmailService;
import com.developersboard.backend.service.mail.impl.MockEmailServiceImpl;
import com.developersboard.config.properties.AwsProperties;
import com.developersboard.constant.EnvConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * This class provides every bean, and other configurations needed to be used in the development
 * phase.
 *
 * @author George Anguah
 * @version 1.0
 * @since 1.0
 */
@Configuration
@Profile(EnvConstants.DOCKER)
public class DockerConfig {

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

  /**
   * Creates a EmailService bean.
   *
   * @return emailService
   */
  @Bean
  public EmailService emailService() {
    return new MockEmailServiceImpl();
  }

  /**
   * Creates a JavaMailSender bean.
   *
   * @return javaMailSender
   */
  @Bean
  public JavaMailSender mailSender() {
    return new JavaMailSenderImpl();
  }
}
