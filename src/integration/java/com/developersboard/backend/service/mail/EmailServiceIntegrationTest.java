package com.developersboard.backend.service.mail;

import com.developersboard.IntegrationTestUtils;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import javax.mail.Message;
import javax.mail.MessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
class EmailServiceIntegrationTest extends IntegrationTestUtils {

  @Autowired private transient EmailService emailService;

  @Autowired private transient GreenMail greenMail;

  @Test
  void sendEmail() throws MessagingException {

    greenMail.start();

    // Use random content to avoid potential residual lingering problems
    final String subject = GreenMailUtil.random();
    final String body = GreenMailUtil.random();

    SimpleMailMessage message = new SimpleMailMessage();
    message.setSubject(subject);
    message.setText(body);
    message.setTo("receiver@email.com");
    message.setFrom("sender@email.com");

    emailService.sendMail(message);

    // Retrieve using GreenMail API
    Message[] messages = greenMail.getReceivedMessages();
    Assertions.assertEquals(1, messages.length);

    // Simple message
    Assertions.assertEquals(subject, messages[0].getSubject());
    Assertions.assertEquals(body, GreenMailUtil.getBody(messages[0]).trim());

    greenMail.stop();
  }
}
