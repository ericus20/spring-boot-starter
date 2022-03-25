package com.developersboard.shared.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.SerializationUtils;

/**
 * The BaseDto provides base fields to be extended.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BaseDto {
  private Long id;

  @EqualsAndHashCode.Include private int version;
  private LocalDateTime createdAt;
  private String createdBy;
  private LocalDateTime updatedAt;
  private String updatedBy;

  public LocalDateTime getCreatedAt() {
    return SerializationUtils.clone(createdAt);
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = SerializationUtils.clone(createdAt);
  }

  public LocalDateTime getUpdatedAt() {
    return SerializationUtils.clone(updatedAt);
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = SerializationUtils.clone(updatedAt);
  }
}
