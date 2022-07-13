package com.developersboard.shared.util;

import com.developersboard.backend.persistent.domain.user.Role;
import com.developersboard.backend.persistent.domain.user.User;
import com.developersboard.backend.persistent.domain.user.UserHistory;
import com.developersboard.backend.persistent.domain.user.UserRole;
import com.developersboard.backend.service.impl.UserDetailsBuilder;
import com.developersboard.constant.ErrorConstants;
import com.developersboard.constant.user.ProfileConstants;
import com.developersboard.constant.user.UserConstants;
import com.developersboard.enums.RoleType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.dto.UserHistoryDto;
import com.developersboard.shared.dto.mapper.UserDtoMapper;
import com.developersboard.shared.dto.mapper.UserHistoryDtoMapper;
import com.developersboard.shared.util.core.ValidationUtils;
import com.developersboard.web.payload.request.SignUpRequest;
import com.developersboard.web.payload.response.UserResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * User utility class that holds methods used across application.
 *
 * @author Matthew Puentes
 * @version 1.0
 * @since 1.0
 */
public final class UserUtils {

  /** Maximum password length for the password generation. */
  public static final int PASSWORD_MAX_LENGTH = 15;
  /** The Constant FAKER. */
  private static final Faker FAKER = new Faker();
  /** Minimum password length for the password generation. */
  private static final int PASSWORD_MIN_LENGTH = 4;

