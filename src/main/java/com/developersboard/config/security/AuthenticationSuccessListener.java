package com.developersboard.config.security;

import com.developersboard.backend.service.security.BruteForceProtectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * This handler will hand over the control to the BruteForceProtectionService to reset the failed
 * counter.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessListener
    implements ApplicationListener<AuthenticationSuccessEvent> {

  private final BruteForceProtectionService bruteForceProtectionService;

  @Override
  public void onApplicationEvent(AuthenticationSuccessEvent event) {
    String username = event.getAuthentication().getName();
    LOG.info("********* login successful for user {} ", username);

    bruteForceProtectionService.resetBruteForceCounter(username);
  }
}
