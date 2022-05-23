package com.developersboard.web.payload.request;

import com.developersboard.constant.user.UserConstants;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class models the format of the login request accepted.
 *
 * @author Stephen Boakye
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class LoginRequest {

  @NotBlank(message = UserConstants.BLANK_USERNAME)
  @Size(min = 3, max = 50, message = UserConstants.USERNAME_SIZE)
  private String username;

  @NotBlank(message = UserConstants.BLANK_PASSWORD)
  @Size(min = 4, message = UserConstants.PASSWORD_SIZE)
  private String password;
}
