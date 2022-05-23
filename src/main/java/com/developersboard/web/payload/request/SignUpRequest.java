package com.developersboard.web.payload.request;

import com.developersboard.constant.user.UserConstants;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This class models the format of the signUp request allowed through the controller endpoints.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class SignUpRequest {

  @EqualsAndHashCode.Include
  @NotBlank(message = UserConstants.BLANK_USERNAME)
  @Size(min = 3, max = 50, message = UserConstants.USERNAME_SIZE)
  private String username;

  @Size(max = 60)
  @EqualsAndHashCode.Include
  @Email(message = UserConstants.INVALID_EMAIL)
  @NotBlank(message = UserConstants.BLANK_EMAIL)
  private String email;

  @ToString.Exclude
  @NotBlank(message = UserConstants.BLANK_PASSWORD)
  @Size(min = 4, max = 15, message = UserConstants.PASSWORD_SIZE)
  private String password;
}
