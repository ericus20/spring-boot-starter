package com.developersboard.shared.util;

import com.developersboard.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.platform.commons.util.ReflectionUtils;

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
  void testingIsUserAuthenticatedAsAnonymous(TestInfo testInfo) {
    TestUtils.setAuthentication(TestUtils.ANONYMOUS_USER, TestUtils.ROLE_ANONYMOUS);
    Assertions.assertFalse(SecurityUtils.isAuthenticated());
  }

  @Test
  void testingIsUserAuthenticatedAuthenticated(TestInfo testInfo) {
    TestUtils.setAuthentication(testInfo.getDisplayName(), TestUtils.ROLE_USER);
    Assertions.assertTrue(SecurityUtils.isAuthenticated());
  }
}
