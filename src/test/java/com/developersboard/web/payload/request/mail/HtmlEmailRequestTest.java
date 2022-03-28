package com.developersboard.web.payload.request.mail;

import com.developersboard.backend.persistent.domain.user.User;
import com.developersboard.shared.util.UserUtils;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class HtmlEmailRequestTest {

  @Test
  void equalsContract() {
    var user = UserUtils.createUser();
    var admin = UserUtils.createUser();

    EqualsVerifier.forClass(HtmlEmailRequest.class)
        .withRedefinedSuperclass()
        .withPrefabValues(User.class, user, admin)
        // No need to make the fields final as they might be updated
        .suppress(Warning.NONFINAL_FIELDS)
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(HtmlEmailRequest.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
