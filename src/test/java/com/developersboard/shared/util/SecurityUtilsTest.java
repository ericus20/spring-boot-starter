package com.developersboard.shared.util;

import com.developersboard.TestUtils;
import com.developersboard.backend.service.impl.UserDetailsBuilder;
import com.developersboard.shared.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

class SecurityUtilsTest {

  @Test
  void callingConstructorShouldThrowException() {
    Assertions.assertThrows(
        AssertionError.class, () -> ReflectionUtils.newInstance(SecurityUtils.class));
  }

  @Test
  void testingIsUserAuthenticatedNotAuthenticated() {
    Assertions.assertFalse(SecurityUtils.isAuthenticated());
  }

  @Test
  void testingIsUserAuthenticatedAsAnonymous() {
    TestUtils.setAuthentication(TestUtils.ANONYMOUS_USER, TestUtils.ROLE_ANONYMOUS);
    Assertions.assertFalse(SecurityUtils.isAuthenticated());
  }

  @Test
  void testingIsUserAuthenticatedAuthenticated(TestInfo testInfo) {
    TestUtils.setAuthentication(testInfo.getDisplayName(), TestUtils.ROLE_USER);
    Assertions.assertTrue(SecurityUtils.isAuthenticated());
  }

  @Test
  void testUserDisabledTrowsException() {
    Assertions.assertThrows(
        DisabledException.class,
        () ->
            SecurityUtils.validateUserDetailsStatus(
                UserDetailsBuilder.buildUserDetails(UserUtils.createUser())));
  }

  @Test
  void testUserEnabledDoesNotTrowsException() {
    Assertions.assertDoesNotThrow(
        () ->
            SecurityUtils.validateUserDetailsStatus(
                UserDetailsBuilder.buildUserDetails(UserUtils.createUser(true))));
  }

  @Test
  void testUserAccountLockedTrowsException() {
    var userDetails = UserDetailsBuilder.buildUserDetails(UserUtils.createUser(true));
    userDetails.setAccountNonLocked(false);

    Assertions.assertThrows(
        LockedException.class, () -> SecurityUtils.validateUserDetailsStatus(userDetails));
  }

  @Test
  void testUserAccountExpiredTrowsException() {
    var userDetails = UserDetailsBuilder.buildUserDetails(UserUtils.createUser(true));
    userDetails.setAccountNonExpired(false);

    Assertions.assertThrows(
        AccountExpiredException.class, () -> SecurityUtils.validateUserDetailsStatus(userDetails));
  }

  @Test
  void testUserCredentialsExpiredTrowsException() {
    var userDetails = UserDetailsBuilder.buildUserDetails(UserUtils.createUser(true));
    userDetails.setCredentialsNonExpired(false);

    Assertions.assertThrows(
        CredentialsExpiredException.class,
        () -> SecurityUtils.validateUserDetailsStatus(userDetails));
  }

  @Test
  void testingGetAuthorizedUserDto(TestInfo testInfo) {
    TestUtils.setAuthentication(testInfo.getDisplayName(), TestUtils.ROLE_USER);

    UserDto authorizedUserDto = SecurityUtils.getAuthorizedUserDto();
    Assertions.assertAll(
        () -> {
          Assertions.assertEquals(testInfo.getDisplayName(), authorizedUserDto.getUsername());
          Assertions.assertNotNull(authorizedUserDto);
          Assertions.assertTrue(SecurityUtils.isAuthenticated());
        });
  }
}
