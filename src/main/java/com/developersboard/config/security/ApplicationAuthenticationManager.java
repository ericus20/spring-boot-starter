package com.developersboard.config.security;

import com.developersboard.shared.util.core.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * ApplicationAuthenticationManager class provides implementation for AuthenticationManager.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class ApplicationAuthenticationManager implements AuthenticationManager {
  private final UserDetailsService userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    var userDetail = userDetailsService.loadUserByUsername(authentication.getName());

    SecurityUtils.authenticateUser(userDetail);

    return SecurityUtils.getAuthentication();
  }
}
