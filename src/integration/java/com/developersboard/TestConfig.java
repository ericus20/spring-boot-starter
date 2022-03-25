package com.developersboard;

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
  private String mailUsername;

  @Value("${spring.mail.password}")
  private String mailPassword;

  @Value("${spring.mail.host}")
  private String mailHost;

  @Value("${spring.mail.port}")
  private int mailPort;

  @Value("${spring.mail.protocol}")
  private String mailProtocol;

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
}
