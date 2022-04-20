package com.developersboard.backend.service.user.impl;

import com.developersboard.backend.persistent.domain.user.User;
import com.developersboard.backend.persistent.domain.user.UserHistory;
import com.developersboard.backend.persistent.domain.user.UserRole;
import com.developersboard.backend.persistent.repository.UserRepository;
import com.developersboard.backend.service.impl.UserDetailsBuilder;
import com.developersboard.backend.service.user.RoleService;
import com.developersboard.backend.service.user.UserService;
import com.developersboard.constant.CacheConstants;
import com.developersboard.constant.UserConstants;
import com.developersboard.enums.RoleType;
import com.developersboard.enums.UserHistoryType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import com.developersboard.shared.util.core.ValidationUtils;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

  private final Clock clock;
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
    Validate.notNull(user, UserConstants.USER_MUST_NOT_BE_NULL);
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
    Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);

    var localUser = userRepository.findByEmail(userDto.getEmail());
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
    Validate.notNull(id, UserConstants.USER_ID_MUST_NOT_BE_NULL);

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
    Validate.notNull(publicId, UserConstants.BLANK_PUBLIC_ID);

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
    Validate.notNull(username, UserConstants.BLANK_USERNAME);

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
    Validate.notNull(email, UserConstants.BLANK_EMAIL);

    User storedUser = userRepository.findByEmail(email);
    if (Objects.isNull(storedUser)) {
      return null;
    }
    return UserUtils.convertToUserDto(storedUser);
  }

  /**
   * Find all users that failed to verify their email after a certain time.
   *
   * @return List of users that failed to verify their email.
   */
  @Override
  public List<UserDto> findAllNotEnabledAfterAllowedDays() {
    var date = LocalDateTime.now(clock).minusDays(UserConstants.DAYS_TO_ALLOW_ACCOUNT_ACTIVATION);
    List<User> expiredUsers = userRepository.findByEnabledFalseAndCreatedAtBefore(date);

    return UserUtils.convertToUserDto(expiredUsers);
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
    Validate.notNull(username, UserConstants.BLANK_USERNAME);

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
    Validate.notNull(username, UserConstants.BLANK_USERNAME);
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
    Validate.notNull(username, UserConstants.BLANK_USERNAME);
    Validate.notNull(email, UserConstants.BLANK_EMAIL);

    return userRepository.existsByUsernameAndEnabledTrueOrEmailAndEnabledTrueOrderById(
        username, email);
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
    Validate.notNull(publicId, UserConstants.BLANK_PUBLIC_ID);

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
    Validate.notNull(publicId, UserConstants.BLANK_PUBLIC_ID);

    User storedUser = userRepository.findByPublicId(publicId);
    if (Objects.nonNull(storedUser)) {
      storedUser.setEnabled(false);
      UserDto userDto = UserUtils.convertToUserDto(storedUser);

      return persistUser(userDto, Collections.emptySet(), UserHistoryType.ACCOUNT_DISABLED, true);
    }
    return null;
  }

  /**
   * Delete the user with the user id given.
   *
   * @param publicId The publicId associated to the user to delete
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  @Override
  @Transactional
  public void deleteUser(String publicId) {
    ValidationUtils.validateInputsWithMessage(UserConstants.BLANK_PUBLIC_ID, publicId);

    // Number of rows deleted is expected to be 1 since publicId is unique
    int numberOfRowsDeleted = userRepository.deleteByPublicId(publicId);
    LOG.debug("Deleted {} user(s) with publicId {}", numberOfRowsDeleted, publicId);
  }

  /**
   * Transfers user details to a user object then persist to database.
   *
   * @param userDto the userDto
   * @param roleTypes the roleTypes
   * @param historyType the user history type
   * @param isUpdate if the operation is an update
   * @return the userDto
   */
  private UserDto persistUser(
      UserDto userDto, Set<RoleType> roleTypes, UserHistoryType historyType, boolean isUpdate) {

    // If no role types are specified, then set the default role type
    var localRoleTypes = new HashSet<>(roleTypes);
    if (localRoleTypes.isEmpty()) {
      localRoleTypes.add(RoleType.ROLE_USER);
    }

    var user = UserUtils.convertToUser(userDto);
    for (RoleType roleType : localRoleTypes) {
      var storedRole = roleService.findByName(roleType.name());
      user.addUserRole(new UserRole(user, storedRole));
    }
    user.addUserHistory(new UserHistory(UUID.randomUUID().toString(), user, historyType));

    return saveOrUpdate(user, isUpdate);
  }
}
