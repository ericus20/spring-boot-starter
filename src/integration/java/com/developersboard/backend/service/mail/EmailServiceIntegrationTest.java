package com.developersboard.backend.service.mail;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.backend.service.security.JwtService;
import com.developersboard.config.properties.SystemProperties;
import com.developersboard.constant.EmailConstants;
import com.developersboard.shared.util.UserUtils;
import com.developersboard.shared.util.core.WebUtils;
import com.developersboard.web.payload.request.mail.FeedbackRequest;
import com.developersboard.web.payload.request.mail.HtmlEmailRequest;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.lang3.StringUtils;
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

  @Autowired private transient SystemProperties systemProperties;
  @Autowired private transient EmailService emailService;
  @Autowired private transient JwtService jwtService;

  @Autowired private transient GreenMail greenMail;

  private transient String body;
  private transient String subject;
  private transient String sender;
  private transient String recipient;

  @BeforeEach
  void setUp() {
    greenMail.start();
    sender = StringUtils.EMPTY;
    recipient = StringUtils.EMPTY;
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
  void sendEmail() {

    var message = new SimpleMailMessage();
    message.setText(body);
    message.setSubject(subject);
    message.setTo(FAKER.internet().emailAddress());
    message.setFrom(FAKER.internet().emailAddress());
    sender = message.getFrom();
    recipient = Objects.requireNonNull(message.getTo())[0];
    subject = message.getSubject();

    emailService.sendMail(message);

    // Retrieve using GreenMail API
    var messages = greenMail.getReceivedMessages();
    Assertions.assertEquals(body, GreenMailUtil.getBody(messages[0]).trim());

    assertEmailResponse(false);
  }

  @Test
  void sendEmailWithFeedback() {

    var feedback = new FeedbackRequest();
    feedback.setMessage(body);
    feedback.setSubject(subject);
    feedback.setName(FAKER.name().fullName());
    // This is a feedback from a user to an internal email. (usually from contact page)
    feedback.setTo(systemProperties.getEmail());
    feedback.setEmail(FAKER.internet().emailAddress());

    sender = feedback.getEmail();
    recipient = feedback.getTo();
    subject = feedback.getSubject();

    emailService.sendMailWithFeedback(feedback);

    // Retrieve using GreenMail API
    Message[] messages = greenMail.getReceivedMessages();
    Assertions.assertEquals(body, GreenMailUtil.getBody(messages[0]).trim());

    assertEmailResponse(false);
  }

  @Test
  void sendHtmlEmail(TestInfo testInfo) {

    var userDto = UserUtils.createUserDto(false);

    Map<String, String> links = WebUtils.getDefaultEmailUrls();
    HtmlEmailRequest emailRequest = new HtmlEmailRequest();
    emailRequest.setUrls(links);
    emailRequest.setTemplate(EmailConstants.EMAIL_WELCOME_TEMPLATE);
    emailRequest.setReceiver(userDto);
    emailRequest.setFrom(userDto.getEmail());
    emailRequest.setTo(userDto.getEmail());
    emailRequest.setSubject(subject);
    emailRequest.setMessage(testInfo.getDisplayName());

    sender = emailRequest.getFrom();
    recipient = emailRequest.getTo();
    subject = emailRequest.getSubject();

    emailService.sendHtmlEmail(emailRequest);

    assertEmailResponse(false);
  }

  @Test
  void sendHtmlEmailWithAttachment(TestInfo testInfo) throws IOException {

    var uploadFileResource = new ClassPathResource(PROFILE_IMAGE_JPEG, getClass());
    var file = uploadFileResource.getFile();
    Assertions.assertTrue(file.exists());
    Assertions.assertNotNull(file);

    var userDto = UserUtils.createUserDto(testInfo.getDisplayName(), true);
    var links = WebUtils.getDefaultEmailUrls();

    var emailRequest = new HtmlEmailRequest();
    emailRequest.setUrls(links);
    emailRequest.setTemplate(EmailConstants.EMAIL_WELCOME_TEMPLATE);
    emailRequest.setReceiver(userDto);
    emailRequest.setFrom(userDto.getEmail());
    emailRequest.setTo(userDto.getEmail());
    emailRequest.setSubject(subject);
    emailRequest.setMessage(testInfo.getDisplayName());
    emailRequest.setAttachments(Collections.singleton(file));

    sender = emailRequest.getFrom();
    recipient = emailRequest.getTo();
    subject = emailRequest.getSubject();

    emailService.sendHtmlEmailWithAttachment(emailRequest);

    assertEmailResponse(true);
  }

  @Test
  void sendAccountVerificationEmail() {

    var userDto = UserUtils.createUserDto(false);
    var token = jwtService.generateJwtToken(userDto.getUsername());
    subject = EmailConstants.CONFIRMATION_PENDING_EMAIL_SUBJECT;
    recipient = userDto.getEmail();

    // Send email verification
    emailService.sendAccountVerificationEmail(userDto, token);
    assertEmailResponse(false);
  }

  @Test
  void sendAccountConfirmationEmail() {

    var userDto = UserUtils.createUserDto(false);
    subject = EmailConstants.CONFIRMATION_SUCCESS_EMAIL_SUBJECT;
    recipient = userDto.getEmail();

    // Send email verification
    emailService.sendAccountConfirmationEmail(userDto);
    assertEmailResponse(false);
  }

  @Test
  void sendPasswordResetEmail() {

    var userDto = UserUtils.createUserDto(false);
    var token = jwtService.generateJwtToken(userDto.getUsername());
    subject = EmailConstants.PASSWORD_RESET_EMAIL_SUBJECT;
    recipient = userDto.getEmail();

    // Send email verification
    emailService.sendPasswordResetEmail(userDto, token);
    assertEmailResponse(false);
  }

  @Test
  void sendPasswordResetConfirmationEmail() {

    var userDto = UserUtils.createUserDto(false);
    subject = EmailConstants.PASSWORD_RESET_SUCCESS_SUBJECT;
    recipient = userDto.getEmail();

    // Send email verification
    emailService.sendPasswordResetConfirmationEmail(userDto);
    assertEmailResponse(false);
  }

  /**
   * Asserts the email response.
   *
   * @param isMultipart {@code true} if the email is multipart, {@code false} otherwise
   */
  private void assertEmailResponse(boolean isMultipart) {
    // Retrieve using GreenMail API
    Message[] messages = greenMail.getReceivedMessages();

    // If no sender is specified, then the email must be from the system.
    if (StringUtils.isBlank(sender)) {
      sender = systemProperties.getEmail();
    }

    if (StringUtils.isBlank(recipient)) {
      recipient = systemProperties.getEmail();
    }

    Assertions.assertAll(
        () -> {
          Assertions.assertEquals(1, messages.length);
          Assertions.assertEquals(subject, messages[0].getSubject());

          // The email is reformatted as internet address Example <"John Doe" <johndoe@hotmail.com>>
          // We want to check that the email is part of the string returned
          Assertions.assertTrue(messages[0].getFrom()[0].toString().contains(sender));
          Assertions.assertTrue(
              messages[0].getRecipients(RecipientType.TO)[0].toString().contains(recipient));

          Assertions.assertEquals(isMultipart, messages[0].getContent() instanceof MimeMultipart);
        });
  }
}
