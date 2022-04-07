package com.developersboard.backend.persistent.domain.user;

import com.developersboard.backend.persistent.domain.base.BaseEntity;
import com.developersboard.constant.ErrorConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;

/**
 * The user model for the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Entity
@Getter
@Setter
@Audited
@Table(name = "users")
@ToString(callSuper = true)
public class User extends BaseEntity<Long> implements Serializable {
  @Serial private static final long serialVersionUID = 7538542321562810251L;

  @Column(unique = true, nullable = false)
  @NotBlank(message = ErrorConstants.BLANK_USERNAME)
  @Size(min = 3, max = 50, message = ErrorConstants.USERNAME_SIZE)
  private String username;

  @Column(unique = true, nullable = false)
  @NotBlank(message = ErrorConstants.BLANK_EMAIL)
  @Email(message = ErrorConstants.INVALID_EMAIL)
  private String email;

  @JsonIgnore
  @ToString.Exclude
  @NotBlank(message = ErrorConstants.BLANK_PASSWORD)
  private String password;

  private String firstName;
  private String middleName;
  private String lastName;
  private String phone;
  private String verificationToken;

  private boolean enabled;
  private boolean accountNonExpired;
  private boolean accountNonLocked;
  private boolean credentialsNonExpired;

  @ToString.Exclude
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<UserRole> userRoles = new HashSet<>();

  @ToString.Exclude
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<UserHistory> userHistories = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof User user) || !super.equals(o)) {
      return false;
    }
    return Objects.equals(getPublicId(), user.getPublicId())
        && Objects.equals(getUsername(), user.getUsername())
        && Objects.equals(getEmail(), user.getEmail());
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof User;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getPublicId(), getUsername(), getEmail());
  }

  /**
   * Add userRole to this User.
   *
   * @param userRole userRole to be added if not already present.
   */
  public void addUserRole(UserRole userRole) {
    userRoles.add(userRole);
    userRole.setUser(this);
  }

  /**
   * Remove userRole from this User.
   *
   * @param userRole userRole to be removed if present.
   */
  public void removeUserRole(UserRole userRole) {
    userRoles.remove(userRole);
    userRole.setUser(null);
  }

  /**
   * Add a UserHistory to this user.
   *
   * @param userHistory userHistory to be added.
   */
  public void addUserHistory(UserHistory userHistory) {
    userHistories.add(userHistory);
    userHistory.setUser(this);
  }

  /**
   * Formulates the full name of the user.
   *
   * @return the full name of the user
   */
  public String getName() {
    return StringUtils.joinWith(StringUtils.SPACE, getFirstName(), getMiddleName(), getLastName());
  }
}
