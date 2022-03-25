package com.developersboard.backend.persistent.domain.base;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApplicationAuditorAwareTest {

  private static final String SYSTEM_USER = "system";

  @Test
  void getCurrentAuditor() {
    var centralizedAuditorAware = new ApplicationAuditorAware();
    Assertions.assertEquals(SYSTEM_USER, centralizedAuditorAware.getCurrentAuditor().orElse(null));
  }

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(ApplicationAuditorAware.class).verify();
  }
}
