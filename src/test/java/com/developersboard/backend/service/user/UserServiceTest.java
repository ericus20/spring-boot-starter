package com.developersboard.backend.service.user;

import com.developersboard.backend.persistent.domain.user.Role;
import com.developersboard.backend.persistent.domain.user.User;
import com.developersboard.backend.persistent.repository.UserRepository;
import com.developersboard.backend.service.user.impl.UserServiceImpl;
import com.developersboard.enums.RoleType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks private transient UserServiceImpl userService;

  @Mock private transient RoleService roleService;

  @Mock private transient UserRepository userRepository;

  @Mock private transient PasswordEncoder passwordEncoder;

  @Mock private transient Clock clock;

  private transient UserDto userDto;
  private transient User user;

  @BeforeEach
  void setUp(TestInfo testInfo) {
    userDto = UserUtils.createUserDto(testInfo.getDisplayName());
    user = UserUtils.convertToUser(userDto);
  }

  @Test
  void createUserNotExistingWithDefaultRoleAsClient() {

    var role = new Role(RoleType.ROLE_USER);
    Mockito.when(roleService.findByName(ArgumentMatchers.anyString())).thenReturn(role);
    Mockito.when(userRepository.findByEmail(userDto.getEmail())).thenReturn(null);
    Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
    Mockito.when(passwordEncoder.encode(userDto.getPassword())).thenReturn(userDto.getPassword());

    var storedUserDetails = userService.createUser(userDto);
    storedUserDetails.setPublicId(userDto.getPublicId());

    Assertions.assertEquals(userDto, storedUserDetails);
  }

  @Test
  void createUserWithNullShouldThrowNullPointerException() {
    Assertions.assertThrows(NullPointerException.class, () -> userService.createUser(null));
  }

  @Test
  void createUserWithNullsShouldThrowNullPointerException() {
    Assertions.assertThrows(NullPointerException.class, () -> userService.createUser(null, null));
  }

  @Test
  void getUserByUsername() {
    Mockito.when(userRepository.findByUsername(userDto.getUsername())).thenReturn(user);

    UserDto storedUserDetails = userService.findByUsername(this.userDto.getUsername());
    Assertions.assertEquals(userDto, storedUserDetails);
  }

  // Test find all users that failed to verify their email after a certain time.
  @Test
  void findAllNotEnabledAfterAllowedDays() {
    var user = UserUtils.createUser();

    var fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    Mockito.doReturn(fixedClock.instant()).when(clock).instant();
    Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();

    Mockito.when(userRepository.findByEnabledFalseAndCreatedAtBefore(ArgumentMatchers.any()))
        .thenReturn(List.of(user));

    List<UserDto> users = userService.findAllNotEnabledAfterAllowedDays();
    Assertions.assertTrue(users.contains(UserUtils.convertToUserDto(user)));
  }

  @Test
  void getUserByUsernameWithNullShouldThrowNullPointerException() {
    Assertions.assertThrows(NullPointerException.class, () -> userService.findByUsername(null));
  }

  @Test
  void testGetUserByIdThrowsExceptionOnNullInput() {
    Assertions.assertThrows(NullPointerException.class, () -> userService.findById(null));
  }

  @Test
  void testGetUserByEmailThrowsExceptionOnNullInput() {
    Assertions.assertThrows(NullPointerException.class, () -> userService.findByEmail(null));
  }

  @Test
  void testGetUserByPublicIdThrowsExceptionOnNullInput() {
    Assertions.assertThrows(NullPointerException.class, () -> userService.findByPublicId(null));
  }

  @Test
  void enableUserNotWithNullPublicId() {
    Assertions.assertThrows(NullPointerException.class, () -> userService.enableUser(null));
  }

  @Test
  void disableUserNotWithNullPublicId() {
    Assertions.assertThrows(NullPointerException.class, () -> userService.disableUser(null));
  }

  @Test
  void testDeleteUserByPublicIdThrowsExceptionOnNullInput() {
    Assertions.assertThrows(NullPointerException.class, () -> userService.deleteUser(null));
  }
}
