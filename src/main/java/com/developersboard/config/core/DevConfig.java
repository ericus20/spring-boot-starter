package com.developersboard.config.core;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.developersboard.config.properties.AwsProperties;
import com.developersboard.constant.ProfileTypeConstants;
import com.developersboard.constant.SecurityConstants;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
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
@Profile(ProfileTypeConstants.DEV)
public class DevConfig {

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
   * A bean to register the path /h2-console for the h2 database.
   *
   * @return the h2 registered bean.
   */
  @Bean
  public ServletRegistrationBean<WebServlet> h2servletRegistration() {
    return new ServletRegistrationBean<>(
        new WebServlet(), SecurityConstants.H2_CONSOLE_URL_MAPPING);
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
