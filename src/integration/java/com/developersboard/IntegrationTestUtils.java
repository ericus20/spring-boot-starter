package com.developersboard;

import com.developersboard.backend.service.user.UserService;
import com.developersboard.constant.ProfileTypeConstants;
import com.developersboard.enums.RoleType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import com.developersboard.web.payload.request.LoginRequest;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ProfileTypeConstants.TEST})
public abstract class IntegrationTestUtils {

  /**
   * Creates and verify user with flexible field creation.
   *
   * @param userService the userService
   * @param username if a custom username is needed
   * @param enabled if the user should be enabled
   * @return persisted user
   */
  protected UserDto createAndAssertUser(UserService userService, String username, boolean enabled) {
    return createAndAssertUser(userService, username, false, enabled);
  }

  /**
   * Creates and verify user with flexible field creation.
   *
   * @param userService the userService
   * @param username the username
   * @param isAdmin if the user should be an admin
   * @param enabled if the user should be enabled
   * @return persisted user
   */
  protected UserDto createAndAssertUser(
      UserService userService, String username, boolean isAdmin, boolean enabled) {

    UserDto userDto = UserUtils.createUserDto(username);
    UserDto persistedUser = persistUser(userService, isAdmin, enabled, userDto);
    Assertions.assertNotNull(persistedUser);
    Assertions.assertNotNull(persistedUser.getId());
    return persistedUser;
  }

  /**
   * Creates and verify user with flexible field creation.
   *
   * @param userService the userService
   * @param userDto the userDto
   * @return persisted user
   */
  protected UserDto createAndAssertUser(UserService userService, UserDto userDto) {
    UserDto persistUser =
        persistUser(userService, false, userDto.isEnabled(), SerializationUtils.clone(userDto));
    Assertions.assertNotNull(persistUser);
    Assertions.assertNotNull(persistUser.getId());
    return persistUser;
  }

  /**
   * Persists user to the database with all configurations and returns the persisted user.
   *
   * @param userService the userService
   * @param isAdmin if the user should be an admin
   * @param enabled if the user should be enabled
   * @param userDto the userDto
   * @return the persisted user
   */
  protected UserDto persistUser(
      UserService userService, boolean isAdmin, boolean enabled, UserDto userDto) {
    Set<RoleType> roleTypes = new HashSet<>();
    if (isAdmin) {
      roleTypes.add(RoleType.ROLE_ADMIN);
    }
    roleTypes.add(RoleType.ROLE_USER);

    if (enabled) {
      UserUtils.enableUser(userDto);
    }
    return userService.createUser(userDto, roleTypes);
  }

  /**
   * Creates a login request with the given username and password and converts it to JSON.
   *
   * @param username the username
   * @param password the password
   * @return the login request
   */
  protected String createLoginRequest(String username, String password) {
    var loginRequest = new LoginRequest(username, password);

    return TestUtils.toJson(loginRequest);
  }
}
