package com.developersboard.shared.util;

import com.developersboard.TestUtils;
import com.developersboard.backend.persistent.domain.user.User;
import com.developersboard.enums.RoleType;
import com.developersboard.shared.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.platform.commons.util.ReflectionUtils;

class UserUtilsTest extends TestUtils {

  @Test
  void callingConstructorShouldThrowException() {
    Assertions.assertThrows(
        AssertionError.class, () -> ReflectionUtils.newInstance(UserUtils.class));
  }

  @Test
  void createUserWithNoArgument() {
    User user = UserUtils.createUser();
    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(user.getUsername());
          Assertions.assertNotNull(user.getPassword());
          Assertions.assertNotNull(user.getEmail());
        });
  }

  @Test
  void createUserWithAnArgument(TestInfo testInfo) {
    User user = UserUtils.createUser(testInfo.getDisplayName());
    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(user.getUsername());
          Assertions.assertNotNull(user.getPassword());
          Assertions.assertNotNull(user.getEmail());

          Assertions.assertEquals(testInfo.getDisplayName(), user.getUsername());
        });
  }

  @Test
  void createUserWithFourParameters() {

    UserDto userDto =
        UserUtils.createUserDto(
            FAKER.name().username(),
            FAKER.elderScrolls().city(),
            FAKER.pokemon().name(),
            Boolean.TRUE);

    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(userDto.getUsername());
          Assertions.assertNotNull(userDto.getPassword());
          Assertions.assertNotNull(userDto.getEmail());
          Assertions.assertTrue(userDto.isEnabled());
        });
  }

  @Test
  void convertToUserDto() {
    User user = UserUtils.createUser();
    var userDto = UserUtils.convertToUserDto(user);

    Assertions.assertAll(
        () -> {
          Assertions.assertEquals(user.getUsername(), userDto.getUsername());
          Assertions.assertNotNull(userDto.getPassword());
          Assertions.assertNotNull(userDto.getEmail());
        });
  }

  @Test
  void convertToUserDtoWithNullCollections() {
    User user = UserUtils.createUser();
    user.setUserRoles(null);
    user.setUserHistories(null);
    var userDto = UserUtils.convertToUserDto(user);

    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(userDto.getUserRoles());
          Assertions.assertNotNull(userDto.getUserHistories());
        });
  }

  @Test
  void convertToUser() {
    User user = UserUtils.createUser();
    var userDto = UserUtils.convertToUserDto(user);

    var userFromUserDto = UserUtils.convertToUser(userDto);
    Assertions.assertEquals(user, userFromUserDto);
  }

  @Test
  void convertToUserWithNullCollections() {
    User user = UserUtils.createUser();
    var userDto = UserUtils.convertToUserDto(user);
    userDto.setUserRoles(null);
    userDto.setUserHistories(null);

    var userFromUserDto = UserUtils.convertToUser(userDto);
    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(userFromUserDto.getUserRoles());
          Assertions.assertNotNull(userFromUserDto.getUserHistories());
        });
  }

  @Test
  void shouldThrowExceptionWhenUserDtoInputIsNull() {
    Assertions.assertThrows(NullPointerException.class, () -> UserUtils.convertToUser(null));
  }

  @Test
  void retrieveRolesFromUserRole(TestInfo testInfo) {
    User user = UserUtils.createUser(testInfo.getDisplayName(), RoleType.ROLE_ADMIN);
    UserUtils.getRoles(user.getUserRoles());

    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(user);
          Assertions.assertNotNull(user.getUserRoles());
        });
  }
}
