package com.developersboard.web.payload.request.mail;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * The feedback pojo used as a template for user feedback journey.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
@Slf4j
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class FeedbackRequest extends EmailRequest {
  private String name;
  private String email;
  private String phone;
}
