package com.developersboard.web.controller;

import com.developersboard.TestUtils;
import com.developersboard.backend.service.mail.EmailService;
import com.developersboard.config.properties.SystemProperties;
import com.developersboard.constant.ContactConstants;
import com.developersboard.constant.user.UserConstants;
import com.developersboard.shared.util.core.SecurityUtils;
import com.developersboard.web.payload.request.mail.FeedbackRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest extends TestUtils {

  private transient MockMvc mockMvc;

  @Mock private transient SystemProperties systemProperties;

  @Mock private transient EmailService emailService;

  @InjectMocks private transient ContactController contactController;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(contactController).build();

    SecurityUtils.clearAuthentication();
  }

  @Test
  void testPathAndViewNameNotAuthenticated() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get(ContactConstants.CONTACT_URL_MAPPING))
        .andExpect(MockMvcResultMatchers.view().name(ContactConstants.CONTACT_VIEW_NAME))
        .andExpect(
            MockMvcResultMatchers.model().attributeDoesNotExist(UserConstants.USER_MODEL_KEY))
        .andExpect(MockMvcResultMatchers.model().attributeExists(ContactConstants.FEEDBACK))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void testPathAndViewNameAuthenticated(TestInfo testInfo) throws Exception {
    TestUtils.setAuthentication(testInfo.getDisplayName(), TestUtils.ROLE_USER);

    mockMvc
        .perform(MockMvcRequestBuilders.get(ContactConstants.CONTACT_URL_MAPPING))
        .andExpect(MockMvcResultMatchers.view().name(ContactConstants.CONTACT_VIEW_NAME))
        .andExpect(MockMvcResultMatchers.model().attributeExists(UserConstants.USER_MODEL_KEY))
        .andExpect(MockMvcResultMatchers.model().attributeExists(ContactConstants.FEEDBACK))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void testSubmitContactFeedbackFormNoFeedback() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.post(ContactConstants.CONTACT_URL_MAPPING))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void testSubmitContactFeedbackForm() throws Exception {
    Mockito.when(systemProperties.getEmail()).thenReturn(FAKER.internet().emailAddress());

    var feedback = getFeedbackRequest();
    mockMvc
        .perform(
            MockMvcRequestBuilders.post(ContactConstants.CONTACT_URL_MAPPING)
                .param("name", FAKER.name().fullName())
                .param("email", FAKER.internet().emailAddress())
                .flashAttr(ContactConstants.FEEDBACK, feedback))
        .andExpect(MockMvcResultMatchers.view().name(ContactConstants.CONTACT_VIEW_NAME))
        .andExpect(MockMvcResultMatchers.model().attributeExists(ContactConstants.FEEDBACK))
        .andExpect(
            MockMvcResultMatchers.model().attributeExists(ContactConstants.FEEDBACK_SUCCESS_KEY))
        .andExpect(MockMvcResultMatchers.status().isOk());

    Mockito.verify(emailService, Mockito.only())
        .sendMailWithFeedback(Mockito.any(FeedbackRequest.class));
  }

  private FeedbackRequest getFeedbackRequest() {
    var feedback = new FeedbackRequest();
    feedback.setEmail(FAKER.internet().emailAddress());
    feedback.setName(FAKER.name().fullName());
    feedback.setSubject(FAKER.lorem().characters(5));
    feedback.setMessage(FAKER.lorem().characters());

    return feedback;
  }
}
