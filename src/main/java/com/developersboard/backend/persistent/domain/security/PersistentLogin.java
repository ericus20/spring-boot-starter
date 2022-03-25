package com.developersboard.backend.persistent.domain.security;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The PersistentLogins class supports and allows user to use remember-me with persistent option.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "persistent_logins")
public class PersistentLogin implements Serializable {
  @Serial private static final long serialVersionUID = 4754399354221934299L;

  @Id
  @Column(length = 64)
  private String series;

  @Column(nullable = false, length = 64)
  private String username;

  @Column(nullable = false, length = 64)
  private String token;

  @Column(nullable = false)
  private LocalDateTime lastUsed;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PersistentLogin that)) {
      return false;
    }
    return Objects.equals(getUsername(), that.getUsername());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUsername());
  }
}
