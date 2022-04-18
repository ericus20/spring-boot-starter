package com.developersboard;

import com.developersboard.backend.service.user.UserService;
import com.developersboard.constant.ProfileTypeConstants;
import com.developersboard.enums.RoleType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import com.github.javafaker.Faker;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ProfileTypeConstants.TEST})
public abstract class IntegrationTestUtils {
  protected static final Faker FAKER = new Faker();

  /**
   * Creates and verify user with flexible field creation.
   *
   * @param userService the userService
   * @param username if a custom username is needed
   * @param enabled if the user should be enabled
   * @return persisted user
   */
  protected UserDto createAndAssertUser(UserService userService, String username, boolean enabled) {
    UserDto userDto = UserUtils.createUserDto(username);
    UserDto persistedUser = persistUser(userService, enabled, userDto);
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
        persistUser(userService, userDto.isEnabled(), SerializationUtils.clone(userDto));
    Assertions.assertNotNull(persistUser);
    Assertions.assertNotNull(persistUser.getId());
    Assertions.assertFalse(persistUser.getUserRoles().isEmpty());

    return persistUser;
  }

  /**
   * Creates and verify user with flexible field creation.
   *
   * @param userService the userService
   * @param userDto the userDto
   * @return persisted user
   */
  protected UserDto createAndAssertAdmin(UserService userService, UserDto userDto) {
    Set<RoleType> roleTypes = new HashSet<>();

    roleTypes.add(RoleType.ROLE_ADMIN);

    if (userDto.isEnabled()) {
      UserUtils.enableUser(userDto);
    }
    UserDto admin = userService.createUser(SerializationUtils.clone(userDto), roleTypes);

    Assertions.assertNotNull(admin);
    Assertions.assertNotNull(admin.getId());
    Assertions.assertFalse(admin.getUserRoles().isEmpty());
    Assertions.assertTrue(
        admin.getUserRoles().stream()
            .anyMatch(role -> role.getRole().getName().equals(RoleType.ROLE_ADMIN.getName())));

    return admin;
  }

  protected UserDto persistUser(UserService userService, boolean enabled, UserDto userDto) {
    Set<RoleType> roleTypes = new HashSet<>();

    roleTypes.add(RoleType.ROLE_USER);

    if (enabled) {
      UserUtils.enableUser(userDto);
    }
    return userService.createUser(userDto, roleTypes);
  }
}
