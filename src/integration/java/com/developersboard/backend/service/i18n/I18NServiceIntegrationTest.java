package com.developersboard.backend.service.i18n;

import com.developersboard.IntegrationTestUtils;
import java.util.Locale;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

class I18NServiceIntegrationTest extends IntegrationTestUtils {

  private static final String MESSAGE_TEST_KEY = "message.test";

  @Test
  void getMessage() {
    String expected = "This is a test message.";
    LocaleContextHolder.setDefaultLocale(Locale.ENGLISH);

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
