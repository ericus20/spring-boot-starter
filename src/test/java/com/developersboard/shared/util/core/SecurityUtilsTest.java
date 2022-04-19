package com.developersboard.shared.util.core;

import com.developersboard.TestUtils;
import com.developersboard.backend.service.impl.UserDetailsBuilder;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;

class SecurityUtilsTest {

  @BeforeEach
  void setUp() {
    SecurityUtils.clearAuthentication();
  }

  @Test
  void callingConstructorShouldThrowException() {
    Assertions.assertThrows(
        AssertionError.class, () -> ReflectionUtils.newInstance(SecurityUtils.class));
  }

  @Test
  void testingIsUserAuthenticatedNull() {
    Assertions.assertFalse(SecurityUtils.isAuthenticated(null));
  }

  @Test
  void testingIsUserAuthenticatedNotAuthenticated() {
    Authentication authentication = SecurityUtils.getAuthentication();
    Assertions.assertAll(
        () -> {
          Assertions.assertFalse(SecurityUtils.isAuthenticated());
          Assertions.assertFalse(SecurityUtils.isAuthenticated(authentication));
        });
  }

  @Test
  void testingIsUserAuthenticatedAsAnonymous() {
    TestUtils.setAuthentication(TestUtils.ANONYMOUS_USER, TestUtils.ANONYMOUS_ROLE);
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

  @Test
  void getAuthorizedUserDtoWithoutAuthenticationThrowsException() {
    SecurityUtils.clearAuthentication();
    Assertions.assertThrows(NullPointerException.class, SecurityUtils::getAuthorizedUserDto);
  }

  @Test
  void getAuthorizedUserDetailsWithoutAuthenticationReturnsNull() {
    SecurityUtils.clearAuthentication();
    Assertions.assertNull(SecurityUtils.getAuthenticatedUserDetails());
  }

  @Test
  void authenticateUserWithNullUserDetails() {
    var authManager = Mockito.mock(AuthenticationManager.class);

    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(authManager);
          Assertions.assertDoesNotThrow(() -> SecurityUtils.authenticateUser(null));
        });
  }

  @Test
  void authenticateUserWithUserDetails() {
    var userDetails = UserDetailsBuilder.buildUserDetails(UserUtils.createUser(true));

    SecurityUtils.authenticateUser(userDetails);

    Assertions.assertTrue(SecurityUtils.isAuthenticated());
  }

  @Test
  void authenticateUserWithNullHttpServletRequestAndUserDetails() {
    SecurityUtils.authenticateUser(null, null);

    Assertions.assertFalse(SecurityUtils.isAuthenticated());
  }

  @Test
  void authenticateUserWithNullHttpServletRequest() {
    var userDetails = UserDetailsBuilder.buildUserDetails(UserUtils.createUser(true));

    SecurityUtils.authenticateUser(null, userDetails);

    Assertions.assertFalse(SecurityUtils.isAuthenticated());
  }

  @Test
  void authenticateUserWithHttpServletRequestAndUserDetails() {
    var userDetails = UserDetailsBuilder.buildUserDetails(UserUtils.createUser(true));
    var httpServletRequest = Mockito.mock(HttpServletRequest.class);

    SecurityUtils.authenticateUser(httpServletRequest, userDetails);

    Assertions.assertTrue(SecurityUtils.isAuthenticated());
  }
}
