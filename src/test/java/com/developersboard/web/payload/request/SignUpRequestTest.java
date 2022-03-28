package com.developersboard.web.payload.request;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class SignUpRequestTest {

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(SignUpRequest.class)
        // No need to make the fields final as they might be updated
        .suppress(Warning.NONFINAL_FIELDS)
        .withOnlyTheseFields("username", "email")
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(SignUpRequest.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .withIgnoredFields("password")
        .withFailOnExcludedFields(true)
        .verify();
  }
}
