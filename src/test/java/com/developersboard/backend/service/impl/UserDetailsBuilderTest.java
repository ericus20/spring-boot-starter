package com.developersboard.backend.service.impl;

import com.developersboard.backend.persistent.domain.user.User;
import com.developersboard.enums.RoleType;
import com.developersboard.shared.util.UserUtils;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.security.core.userdetails.UserDetails;

class UserDetailsBuilderTest {

  private transient User user;

  @BeforeEach
  void setUp(TestInfo testInfo) {
    user = UserUtils.createUser(testInfo.getDisplayName(), RoleType.ROLE_USER);
  }

  @Test
  void shouldSuccessfullyBuildAUserDetailsGivenUser(TestInfo testInfo) {
    var userDetails = UserDetailsBuilder.buildUserDetails(user);

    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(userDetails);
          Assertions.assertFalse(userDetails.isAccountNonExpired());
          Assertions.assertFalse(userDetails.isAccountNonLocked());
          Assertions.assertFalse(userDetails.isCredentialsNonExpired());
          Assertions.assertFalse(userDetails.isEnabled());
          MatcherAssert.assertThat(userDetails, CoreMatchers.instanceOf(UserDetails.class));
          Assertions.assertEquals(testInfo.getDisplayName(), userDetails.getUsername());
        });
  }

  @Test
  void shouldThrowExceptionWhenInputIsNull() {
    Assertions.assertThrows(
        NullPointerException.class, () -> UserDetailsBuilder.buildUserDetails(null));
  }

  @Test
  void equalsContract() {
    User client = UserUtils.createUser();
    User admin = UserUtils.createUser();

    EqualsVerifier.forClass(UserDetailsBuilder.class)
        .withRedefinedSuperclass()
        .withPrefabValues(User.class, client, admin)
        .suppress(Warning.NONFINAL_FIELDS)
        .withOnlyTheseFields("publicId", "username", "email")
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(UserDetailsBuilder.class)
        .withClassName(NameStyle.SIMPLE_NAME)
        .verify();
  }
}
