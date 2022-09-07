package com.developersboard.web.payload.response;

import com.developersboard.shared.dto.UserDto;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * This class models the format of the response produced in the controller endpoints.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
public class AuditResponse<T extends Serializable> implements Serializable {
  @Serial private static final long serialVersionUID = -8632756128923682589L;

  private UserDto author;

  private String publicId;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private boolean enabled;
}
