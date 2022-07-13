package com.developersboard.backend.service.user;

import com.developersboard.backend.persistent.domain.user.User;
import com.developersboard.enums.RoleType;
import com.developersboard.enums.UserHistoryType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.web.payload.response.UserResponse;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * This UserService interface is the contract for the user service operations.
 *
 * @author Matthew Puentes
 * @version 1.0
 * @since 1.0
 */
public interface UserService {

  /**
   * Saves or updates the user with the user instance given.
   *
   * @param user the user with updated information
   * @param isUpdate if the operation is an update
   * @return the updated user.
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  UserDto saveOrUpdate(User user, boolean isUpdate);

  /**
   * Create the userDto with the userDto instance given.
   *
   * @param userDto the userDto with updated information
   * @return the updated userDto.
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  UserDto createUser(final UserDto userDto);

  /**
   * Create the userDto with the userDto instance given.
   *
   * @param userDto the userDto with updated information
   * @param roleTypes the roleTypes.
   * @return the updated userDto.
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  UserDto createUser(final UserDto userDto, final Set<RoleType> roleTypes);

  /**
   * Returns users according to the details in the dataTablesInput or null if no user exists.
   *
   * @param dataTablesInput the dataTablesInput
   * @return the dataTablesOutput
   */
  DataTablesOutput<UserResponse> getUsers(final DataTablesInput dataTablesInput);

  /**
   * Returns a user for the given id or null if a user could not be found.
   *
   * @param id The id associated to the user to find
   * @return a user for the given email or null if a user could not be found.
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  UserDto findById(Long id);

  /**
   * Returns a user for the given publicId or null if a user could not be found.
   *
   * @param publicId the publicId
   * @return the userDto
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  UserDto findByPublicId(String publicId);

  /**
   * Returns a user for the given username or null if a user could not be found.
   *
   * @param username The username associated to the user to find
   * @return a user for the given username or null if a user could not be found
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  UserDto findByUsername(String username);

  /**
   * Returns a user for the given email or null if a user could not be found.
   *
   * @param email The email associated to the user to find
   * @return a user for the given email or null if a user could not be found
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  UserDto findByEmail(String email);

  /**
   * Find all users that failed to verify their email after a certain time.
   *
   * @return List of users that failed to verify their email.
   */
  List<UserDto> findAllNotEnabledAfterAllowedDays();

  /**
   * Returns a userDetails for the given username or null if a user could not be found.
   *
   * @param username The username associated to the user to find
   * @return a user for the given username or null if a user could not be found
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  UserDetails getUserDetails(String username);

  /**
   * Checks if the username already exists.
   *
   * @param username the username
   * @return <code>true</code> if username exists
   */
  boolean existsByUsername(String username);

  /**
   * Checks if the username or email already exists and enabled.
   *
   * @param username the username
   * @param email the email
   * @return <code>true</code> if username exists
   */
  boolean existsByUsernameOrEmailAndEnabled(String username, String email);

  /**
   * Validates the username exists and the token belongs to the user with the username.
   *
   * @param username the username
   * @param token the token
   * @return if token is valid
   */
  boolean isValidUsernameAndToken(String username, String token);

  /**
   * Update the user with the user instance given and the update type for record.
   *
   * @param userDto The user with updated information
   * @param userHistoryType the history type to be recorded
   * @return the updated user
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  UserDto updateUser(UserDto userDto, UserHistoryType userHistoryType);

  /**
   * Enables the user by setting the enabled state to true.
   *
   * @param publicId The user publicId
   * @return the updated user
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  UserDto enableUser(String publicId);

  /**
   * Disables the user by setting the enabled state to false.
   *
   * @param publicId The user publicId
   * @return the updated user
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  UserDto disableUser(String publicId);

  /**
   * Delete the user with the user id given.
   *
   * @param publicId The publicId associated to the user to delete
   * @throws NullPointerException in case the given entity is {@literal null}
   */
  void deleteUser(String publicId);
}
