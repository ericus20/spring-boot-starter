package com.developersboard.backend.bootstrap;

import com.developersboard.backend.persistent.domain.user.Role;
import com.developersboard.backend.persistent.repository.RoleRepository;
import com.developersboard.backend.service.user.UserService;
import com.developersboard.constant.ProfileTypeConstants;
import com.developersboard.enums.RoleType;
import com.developersboard.enums.UserHistoryType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * A convenient class to initializes and save user data on application start.
 *
 * @author George Anguah
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

  private final Environment environment;
  private final UserService userService;
  private final RoleRepository roleRepository;

  @Value("${admin.username}")
  private String adminUsername;

  @Value("${admin.email}")
  private String adminEmail;

  @Value("${admin.password}")
  private String adminPassword;

  @Override
  public void run(String... args) {
    Arrays.stream(RoleType.values())
        .forEach(roleTypeValue -> roleRepository.save(new Role(roleTypeValue)));

    // only run these initial data if we are not in test mode.
    if (!Arrays.asList(environment.getActiveProfiles()).contains(ProfileTypeConstants.TEST)) {
      persistDefaultAdminUser();
    }
  }

  private void persistDefaultAdminUser() {
    var adminDto = UserUtils.createUserDto(adminUsername, adminPassword, adminEmail, true);
    Set<RoleType> adminRoleType = Collections.singleton(RoleType.ROLE_ADMIN);

    UserDto user = userService.createUser(adminDto, adminRoleType);
    user.setLastName("Test");

    userService.updateUser(user, UserHistoryType.PROFILE_UPDATE);
  }
}
