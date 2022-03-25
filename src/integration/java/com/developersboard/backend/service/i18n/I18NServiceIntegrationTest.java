package com.developersboard.backend.service.i18n;

import com.developersboard.constant.ProfileTypeConstants;
import java.util.Locale;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = {ProfileTypeConstants.TEST})
class I18NServiceIntegrationTest {

  private static final String MESSAGE_TEST_KEY = "message.test";

  @Autowired protected transient I18NService i18NService;

  @Test
  void getMessage() {
    String expected = "This is a test message.";
    LocaleContextHolder.setDefaultLocale(Locale.CHINESE);

    Assertions.assertEquals(expected, i18NService.getMessage(MESSAGE_TEST_KEY));
  }

  @Test
  void getMessageWithFrenchLocale() {
    String expected = "Ceci est un message test.";
    LocaleContextHolder.setDefaultLocale(Locale.FRENCH);

    Assertions.assertEquals(expected, i18NService.getMessage(MESSAGE_TEST_KEY));
  }

  @Test
  void getMessageWithSpanishLocale() {
    String expected = "Este es un mensaje de prueba.";
    LocaleContextHolder.setDefaultLocale(new Locale("es"));

    Assertions.assertEquals(expected, i18NService.getMessage(MESSAGE_TEST_KEY));
  }

  @Test
  void getMessageWithChineseLocale() {
    String expected = "这是一条测试消息。";
    LocaleContextHolder.setDefaultLocale(Locale.CHINA);

    Assertions.assertEquals(expected, i18NService.getMessage(MESSAGE_TEST_KEY));
  }
}
