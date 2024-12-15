package com.developersboard;

import com.developersboard.config.properties.AwsProperties;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * This class provides every bean, and other configurations needed to be used in the testing phase.
 *
 * @author George Anguah
 * @version 1.0
 * @since 1.0
 */
@Configuration
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
    ServerSetup smtp = ServerSetupTest.SMTP;
    smtp.setServerStartupTimeout(5000);

    return new GreenMail(smtp)
        .withConfiguration(GreenMailConfiguration.aConfig().withUser(mailUsername, mailPassword));
  }

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
