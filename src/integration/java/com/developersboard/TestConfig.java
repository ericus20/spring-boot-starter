package com.developersboard;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.developersboard.config.properties.AwsProperties;
import com.developersboard.constant.ProfileTypeConstants;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * This class provides every bean, and other configurations needed to be used in the testing phase.
 *
 * @author George Anguah
 * @version 1.0
 * @since 1.0
 */
@Configuration
@Profile(ProfileTypeConstants.TEST)
public class TestConfig {

  @Value("${spring.mail.username}")
  private transient String mailUsername;

  @Value("${spring.mail.password}")
  private transient String mailPassword;

  @Value("${spring.mail.host}")
  private transient String mailHost;

  @Value("${spring.mail.port}")
  private transient int mailPort;

  @Value("${spring.mail.protocol}")
  private transient String mailProtocol;

  /**
   * Creates a JavaMailSender bean.
   *
   * @return javaMailSender
   */
  @Bean
  public JavaMailSender mailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(mailHost);
    mailSender.setPort(mailPort);
    mailSender.setUsername(mailUsername);
    mailSender.setPassword(mailPassword);
    mailSender.setProtocol(mailProtocol);

    return mailSender;
  }

  @Bean
  public GreenMail greenMail() {
    return new GreenMail(ServerSetupTest.SMTP)
        .withConfiguration(GreenMailConfiguration.aConfig().withUser(mailUsername, mailPassword));
  }

  /**
   * A bean to be used by AmazonS3 Service.
   *
   * @param props the aws properties
   * @return instance of AmazonS3Client
   */
  @Bean
  public AmazonS3 amazonS3(AwsProperties props) {
    var endpoint = new EndpointConfiguration(props.getServiceEndpoint(), props.getRegion());
    // Create the credentials provider
    var credentials = new AnonymousAWSCredentials();

    return AmazonS3ClientBuilder.standard()
        .withPathStyleAccessEnabled(true)
        .withEndpointConfiguration(endpoint)
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .build();
  }
}
