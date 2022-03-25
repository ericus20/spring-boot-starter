package com.developersboard.backend.persistent.repository;

import com.developersboard.backend.persistent.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
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
  Boolean existsByUsernameAndEnabledTrueOrEmailAndEnabledTrueOrderById(
      String username, String email);

  /**
   * Find user by public id.
   *
   * @param publicId publicId used to search for user.
   * @return User found.
   */
  User findByPublicId(String publicId);
}
