package com.developersboard.backend.service.user;

import com.developersboard.backend.persistent.domain.user.Role;
import com.developersboard.backend.persistent.domain.user.User;
import com.developersboard.backend.persistent.repository.UserRepository;
import com.developersboard.backend.service.user.impl.UserServiceImpl;
import com.developersboard.enums.RoleType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

  @InjectMocks private transient UserServiceImpl userService;

  @Mock private transient RoleService roleService;

  @Mock private transient UserRepository userRepository;

  @Mock private transient PasswordEncoder passwordEncoder;

  private transient UserDto userDto;
  private transient User user;

  @BeforeEach
  void setUp(TestInfo testInfo) {
    MockitoAnnotations.openMocks(this);
    userDto = UserUtils.createUserDto(testInfo.getDisplayName());
    user = UserUtils.convertToUser(userDto);
  }

  @Test
  void createUserNotExistingWithDefaultRoleAsClient() {

    var role = new Role(RoleType.ROLE_USER);
    Mockito.when(roleService.getRoleByName(ArgumentMatchers.anyString())).thenReturn(role);
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