  private UserUtils() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }

  /**
   * Create a user.
   *
   * @return a user
   */
  public static User createUser() {
    return createUser(FAKER.name().username());
  }

  /**
   * Create a test user with flexibility.
   *
   * @param enabled if the user should be enabled or disabled
   * @return the user
   */
  public static User createUser(final boolean enabled) {
    return createUser(
        FAKER.name().username(),
        FAKER.internet().password(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH),
        FAKER.internet().emailAddress(),
        enabled);
  }

  /**
   * Create a user with some flexibility.
   *
   * @param username username used to create user.
   * @param roleType the role type
   * @return a user
   */
  public static User createUser(String username, RoleType roleType) {
    var user = createUser(username);
    user.getUserRoles().add(new UserRole(user, new Role(roleType)));
    return user;
  }

  /**
   * Create a user with some flexibility.
   *
   * @param username username used to create user.
   * @return a user
   */
  public static User createUser(String username) {
    return createUser(
        username,
        FAKER.internet().password(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH),
        FAKER.internet().emailAddress());
  }

  /**
   * Create a user with some flexibility.
   *
   * @param username username used to create user
   * @param password password used to create user.
   * @param email email used to create user.
   * @return a user
   */
  public static User createUser(String username, String password, String email) {
    return createUser(username, password, email, false);
  }

  /**
   * Create user with some flexibility.
   *
   * @param username username used to create user.
   * @param password password used to create user.
   * @param email email used to create user.
   * @param enabled boolean value used to evaluate if user enabled.
   * @return a user
   */
  public static User createUser(String username, String password, String email, boolean enabled) {
    var user = new User();
    user.setUsername(username);
    user.setPassword(password);
    user.setEmail(email);
    user.setPhone(FAKER.phoneNumber().cellPhone());

    var name = FAKER.name().nameWithMiddle();
    var fullName = name.split(" ");
    user.setFirstName(fullName[0]);
    user.setMiddleName(fullName[1]);
    user.setLastName(fullName[2]);

    if (enabled) {
      user.setEnabled(true);
      user.setAccountNonExpired(true);
      user.setAccountNonLocked(true);
      user.setCredentialsNonExpired(true);
    }
    return user;
  }

  /**
   * Create a test user with flexibility.
   *
   * @param username the username
   * @return the userDto
   */
  public static UserDto createUserDto(final String username) {
    return UserUtils.convertToUserDto(createUser(username));
  }

  /**
   * Create a test user with flexibility.
   *
   * @param enabled if the user should be enabled or disabled
   * @return the userDto
   */
  public static UserDto createUserDto(final boolean enabled) {
    return createUserDto(FAKER.name().username(), enabled);
  }

  /**
   * Create a test user with flexibility.
   *
   * @param username the username
   * @param enabled if the user should be enabled to authenticate
   * @return the userDto
   */
  public static UserDto createUserDto(final String username, boolean enabled) {
    var userDto = UserUtils.convertToUserDto(createUser(username));
    if (enabled) {
      enableUser(userDto);
    }
    return userDto;
  }

  /**
   * Create user with some flexibility.
   *
   * @param username username used to create user.
   * @param password password used to create user.
   * @param email email used to create user.
   * @param enabled boolean value used to evaluate if user enabled.
   * @return a userDto
   */
  public static UserDto createUserDto(
      String username, String password, String email, boolean enabled) {
    var user = createUser(username, password, email, enabled);

    return UserUtils.convertToUserDto(user);
  }

  /**
   * Transfers data from entity to transfer object.
   *
   * @param user stored user
   * @return user dto
   */
  public static UserDto convertToUserDto(final User user) {
    var userDto = UserDtoMapper.MAPPER.toUserDto(user);
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
    return userDto;
  }

  /**
   * Transfers data from signUpRequest object to transfer object.
   *
   * @param signUpRequest the signup request
   * @return user dto
   */
  public static UserDto convertToUserDto(final SignUpRequest signUpRequest) {
    var userDto = UserDtoMapper.MAPPER.toUserDto(signUpRequest);
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
    return userDto;
  }

  /**
   * Transfers data from entity to transfer object.
   *
   * @param users stored users
   * @return user dto
   */
  public static List<UserDto> convertToUserDto(final List<User> users) {
    var userDtoList = UserDtoMapper.MAPPER.toUserDto(users);
    Validate.notNull(userDtoList, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
    return userDtoList;
  }

  /**
   * Transfers data from userDetails to dto object.
   *
   * @param userDetailsBuilder stored user details
   * @return user dto
   */
  public static UserDto convertToUserDto(UserDetailsBuilder userDetailsBuilder) {
    var userDto = UserDtoMapper.MAPPER.toUserDto(userDetailsBuilder);
    Validate.notNull(userDetailsBuilder, "userDetailsBuilder cannot be null");
    return userDto;
  }

  /**
   * Transfers data from transfer object to entity.
   *
   * @param userDto the userDto
   * @return user
   */
  public static User convertToUser(final UserDto userDto) {
    var user = UserDtoMapper.MAPPER.toUser(userDto);
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
    return user;
  }

  /**
   * Transfers data from entity to returnable object.
   *
   * @param userHistories stored userHistories details
   * @return userHistories dto
   */
  public static List<UserHistoryDto> convertToUserHistoryDto(final Set<UserHistory> userHistories) {
    ValidationUtils.validateInputs(userHistories);

    return UserHistoryDtoMapper.MAPPER.toUserHistoryDto(userHistories);
  }

  /**
   * Enables and unlocks a user.
   *
   * @param userDto the userDto
   */
  public static void enableUser(final UserDto userDto) {
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
    userDto.setEnabled(true);
    userDto.setAccountNonExpired(true);
    userDto.setAccountNonLocked(true);
    userDto.setCredentialsNonExpired(true);
    userDto.setFailedLoginAttempts(0);
  }

  /**
   * Verifies input string is an email.
   *
   * @param email email.
   * @return true if pattern matches valid3 email, otherwise false.
   */
  public static boolean isEmail(String email) {
    return EmailValidator.getInstance().isValid(email);
  }

  /**
   * Retrieves the roles from the userRoles.
   *
   * @param userRoles the userRoles
   * @return set of the roles as strings
   */
  public static List<String> getRoles(Set<UserRole> userRoles) {
    List<String> roles = new ArrayList<>();

    for (UserRole userRole : userRoles) {
      if (Objects.nonNull(userRole.getRole())) {
        roles.add(userRole.getRole().getName());
      }
    }
    return roles;
  }

  /**
   * Returns the role with the highest precedence if user has multiple roles.
   *
   * @param user the user
   * @return the role
   */
  public static String getTopmostRole(User user) {
    ValidationUtils.validateInputs(user);

    if (Objects.isNull(user.getUserRoles())) {
      return null;
    }

    List<String> roles = getRoles(user.getUserRoles());

    if (roles.contains(RoleType.ROLE_ADMIN.getName())) {
      return RoleType.ROLE_ADMIN.getName();
    }

    return RoleType.ROLE_USER.getName();
  }

  /**
   * Returns the user profile or random image if not found.
   *
   * @param user the user
   * @return profile image
   */
  public static String getUserProfileImage(User user) {
    if (StringUtils.isBlank(user.getProfileImage())) {
      return ProfileConstants.PIC_SUM_PHOTOS_150_RANDOM;
    }

    return user.getProfileImage();
  }

  /**
   * Transfers data from entity to returnable object.
   *
   * @return user dto
   */
  public static Function<User, UserResponse> getUserResponse() {
    return UserDtoMapper.MAPPER::toUserResponse;
  }
}
