package com.developersboard.backend.service.i18n;

import java.util.Locale;
import org.springframework.context.NoSuchMessageException;

/**
 * Provides public methods for internationalization.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public interface I18NService {

  /**
   * Try to resolve the message. Return default message if no message was found.
   *
   * @param code The code to lookup up, such as 'calculator.noRateSet'. Users of this class are
   *     encouraged to base message names on the relevant fully qualified class name, thus avoiding
   *     conflict and ensuring maximum clarity.
   * @return the resolved message if the lookup was successful; otherwise the default message passed
   *     as a parameter
   */
  String getMessage(String code);

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
  String getMessage(String code, Object[] args, Locale locale);
}
