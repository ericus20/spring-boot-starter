package com.developersboard.web.payload.request.mail;

import com.developersboard.constant.user.UserConstants;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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

  @NotBlank(message = UserConstants.BLANK_NAME)
  private String name;

  @Size(max = 60)
  @EqualsAndHashCode.Include
  @Email(message = UserConstants.INVALID_EMAIL)
  @NotBlank(message = UserConstants.BLANK_EMAIL)
  private String email;

  private String phone;
}
