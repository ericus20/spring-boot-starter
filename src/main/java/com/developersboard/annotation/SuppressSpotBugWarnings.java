package com.developersboard.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A custom annotation to suppress SpotBugs issues as needed.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Retention(RetentionPolicy.CLASS)
public @interface SuppressSpotBugWarnings {
  /**
   * The set of FindBugs warnings that are to be suppressed in annotated element. The value can be a
   * bug category, kind or pattern.
   *
   * @return the set of FindBugs warnings that are to be suppressed in annotated element
   */
  String[] value() default {};

  /**
   * Optional documentation of the reason why the warning is suppressed.
   *
   * @return the reason why the warning is suppressed
   */
  String justification() default "";
}
