package com.developersboard.backend.service.user.impl;

import com.developersboard.backend.persistent.domain.user.User;
import com.developersboard.backend.persistent.domain.user.UserHistory;
import com.developersboard.backend.persistent.domain.user.UserRole;
import com.developersboard.backend.persistent.repository.UserRepository;
import com.developersboard.backend.service.impl.UserDetailsBuilder;
import com.developersboard.backend.service.user.RoleService;
import com.developersboard.backend.service.user.UserService;
import com.developersboard.constant.CacheConstants;
import com.developersboard.constant.user.UserConstants;
import com.developersboard.enums.RoleType;
import com.developersboard.enums.UserHistoryType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import com.developersboard.shared.util.core.ValidationUtils;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The UserServiceImpl class provides implementation for the UserService definitions.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

  private final RoleService roleService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Saves or updates the user with the user instance given.
   *
   * @param user the user with updated information
   * @param isUpdate if the operation is an update
   * @return the updated user.
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  @Override
  @Transactional
  public UserDto saveOrUpdate(User user, boolean isUpdate) {
    ValidationUtils.validateInputsWithMessage(UserConstants.USER_MUST_NOT_BE_NULL, user);

    User persistedUser = isUpdate ? userRepository.saveAndFlush(user) : userRepository.save(user);
    LOG.debug(UserConstants.USER_PERSISTED_SUCCESSFULLY, persistedUser);

    return UserUtils.convertToUserDto(persistedUser);
  }

  /**
   * Create the userDto with the userDto instance given.
   *
   * @param userDto the userDto with updated information
   * @return the updated userDto.
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  @Override
  @Transactional
  public UserDto createUser(UserDto userDto) {
    return createUser(userDto, Collections.singleton(RoleType.ROLE_USER));
  }

  /**
   * Create the userDto with the userDto instance given.
   *
   * @param userDto the userDto with updated information
   * @param roleTypes the roleTypes.
   * @return the updated userDto.
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  @Override
  @Transactional
  public UserDto createUser(UserDto userDto, Set<RoleType> roleTypes) {
    ValidationUtils.validateInputsWithMessage(UserConstants.USER_DTO_MUST_NOT_BE_NULL, userDto);

    User localUser = userRepository.findByEmail(userDto.getEmail());
    if (Objects.nonNull(localUser)) {
      if (!localUser.isEnabled()) {
        LOG.debug(UserConstants.USER_EXIST_BUT_NOT_ENABLED, userDto.getEmail(), localUser);
        return UserUtils.convertToUserDto(localUser);
      }
      LOG.warn(UserConstants.USER_ALREADY_EXIST, userDto.getEmail());
    } else {
      // Assign a public id to the user. This is used to identify the user in the system and can be
      // shared publicly over the internet.
      userDto.setPublicId(UUID.randomUUID().toString());

      // Update the user password with an encrypted copy of the password
      userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

      return persistUser(userDto, roleTypes, UserHistoryType.CREATED, false);
    }
    return null;
  }

  /**
   * Returns a user for the given id or null if a user could not be found.
   *
   * @param id The id associated to the user to find
   * @return a user for the given email or null if a user could not be found.
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  @Override
  public UserDto findById(Long id) {
    ValidationUtils.validateInputsWithMessage(UserConstants.USER_ID_MUST_NOT_BE_NULL, id);

    User storedUser = userRepository.findById(id).orElse(null);
    if (Objects.isNull(storedUser)) {
      return null;
    }
    return UserUtils.convertToUserDto(storedUser);
  }

  /**
   * Returns a user for the given publicId or null if a user could not be found.
   *
   * @param publicId the publicId
   * @return the userDto
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  @Override
  @Cacheable(CacheConstants.USERS)
  public UserDto findByPublicId(String publicId) {
    Validate.notBlank(publicId, UserConstants.BLANK_PUBLIC_ID);

    User storedUser = userRepository.findByPublicId(publicId);
    if (Objects.isNull(storedUser)) {
      return null;
    }
    return UserUtils.convertToUserDto(storedUser);
  }

  /**
   * Returns a user for the given username or null if a user could not be found.
   *
   * @param username The username associated to the user to find
   * @return a user for the given username or null if a user could not be found
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  @Override
  @Cacheable(CacheConstants.USERS)
  public UserDto findByUsername(String username) {
    Validate.notBlank(username, UserConstants.BLANK_USERNAME);

    User storedUser = userRepository.findByUsername(username);
    if (Objects.isNull(storedUser)) {
      return null;
    }
    return UserUtils.convertToUserDto(storedUser);
  }

  /**
   * Returns a user for the given email or null if a user could not be found.
   *
   * @param email The email associated to the user to find
   * @return a user for the given email or null if a user could not be found
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  @Override
  public UserDto findByEmail(String email) {
    Validate.notBlank(email, UserConstants.BLANK_EMAIL);

    User storedUser = userRepository.findByEmail(email);
    if (Objects.isNull(storedUser)) {
      return null;
    }
    return UserUtils.convertToUserDto(storedUser);
  }

  /**
   * Returns a userDetails for the given username or null if a user could not be found.
   *
   * @param username The username associated to the user to find
   * @return a user for the given username or null if a user could not be found
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  @Override
  public UserDetails getUserDetails(String username) {
    Validate.notBlank(username, UserConstants.BLANK_USERNAME);

    User storedUser = userRepository.findByUsername(username);
    return UserDetailsBuilder.buildUserDetails(storedUser);
  }

  /**
   * Checks if the username already exists.
   *
   * @param username the username
   * @return <code>true</code> if username exists
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  @Override
  public boolean existsByUsername(String username) {
    Validate.notBlank(username, UserConstants.BLANK_USERNAME);

    return userRepository.existsByUsernameOrderById(username);
  }

  /**
   * Checks if the username or email already exists and enabled.
   *
   * @param username the username
   * @param email the email
   * @return <code>true</code> if username exists
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  @Override
  public boolean existsByUsernameOrEmailAndEnabled(String username, String email) {
    ValidationUtils.validateInputs(username, email);

    return userRepository.existsByUsernameAndEnabledTrueOrEmailAndEnabledTrueOrderById(
        username, email);
  }

  /**
   * Validates the publicId exists and the verification token belongs to the user with the publicId.
   *
   * @param publicId the publicId
   * @param verification the verification
   * @return if verification is valid
   */
  @Override
  public boolean isVerificationTokenValid(String publicId, String verification) {
    ValidationUtils.validateInputs(publicId, verification);

    return userRepository.existsByPublicIdAndVerificationTokenOrderById(publicId, verification);
  }

  /**
   * Update the user with the user instance given and the update type for record.
   *
   * @param userDto The user with updated information
   * @param userHistoryType the history type to be recorded
   * @return the updated user
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  @Override
  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = CacheConstants.USERS, key = "#userDto.username"),
        @CacheEvict(value = CacheConstants.USERS, key = "#userDto.publicId")
      })
  public UserDto updateUser(UserDto userDto, UserHistoryType userHistoryType) {
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);

    userDto.setVerificationToken(null);
    return persistUser(userDto, Collections.emptySet(), userHistoryType, true);
  }

  /**
   * Assigns and saves the reset or verification token with the user object.
   *
   * @param userDto the userDto
   * @param verificationToken the token
   */
  @Override
  @Transactional
  public void saveVerificationToken(UserDto userDto, String verificationToken) {
    ValidationUtils.validateInputs(userDto, verificationToken);

    User userFromDto = UserUtils.convertToUser(userDto);
    userFromDto.setVerificationToken(verificationToken);

    User updatedUser = userRepository.saveAndFlush(userFromDto);
    LOG.debug(UserConstants.USER_UPDATE_SUCCESS, updatedUser);
  }

  /**
   * Enables the user by setting the enabled state to true.
   *
   * @param publicId The user publicId
   * @return the updated user
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  @Override
  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = CacheConstants.USERS),
        @CacheEvict(value = CacheConstants.USER_DETAILS, allEntries = true)
      })
  public UserDto enableUser(String publicId) {
    Validate.notBlank(publicId, UserConstants.BLANK_PUBLIC_ID);

    User storedUser = userRepository.findByPublicId(publicId);
    LOG.debug("Enabling user {}", storedUser);

    if (Objects.nonNull(storedUser)) {
      storedUser.setEnabled(true);
      UserDto userDto = UserUtils.convertToUserDto(storedUser);

      return persistUser(userDto, Collections.emptySet(), UserHistoryType.ACCOUNT_ENABLED, true);
    }
    return null;
  }

  /**
   * Disables the user by setting the enabled state to false.
   *
   * @param publicId The user publicId
   * @return the updated user
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  @Override
  @Transactional
  @Caching(
      evict = {
        @CacheEvict(value = CacheConstants.USERS),
        @CacheEvict(value = CacheConstants.USER_DETAILS, allEntries = true)
      })
  public UserDto disableUser(String publicId) {
    Validate.notBlank(publicId, UserConstants.BLANK_PUBLIC_ID);

    User storedUser = userRepository.findByPublicId(publicId);
    if (Objects.nonNull(storedUser)) {
      storedUser.setEnabled(false);
      UserDto userDto = UserUtils.convertToUserDto(storedUser);

      return persistUser(userDto, Collections.emptySet(), UserHistoryType.ACCOUNT_DISABLED, true);
    }
    return null;
  }

  /**
   * Transfers user details to a user object then persist to database.
   *
   * @param userDto the userDto
   * @param roles the roles
   * @param historyType the user history type
   * @param isUpdate if the operation is an update
   * @return the userDto
   */
  private UserDto persistUser(
      UserDto userDto, Set<RoleType> roles, UserHistoryType historyType, boolean isUpdate) {

    var user = UserUtils.convertToUser(userDto);
    for (RoleType roleType : roles) {
      var role = roleService.getRoleByName(roleType.getRole());
      if (Objects.nonNull(role)) {
        user.addUserRole(new UserRole(user, role));
      }
    }
    user.addUserHistory(new UserHistory(UUID.randomUUID().toString(), user, historyType));

    return saveOrUpdate(user, isUpdate);
  }
}
