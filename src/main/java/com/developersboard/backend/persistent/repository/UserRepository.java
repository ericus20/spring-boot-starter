package com.developersboard.backend.persistent.repository;

import com.developersboard.backend.persistent.domain.user.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

/**
 * Repository for the User.
 *
 * @author Matthew Puentes
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @RestResource(exported = false)
  @NonNull
  Optional<User> findById(Long id);

  /**
   * Find user by email.
   *
   * @param email email used to search for user.
   * @return User found.
   */
  User findByEmail(String email);

  /**
   * Check if user exists by email.
   *
   * @param email email to check if user exists.
   * @return True if user exists or false otherwise.
   */
  Boolean existsByEmailOrderById(String email);

  /**
   * Find user by username.
   *
   * @param username username used to search for user.
   * @return User found.
   */
  User findByUsername(String username);

  /**
   * Check if user exists by username.
   *
   * @param username username to check if user exists.
   * @return True if user exists or false otherwise.
   */
  Boolean existsByUsernameOrderById(String username);

  /**
   * Check if user exists by username or email.
   *
   * @param username username to check if user exists.
   * @param email email to check if user exists.
   * @return True if user exists or false otherwise.
   */
  @RestResource(exported = false)
  Boolean existsByUsernameAndEnabledTrueOrEmailAndEnabledTrueOrderById(
      String username, String email);

  /**
   * Find user by public id.
   *
   * @param publicId publicId used to search for user.
   * @return User found.
   */
  User findByPublicId(String publicId);

  /**
   * Find all users that failed to verify their email after a certain time.
   *
   * @param allowedDaysToVerify email verification allowed days.
   * @return List of users that failed to verify their email.
   */
  @RestResource(exported = false)
  List<User> findByEnabledFalseAndCreatedAtBefore(LocalDateTime allowedDaysToVerify);

  /**
   * Delete the user associated with the given public id.
   *
   * @param publicId public id of the user to delete.
   * @return Number of rows deleted.
   */
  @RestResource(exported = false)
  int deleteByPublicId(String publicId);
}
