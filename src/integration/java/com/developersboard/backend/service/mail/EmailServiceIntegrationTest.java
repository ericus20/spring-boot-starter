package com.developersboard.backend.service.mail;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.constant.EmailConstants;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import com.developersboard.shared.util.core.WebUtils;
import com.developersboard.web.payload.request.mail.HtmlEmailRequest;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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

  @Test
  void sendHtmlEmailWithAttachment(TestInfo testInfo) throws IOException, MessagingException {
    greenMail.start();

    ClassPathResource uploadFileResource = new ClassPathResource("/profileImage.jpeg", getClass());
    File file = uploadFileResource.getFile();
    Assertions.assertTrue(file.exists());
    Assertions.assertNotNull(file);

    UserDto userDto = UserUtils.createUserDto(testInfo.getDisplayName(), true);

    Map<String, String> links = WebUtils.getDefaultEmailUrls();
    HtmlEmailRequest emailRequest = new HtmlEmailRequest();
    emailRequest.setUrls(links);
    emailRequest.setTemplate(EmailConstants.EMAIL_WELCOME_TEMPLATE);
    emailRequest.setReceiver(userDto);
    emailRequest.setFrom(userDto.getEmail());
    emailRequest.setTo(userDto.getEmail());
    emailRequest.setSubject("Test Email");
    emailRequest.setMessage(testInfo.getDisplayName());
    emailRequest.setAttachments(Collections.singleton(file));

    emailService.sendHtmlEmailWithAttachment(emailRequest);

    // Retrieve using GreenMail API
    Message[] messages = greenMail.getReceivedMessages();
    Assertions.assertEquals(1, messages.length);

    // if you send content as a multipart...
    Assertions.assertTrue(messages[0].getContent() instanceof MimeMultipart);

    greenMail.stop();
  }
}
