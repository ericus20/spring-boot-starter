package com.developersboard.backend.service.impl;

import com.developersboard.backend.persistent.repository.UserRepository;
import com.developersboard.constant.CacheConstants;
import com.developersboard.shared.util.UserUtils;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The implementation of service used to query user details during login.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Primary
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  /**
   * Locates the user based on the usernameOrEmail. In the actual implementation, the search may be
   * case-sensitive, or case-insensitive depending on how the implementation instance is configured.
   * In this case, the <code>UserDetails</code> object that comes back may have a usernameOrEmail
   * that is of a different case than what was actually requested..
   *
   * @param usernameOrEmail the usernameOrEmail identifying the user whose data is required.
   * @return a fully populated user record (never <code>null</code>)
   * @throws UsernameNotFoundException if the user could not be found or the user has no
   *     GrantedAuthority
   */
  @Override
  @Cacheable(key = "{ #root.methodName, #usernameOrEmail }", value = CacheConstants.USER_DETAILS)
  public UserDetails loadUserByUsername(final String usernameOrEmail) {
    // Ensure that usernameOrEmail is not empty or null.
    if (StringUtils.isNotBlank(usernameOrEmail)) {
      var storedUser =
          UserUtils.isEmail(usernameOrEmail)
              ? userRepository.findByEmail(usernameOrEmail)
              : userRepository.findByUsername(usernameOrEmail);
      if (Objects.isNull(storedUser)) {
        LOG.warn("No record found for storedUser with usernameOrEmail {}", usernameOrEmail);
        throw new UsernameNotFoundException(
            "User with usernameOrEmail " + usernameOrEmail + " not found");
      }
      return UserDetailsBuilder.buildUserDetails(storedUser);
    }
    return null;
  }
}
