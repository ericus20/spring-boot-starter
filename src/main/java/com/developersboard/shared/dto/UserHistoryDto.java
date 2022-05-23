package com.developersboard.shared.dto;

import com.developersboard.enums.UserHistoryType;
import com.developersboard.web.payload.pojo.SeparateDateFormat;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The UserHistoryDto transfers user history from outside into the application and vice versa.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class UserHistoryDto extends BaseDto implements Serializable {
  @Serial private static final long serialVersionUID = -8842211126703873453L;

  private UserHistoryType userHistoryType;
  private String timeElapsedDescription;
  private SeparateDateFormat separateDateFormat;
}
