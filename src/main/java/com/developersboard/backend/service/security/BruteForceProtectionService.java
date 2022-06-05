package com.developersboard.backend.service.security;

/**
 * This is the contract for the brute force protection service operations. Responsible for handling
 * the login attempts.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public interface BruteForceProtectionService {

  /**
   * Method to register every login failure attempt for a given username. We will be using a simple
   * counter to keep track of the failed attempts.
   *
   * @param username the username
   */
  void registerLoginFailure(final String username);

  /**
   * Method to reset the counter for successful login. We want to make sure that we are setting the
   * counter as 0 on successful login to avoid customer lockout.
   *
   * @param username the username
   */
  void resetBruteForceCounter(final String username);

  /**
   * check if the user account is under brute force attack, this will check the failed count with
   * threshold value and will return true if the failed count exceeds the threshold value.
   *
   * @param username the username
   * @return if the user account is under brute force attack
   */
  boolean isBruteForceAttack(final String username);
}
