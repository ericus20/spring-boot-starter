package com.developersboard.backend.persistent.domain.user;

import com.developersboard.backend.persistent.domain.base.BaseEntity;
import com.developersboard.constant.user.UserConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
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
import org.hibernate.envers.NotAudited;

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
  @NotBlank(message = UserConstants.BLANK_USERNAME)
  @Size(min = 3, max = 50, message = UserConstants.USERNAME_SIZE)
  private String username;

  @Column(unique = true, nullable = false)
  @NotBlank(message = UserConstants.BLANK_EMAIL)
  @Email(message = UserConstants.INVALID_EMAIL)
  private String email;

  @JsonIgnore
  @ToString.Exclude
  @NotBlank(message = UserConstants.BLANK_PASSWORD)
  private String password;

  private String firstName;
  private String middleName;
  private String lastName;
  private String phone;
  private String profileImage;
  private String verificationToken;

  private int failedLoginAttempts;
  private LocalDateTime lastSuccessfulLogin;

  private boolean enabled;
  private boolean accountNonExpired;
  private boolean accountNonLocked;
  private boolean credentialsNonExpired;

  @NotAudited
  @ToString.Exclude
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<UserRole> userRoles = new HashSet<>();

  @NotAudited
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
   * @param user the user
   * @param role the role
   */
  public void addUserRole(final User user, final Role role) {
    var userRole = new UserRole(user, role);
    userRoles.add(new UserRole(user, role));
    userRole.setUser(this);
  }

  /**
   * Remove userRole from this User.
   *
   * @param user the user
   * @param role the role
   */
  public void removeUserRole(final User user, final Role role) {
    var userRole = new UserRole(user, role);
    userRoles.remove(userRole);
    userRole.setUser(null);
  }

  /**
   * Add a UserHistory to this user.
   *
   * @param userHistory userHistory to be added.
   */
  public void addUserHistory(final UserHistory userHistory) {
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
