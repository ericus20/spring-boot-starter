package com.developersboard.backend.service.mail;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.constant.EmailConstants;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.StringUtils;
import com.developersboard.shared.util.UserUtils;
import com.developersboard.shared.util.core.WebUtils;
import com.developersboard.web.payload.request.mail.FeedbackRequest;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmailServiceIntegrationTest extends IntegrationTestUtils {

  public static final String PROFILE_IMAGE_JPEG = "/profileImage.jpeg";

  @Autowired private transient EmailService emailService;

  @Autowired private transient GreenMail greenMail;

  private transient String body;
  private transient String subject;

  @BeforeEach
  void setUp() {
    greenMail.start();
  }

  @BeforeAll
  void setup() {
    greenMail.start();
    // Use random content to avoid potential residual lingering problems
    subject = GreenMailUtil.random();
    body = GreenMailUtil.random();
  }

  @AfterEach
  void tearDown() {
    greenMail.stop();
  }

  @Test
  void sendEmail() throws MessagingException, IOException {

    var message = new SimpleMailMessage();
    message.setText(body);
    message.setSubject(subject);
    message.setTo(StringUtils.FAKER.internet().emailAddress());
    message.setFrom(StringUtils.FAKER.internet().emailAddress());

    emailService.sendMail(message);

    // Retrieve using GreenMail API
    var messages = greenMail.getReceivedMessages();
    Assertions.assertEquals(body, GreenMailUtil.getBody(messages[0]).trim());

    assertEmailResponse(false);
  }

  @Test
  void sendEmailWithFeedback() throws MessagingException, IOException {

    var feedback = new FeedbackRequest();
    feedback.setMessage(body);
    feedback.setSubject(subject);
    feedback.setName(StringUtils.FAKER.name().fullName());
    feedback.setTo(StringUtils.FAKER.internet().emailAddress());
    feedback.setFrom(StringUtils.FAKER.internet().emailAddress());
    feedback.setEmail(StringUtils.FAKER.internet().emailAddress());

    emailService.sendMailWithFeedback(feedback);

    // Retrieve using GreenMail API
    Message[] messages = greenMail.getReceivedMessages();
    Assertions.assertEquals(body, GreenMailUtil.getBody(messages[0]).trim());

    assertEmailResponse(false);
  }

  @Test
  void sendHtmlEmail(TestInfo testInfo) throws MessagingException, IOException {

    UserDto userDto = UserUtils.createUserDto(false);

    Map<String, String> links = WebUtils.getDefaultEmailUrls();
    HtmlEmailRequest emailRequest = new HtmlEmailRequest();
    emailRequest.setUrls(links);
    emailRequest.setTemplate(EmailConstants.EMAIL_WELCOME_TEMPLATE);
    emailRequest.setReceiver(userDto);
    emailRequest.setFrom(userDto.getEmail());
    emailRequest.setTo(userDto.getEmail());
    emailRequest.setSubject(subject);
    emailRequest.setMessage(testInfo.getDisplayName());

    emailService.sendHtmlEmail(emailRequest);

    assertEmailResponse(false);
  }

  @Test
  void sendHtmlEmailWithAttachment(TestInfo testInfo) throws IOException, MessagingException {

    ClassPathResource uploadFileResource = new ClassPathResource(PROFILE_IMAGE_JPEG, getClass());
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
    emailRequest.setSubject(subject);
    emailRequest.setMessage(testInfo.getDisplayName());
    emailRequest.setAttachments(Collections.singleton(file));

    emailService.sendHtmlEmailWithAttachment(emailRequest);

    assertEmailResponse(true);
  }

  private void assertEmailResponse(boolean isMultipart) throws IOException, MessagingException {
    // Retrieve using GreenMail API
    Message[] messages = greenMail.getReceivedMessages();
    Assertions.assertEquals(1, messages.length);
    Assertions.assertEquals(subject, messages[0].getSubject());

    Assertions.assertEquals(isMultipart, messages[0].getContent() instanceof MimeMultipart);
  }
}
