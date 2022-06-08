package com.developersboard.backend.persistent.domain.user;

import com.developersboard.TestUtils;
import com.developersboard.enums.RoleType;
import com.developersboard.shared.util.UserUtils;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void equalsContract() {
    User client = UserUtils.createUser();
    User admin = UserUtils.createUser();

    EqualsVerifier.forClass(User.class)
        .withRedefinedSuperclass()
        .withPrefabValues(User.class, client, admin)
        .withOnlyTheseFields(TestUtils.getUserEqualsFields().toArray(new String[0]))
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(User.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .withIgnoredFields("password", "userRoles", "userHistories")
        .verify();
  }

  @Test
  void testAddUserRole() {
    User user = UserUtils.createUser();
    user.addUserRole(user, new Role(RoleType.ROLE_USER));

    Assertions.assertFalse(user.getUserRoles().isEmpty());
  }

  @Test
  void testRemoveUserRole() {
    var user = UserUtils.createUser();
    user.addUserRole(user, new Role(RoleType.ROLE_USER));
    Assertions.assertFalse(user.getUserRoles().isEmpty());

    user.removeUserRole(user, new Role(RoleType.ROLE_USER));
    Assertions.assertTrue(user.getUserRoles().isEmpty());
  }

  @Test
  void testName() {
    var user = UserUtils.createUser();
    var name =
        String.join(
            StringUtils.SPACE, user.getFirstName(), user.getMiddleName(), user.getLastName());

    Assertions.assertEquals(name, user.getName());
  }
}
