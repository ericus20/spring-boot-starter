package com.developersboard.config;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Defines the Internationalization configuration.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class I18NConfig implements WebMvcConfigurer {

  /** The number of seconds for the message source to reload. */
  private static final int CACHE_MILLIS = 1800;

  private static final String LOCALE = "locale";
  private static final String MESSAGES_LOCATION = "classpath:i18n/messages";

  /**
   * Setup properties message source.
   *
   * @return messageSource.
   */
  @Bean
  public ReloadableResourceBundleMessageSource messageSource() {
    var messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename(MESSAGES_LOCATION);
    messageSource.setCacheMillis(CACHE_MILLIS);
    messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());

    return messageSource;
  }

  /**
   * Setup localeResolver. The LocaleResolver interface has implementations that determine the
   * current locale based on the session, cookies, the Accept-Language header, or a fixed value.
   *
   * @return localResolver.
   */
  @Bean
  public LocaleResolver localeResolver() {
    CookieLocaleResolver localeResolver = new CookieLocaleResolver();
    localeResolver.setDefaultLocale(Locale.US);

    return localeResolver;
  }

  /**
   * Setup localChangeInterceptor. The LocalChangeInterceptor bean that will switch to a new locale
   * based on the value of the lang parameter appended to a request:
   *
   * @return localChangeInterceptor.
   */
  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName(LOCALE);

    return localeChangeInterceptor;
  }

  /**
   * Add localChangeInterceptor to registry interceptors. The LocalChangeInterceptor needs to be
   * added to the application’s interceptor registry.
   *
   * @param registry addInterceptors.
   */
  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
  }
}
