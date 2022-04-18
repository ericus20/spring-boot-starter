package com.developersboard.backend.persistent.domain.user;

import com.developersboard.backend.persistent.domain.base.BaseEntity;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

/**
 * The user role model for the application.
 *
 * @author Matthew Puentes
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@Entity
@Audited
@NoArgsConstructor
@ToString(callSuper = true)
public class UserRole extends BaseEntity<Long> implements Serializable {
  @Serial private static final long serialVersionUID = 2803657434288286128L;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ToString.Exclude
  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "role_id")
  private Role role;

  /**
   * Constructor for UserRole.
   *
   * @param user user for object instantiation.
   * @param role user for object instantiation.
   */
  public UserRole(User user, Role role) {
    this.user = user;
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserRole userRole) || !super.equals(o)) {
      return false;
    }
    return Objects.equals(getUser(), userRole.getUser())
        && Objects.equals(getRole(), userRole.getRole());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getUser(), getRole());
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof UserRole;
  }
}
