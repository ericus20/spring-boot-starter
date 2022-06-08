package com.developersboard.backend.service.impl;

import com.developersboard.backend.persistent.domain.user.User;
import com.developersboard.constant.user.UserConstants;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.Validate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * UserDetailsBuilder builds the userDetails to be used by the application security context.
 *
 * @author George Anguah
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class UserDetailsBuilder implements UserDetails {
  @Serial private static final long serialVersionUID = -8755873164632782035L;

  private Long id;
  @EqualsAndHashCode.Include private String email;
  @EqualsAndHashCode.Include private String publicId;
  @EqualsAndHashCode.Include private String username;
  private String firstName;
  private String lastName;
  private String password;
  private String phone;
  private boolean enabled;
  private boolean accountNonExpired;
  private boolean accountNonLocked;
  private boolean credentialsNonExpired;
  private int failedLoginAttempts;
  private LocalDateTime lastSuccessfulLogin;

  private Collection<? extends GrantedAuthority> authorities;

  /**
   * Builds userDetails object from the specified user.
   *
   * @param user the user
   * @throws NullPointerException if the user is null
   * @return the userDetails
   */
  public static UserDetailsBuilder buildUserDetails(final User user) {
    Validate.notNull(user, UserConstants.USER_MUST_NOT_BE_NULL);

    // Build the authorities from the user's roles
    Set<GrantedAuthority> authorities = new HashSet<>();
    user.getUserRoles()
        .forEach(
            userRole -> {
              if (Objects.nonNull(userRole.getRole())) {
                authorities.add(new SimpleGrantedAuthority(userRole.getRole().getName()));
              }
            });

    return UserDetailsBuilder.builder()
        .id(user.getId())
        .email(user.getEmail())
        .publicId(user.getPublicId())
        .username(user.getUsername())
        .password(user.getPassword())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .publicId(user.getPublicId())
        .enabled(user.isEnabled())
        .failedLoginAttempts(user.getFailedLoginAttempts())
        .lastSuccessfulLogin(user.getLastSuccessfulLogin())
        .accountNonExpired(user.isAccountNonExpired())
        .accountNonLocked(user.isAccountNonLocked())
        .credentialsNonExpired(user.isCredentialsNonExpired())
        .authorities(authorities)
        .build();
  }
}
