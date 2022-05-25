package com.developersboard.web.payload.pojo;

import java.io.Serial;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * The SeparateDateFormat is used to describe how long a user history has been in seconds, minutes,
 * hours and days.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
public class SeparateDateFormat implements Serializable {
  @Serial private static final long serialVersionUID = 4816694102146369185L;

  private long seconds;
  private long minutes;
  private long hours;
  private long days;
  private long weeks;
  private long months;
}
