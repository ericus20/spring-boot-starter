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

  /**
   * Returns a deep copy of createdAt.
   *
   * @return the createdAt
   */
  public LocalDateTime getCreatedAt() {
    return SerializationUtils.clone(createdAt);
  }

  /**
   * Sets a deep copy of the createdAt provided.
   *
   * @param createdAt the createdAt to set
   */
  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = SerializationUtils.clone(createdAt);
  }

  /**
   * Returns a deep copy of updatedAt.
   *
   * @return the updatedAt
   */
  public LocalDateTime getUpdatedAt() {
    return SerializationUtils.clone(updatedAt);
  }

  /**
   * Sets a deep copy of the updatedAt provided.
   *
   * @param updatedAt the updatedAt to set
   */
  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = SerializationUtils.clone(updatedAt);
  }
}
