package com.developersboard.web.payload.request;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class LoginRequestTest {

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(LoginRequest.class)
        // No need to make the fields final as they might be updated
        .suppress(Warning.NONFINAL_FIELDS)
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(LoginRequest.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
