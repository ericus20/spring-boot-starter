package com.developersboard.config.security;

import com.developersboard.backend.service.security.BruteForceProtectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

/**
 * This class listen to specific events and will notify us in case of any authentication failures.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener
    implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

  private final BruteForceProtectionService bruteForceProtectionService;

  @Override
  public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
    String username = event.getAuthentication().getName();
    LOG.warn("********* login failed for user {} ", username);

    bruteForceProtectionService.registerLoginFailure(username);
  }
}
