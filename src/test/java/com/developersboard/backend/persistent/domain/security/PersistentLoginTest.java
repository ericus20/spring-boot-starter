package com.developersboard.backend.persistent.domain.security;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

class PersistentLoginTest {

  @Test
  void testCreatePersistentLoginThenVerifyFieldsSet(TestInfo testInfo) {
    var persistence = new PersistentLogin();
    persistence.setSeries(testInfo.getDisplayName());

    Assertions.assertNotNull(persistence.getSeries());
  }

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(PersistentLogin.class)
        .withIgnoredFields("series", "token", "lastUsed")
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(PersistentLogin.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
