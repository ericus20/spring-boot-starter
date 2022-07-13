package com.developersboard.backend.persistent.domain.base;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * BaseEntity class allows an entity to inherit common properties from it.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity<T extends Serializable> {

  private static final String SEQUENCE_NAME = "SpringBootStarterSequence";
  private static final String SEQUENCE_INITIAL_VALUE = "1";
  private static final String STRATEGY =
      "com.developersboard.backend.persistent.domain.base.AssignedSequenceStyleGenerator";
  private static final String SEQUENCE_GENERATOR_NAME = "SpringBootStarterSequenceGenerator";

  /**
   * Sequence Style Generator to auto generate ID based on the choices in the parameters.
   *
   * @see AssignedSequenceStyleGenerator
   */
  @GenericGenerator(
      name = SEQUENCE_GENERATOR_NAME,
      strategy = STRATEGY,
      parameters = {
        @Parameter(name = "sequence_name", value = SEQUENCE_NAME),
        @Parameter(name = "initial_value", value = SEQUENCE_INITIAL_VALUE),
        @Parameter(name = "increment_size", value = SEQUENCE_INITIAL_VALUE)
      })
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_GENERATOR_NAME)
  private T id;

  @Column(unique = true, nullable = false)
  @NotBlank(message = "Public facing id is needed for all entities")
  private String publicId;

  /** Manages the version of Entities to measure the amount of modifications made to this entity. */
  @Version private short version;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @CreatedBy
  @Column(nullable = false, updatable = false)
  private String createdBy;

  @Column @LastModifiedDate private LocalDateTime updatedAt;

  @Column @LastModifiedBy private String updatedBy;

  /**
   * Evaluate the equality of BaseEntity class.
   *
   * @param o is the other object use in equality test.
   * @return the equality of both objects.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BaseEntity<?> that)) {
      return false;
    }
    if (!that.canEqual(this)) {
      return false;
    }
    return Objects.equals(getVersion(), that.getVersion())
        && Objects.equals(getPublicId(), that.getPublicId());
  }

  /**
   * This method is meant for allowing to redefine equality on several levels of the class hierarchy
   * while keeping its contract.
   *
   * @see <a href="https://www.artima.com/articles/how-to-write-an-equality-method-in-java">More</a>
   * @param other is the other object use in equality test.
   * @return if the other object can be equal to this object.
   */
  protected boolean canEqual(Object other) {
    return other instanceof BaseEntity;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getVersion(), getPublicId());
  }

  /**
   * A callback to assign a random UUID to publicId.
   *
   * <p>Assign a public id to the user. This is used to identify the user in the system and can be
   * shared publicly over the internet.
   */
  @PrePersist
  private void onCreate() {
    if (Objects.isNull(getPublicId())) {
      setPublicId(UUID.randomUUID().toString());
    }
  }
}
