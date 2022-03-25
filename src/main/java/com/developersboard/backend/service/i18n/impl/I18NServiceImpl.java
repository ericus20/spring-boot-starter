package com.developersboard.backend.service.i18n.impl;

import com.developersboard.backend.service.i18n.I18NService;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

/**
 * Provides public methods for internationalization.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Service
public class I18NServiceImpl implements I18NService {
  /** The message source. */
  private final transient MessageSource source;

  /**
   * Constructor injection of the message source bean.
   *
   * @param messageSource the source.
   */
  @Autowired
  public I18NServiceImpl(final MessageSource messageSource) {
    this.source = messageSource;
  }

  /**
   * Try to resolve the message. Return default message if no message was found.
   *
   * @param code he code to lookup up, such as 'calculator.noRateSet'. Users of this class are
   *     encouraged to base message names on the relevant fully qualified class name, thus avoiding
   *     conflict and ensuring maximum clarity.
   * @return the resolved message if the lookup was successful; otherwise the default message passed
   *     as a parameter.
   */
  @Override
  public String getMessage(final String code) {
    Locale locale = LocaleContextHolder.getLocale();
    return getMessage(code, new Object[] {}, locale);
  }

  /**
   * Try to resolve the message. Treat as an error if the message can't be found.
   *
   * @param code the code to lookup up, such as 'calculator.noRateSet'
   * @param args an array of arguments that will be filled in for params within the message (params
   *     look like "{0}", "{1,date}", "{2,time}" within a message), or {@code null} if none.
   * @param locale the locale in which to do the lookup
   * @return the resolved message
   * @throws NoSuchMessageException if the message wasn't found
   */
  @Override
  public String getMessage(final String code, final Object[] args, final Locale locale) {
    return source.getMessage(code, args, locale);
  }
}
