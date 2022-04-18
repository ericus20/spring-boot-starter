package com.developersboard.backend.persistent.domain.base;

import com.developersboard.TestUtils;
import java.util.Optional;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.security.core.context.SecurityContextHolder;

class ApplicationAuditorAwareTest {

  private static final String SYSTEM_USER = "system";

  @BeforeEach
  void setUp() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void getCurrentAuditor() {
    var centralizedAuditorAware = new ApplicationAuditorAware();
    Assertions.assertEquals(SYSTEM_USER, centralizedAuditorAware.getCurrentAuditor().orElse(null));
  }

  @Test
  void getCurrentAuditorWithNoAuthentication() {
    TestUtils.setAuthentication(TestUtils.ANONYMOUS_ROLE, TestUtils.ANONYMOUS_USER);
    SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    Assertions.assertEquals(SYSTEM_USER, getAuditor());
  }

  @Test
  void getCurrentAuditorWithAnonymousUser() {
    TestUtils.setAuthentication(TestUtils.ANONYMOUS_USER, TestUtils.ANONYMOUS_ROLE);
    Assertions.assertEquals(SYSTEM_USER, getAuditor());
  }

  @Test
  void getCurrentAuditorWithAuthenticatedUser(TestInfo testInfo) {
    TestUtils.setAuthentication(testInfo.getDisplayName(), TestUtils.ROLE_USER);
    Assertions.assertEquals(getAuditor(), testInfo.getDisplayName());
  }

  private String getAuditor() {
    var applicationAuditorAware = new ApplicationAuditorAware();
    Optional<String> currentAuditor = applicationAuditorAware.getCurrentAuditor();
    return currentAuditor.orElse(null);
  }

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(ApplicationAuditorAware.class).verify();
  }
}
