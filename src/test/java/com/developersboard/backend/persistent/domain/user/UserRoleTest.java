package com.developersboard.backend.persistent.domain.user;

import com.developersboard.TestUtils;
import com.developersboard.enums.RoleType;
import com.developersboard.shared.util.UserUtils;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class UserRoleTest {

  @Test
  void equalsContract() {
    User client = UserUtils.createUser();
    User admin = UserUtils.createUser();

    Role roleClient = new Role(RoleType.ROLE_USER);
    Role roleAdmin = new Role(RoleType.ROLE_ADMIN);

    EqualsVerifier.forClass(UserRole.class)
        .withRedefinedSuperclass()
        .withPrefabValues(User.class, client, admin)
        .withPrefabValues(Role.class, roleClient, roleAdmin)
        .withIgnoredFields(TestUtils.getIgnoredFields().toArray(new String[0]))
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(UserRole.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .withIgnoredFields("user", "role")
        .verify();
  }
}
