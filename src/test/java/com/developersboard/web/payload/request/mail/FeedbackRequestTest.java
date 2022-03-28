package com.developersboard.web.payload.request.mail;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class FeedbackRequestTest {

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(FeedbackRequest.class)
        .withRedefinedSuperclass()
        // No need to make the fields final as they might be updated
        .suppress(Warning.NONFINAL_FIELDS)
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(FeedbackRequest.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
