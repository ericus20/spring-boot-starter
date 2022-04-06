package com.developersboard.backend.service.user;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.backend.persistent.domain.user.Role;
import com.developersboard.backend.service.impl.UserDetailsBuilder;
import com.developersboard.enums.RoleType;
import com.developersboard.enums.UserHistoryType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.StringUtils;
import com.developersboard.shared.util.UserUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;

class UserServiceIntegrationTest extends IntegrationTestUtils {

  @Autowired private transient UserService userService;

  /**
   * Test attempts to create a user, verify that user has been created successfully by checking
   * assigned id.
   */
  @Test
  void createUserWithoutSpecifiedRoles(TestInfo testInfo) {
    var userDto = UserUtils.createUserDto(testInfo.getDisplayName());
    var persistedUserDto = userService.createUser(userDto);

    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(persistedUserDto);
          Assertions.assertNotNull(persistedUserDto.getId());
          Assertions.assertEquals(userDto, persistedUserDto);

          // assert that the user now has a new USER role assigned after creation.
          Assertions.assertFalse(
              persistedUserDto.getUserRoles().stream()
                  .filter(userRole -> Objects.nonNull(userRole.getRole()))
                  .filter(userRole -> userRole.getRole().equals(new Role(RoleType.ROLE_USER)))
                  .collect(Collectors.toSet())
                  .isEmpty());
        });
  }

  /** Creating a user who exists and not enabled should return the existing user. */
  @Test
  void createUserAlreadyExistingAndNotEnabled(TestInfo testInfo) {
    // Create a new user with the test name as username
    var userDto = createAndAssertUser(userService, testInfo.getDisplayName(), false);

    // create another user using the same details from the first user "userDto"
    var existingUser = createAndAssertUser(userService, userDto);

    // Assert that the existing user is returned as is and not enabled.
    Assertions.assertEquals(userDto, existingUser);
  }

  /** Creating a user who exists and enabled should return null. */
  @Test
  void createUserAlreadyExistingAndEnabled(TestInfo testInfo) {
    // Create a new user with the test name as username with enabled set to true
    var userDto = createAndAssertUser(userService, testInfo.getDisplayName(), true);

    // since the user is enabled, create another user using the same details from the first user
    // should return null as the user already exists.
    var existingUser = persistUser(userService, false, true, userDto);

    Assertions.assertNull(existingUser);
  }

  /** Test checks that an existing user can be retrieved using the username provided. */
  @Test
  void getUserByUsername(TestInfo testInfo) {
    var userDto = createAndAssertUser(userService, testInfo.getDisplayName(), false);

    var storedUser = userService.findByUsername(userDto.getUsername());
    Assertions.assertEquals(userDto, storedUser);
  }

  @Test
  void getUserByUsernameNotExisting(TestInfo testInfo) {
    var userByUsername = userService.findByUsername(testInfo.getDisplayName());
    Assertions.assertNull(userByUsername);
  }

  @Test
  void getUserById(TestInfo testInfo) {
    var userDto = createAndAssertUser(userService, testInfo.getDisplayName(), false);

    var storedUser = userService.findById(userDto.getId());
    Assertions.assertEquals(userDto, storedUser);
  }

  @Test
  void getUserByIdNotExisting() {

    var storedUser = userService.findById(RandomUtils.nextLong());
    Assertions.assertNull(storedUser);
  }

  @Test
  void getUserByPublicId(TestInfo testInfo) {
    var userDto = createAndAssertUser(userService, testInfo.getDisplayName(), false);

    var storedUser = userService.findByPublicId(userDto.getPublicId());
    Assertions.assertEquals(userDto, storedUser);
  }

  @Test
  void getUserByPublicIdNotExisting(TestInfo testInfo) {

    var storedUser = userService.findByPublicId(testInfo.getDisplayName());
    Assertions.assertNull(storedUser);
  }

  @Test
  void getUserByEmail(TestInfo testInfo) {
    var userDto = createAndAssertUser(userService, testInfo.getDisplayName(), false);

    var userByEmail = userService.findByEmail(userDto.getEmail());
    Assertions.assertEquals(userDto, userByEmail);
  }

  @Test
  void getUserByEmailNotExisting(TestInfo testInfo) {
    var userByEmail = userService.findByEmail(testInfo.getDisplayName());
    Assertions.assertNull(userByEmail);
  }

  @Test
  void findAllNotEnabledAfterCreationDays(TestInfo testInfo) {
    UserDto userDto = createAndAssertUser(userService, testInfo.getDisplayName(), false);

    List<UserDto> users = userService.findAllNotEnabledAfterAllowedDays();
    // User was just created and should not be returned to be deleted.
    Assertions.assertFalse(users.contains(userDto));
  }

  @Test
  void getUserDetails(TestInfo testInfo) {
    var userDto = createAndAssertUser(userService, testInfo.getDisplayName(), false);

    var userDetails = userService.getUserDetails(userDto.getUsername());
    Assertions.assertTrue(userDetails instanceof UserDetailsBuilder);
  }

  @Test
  void existsByUsername(TestInfo testInfo) {
    var userDto = createAndAssertUser(userService, testInfo.getDisplayName(), false);

    var existsByUsername = userService.existsByUsername(userDto.getUsername());
    Assertions.assertTrue(existsByUsername);
  }

  @Test
  void existsByUsernameWithNullThrowsException() {
    Assertions.assertThrows(NullPointerException.class, () -> userService.existsByUsername(null));
  }

  @Test
  void existsByUsernameNotExisting(TestInfo testInfo) {
    var existsByUsername = userService.existsByUsername(testInfo.getDisplayName());
    Assertions.assertFalse(existsByUsername);
  }

  @Test
  void existsByUsernameOrEmailNotEnabled(TestInfo testInfo) {
    var user = createAndAssertUser(userService, testInfo.getDisplayName(), false);

    Assertions.assertFalse(
        userService.existsByUsernameOrEmailAndEnabled(user.getUsername(), user.getEmail()));
  }

  @Test
  void existsByUsernameOrEmailAndEnabled(TestInfo testInfo) {
    var userDto = createAndAssertUser(userService, testInfo.getDisplayName(), true);

    Assertions.assertTrue(
        userService.existsByUsernameOrEmailAndEnabled(userDto.getUsername(), userDto.getEmail()));
  }

  @Test
  void updateUser(TestInfo testInfo) {
    var userDto = createAndAssertUser(userService, testInfo.getDisplayName(), false);
    var previousFirstName = userDto.getFirstName();
    userDto.setFirstName(StringUtils.FAKER.name().firstName());

    var updatedUserDto = userService.updateUser(userDto, UserHistoryType.PROFILE_UPDATE);
    Assertions.assertNotNull(updatedUserDto.getId());
    Assertions.assertNotEquals(previousFirstName, updatedUserDto.getFirstName());
    Assertions.assertEquals(updatedUserDto.getId(), userDto.getId());

    Assertions.assertTrue(updatedUserDto.getVersion() > userDto.getVersion());
    Assertions.assertTrue(updatedUserDto.getUpdatedAt().isAfter(updatedUserDto.getCreatedAt()));
  }

  @Test
  void enableUser(TestInfo testInfo) {
    var userDto = createAndAssertUser(userService, testInfo.getDisplayName(), false);

    // User should not be enabled after creation.
    Assertions.assertFalse(userDto.isEnabled());

    // Enable the user.
    var updatedUserDto = userService.enableUser(userDto.getPublicId());
    Assertions.assertNotNull(updatedUserDto.getId());
    Assertions.assertTrue(updatedUserDto.isEnabled());
    Assertions.assertEquals(updatedUserDto.getId(), userDto.getId());

    Assertions.assertTrue(updatedUserDto.getVersion() > userDto.getVersion());
    Assertions.assertTrue(updatedUserDto.getUpdatedAt().isAfter(updatedUserDto.getCreatedAt()));
  }

  @Test
  void enableUserNotExistingDoesNothing(TestInfo testInfo) {
    Assertions.assertNull(userService.enableUser(testInfo.getDisplayName()));
  }

  @Test
  void disableUser(TestInfo testInfo) {
    var userDto = createAndAssertUser(userService, testInfo.getDisplayName(), true);

    // User should be enabled after creation.
    Assertions.assertTrue(userDto.isEnabled());

    // Disable the user.
    var updatedUserDto = userService.disableUser(userDto.getPublicId());
    Assertions.assertNotNull(updatedUserDto.getId());
    Assertions.assertFalse(updatedUserDto.isEnabled());
    Assertions.assertEquals(updatedUserDto.getId(), userDto.getId());

    Assertions.assertTrue(updatedUserDto.getVersion() > userDto.getVersion());
    Assertions.assertTrue(updatedUserDto.getUpdatedAt().isAfter(updatedUserDto.getCreatedAt()));
  }

  @Test
  void disableUserNotExistingDoesNothing(TestInfo testInfo) {
    Assertions.assertNull(userService.disableUser(testInfo.getDisplayName()));
  }

  @Test
  void deleteUser() {
    UserDto userDto = createAndAssertUser(userService, UserUtils.createUserDto(false));
    Assertions.assertTrue(userService.existsByUsername(userDto.getUsername()));

    userService.deleteUser(userDto.getPublicId());
    Assertions.assertFalse(userService.existsByUsername(userDto.getUsername()));
  }
}
