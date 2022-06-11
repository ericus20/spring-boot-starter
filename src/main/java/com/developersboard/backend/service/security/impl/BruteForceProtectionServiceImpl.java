package com.developersboard.backend.service.security.impl;

import com.developersboard.backend.persistent.repository.UserRepository;
import com.developersboard.backend.service.security.BruteForceProtectionService;
import com.developersboard.constant.CacheConstants;
import com.developersboard.constant.user.UserConstants;
import com.developersboard.shared.util.core.ValidationUtils;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is the implementation of the brute force protection service.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BruteForceProtectionServiceImpl implements BruteForceProtectionService {

  @Value("${security.failedLoginAttempts}")
  private int maxFailedLogins;

  @Value("${brute.force.cache.maxSize}")
  private int cacheMaxLimit;

  private final UserRepository userRepository;

  private final ConcurrentHashMap<String, FailedLogin> cache;

  @Autowired
  public BruteForceProtectionServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
    this.cache = new ConcurrentHashMap<>(cacheMaxLimit); // setting max limit for cache
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(value = CacheConstants.USERS, key = "#username"),
        @CacheEvict(value = CacheConstants.USER_DETAILS, allEntries = true)
      })
  @Transactional
  public void registerLoginFailure(final String username) {
    ValidationUtils.validateInputs(username, UserConstants.BLANK_USERNAME);

    var user = userRepository.findByUsername(username);

    if (Objects.nonNull(user) && user.isAccountNonLocked()) {
      var failedAttempts = user.getFailedLoginAttempts();
      if (maxFailedLogins < failedAttempts + 1) {
        LOG.info("User {} is locked due to {} failed login attempts", username, failedAttempts);
        // disable user account
        user.setAccountNonLocked(false);
      } else {
        // increment failed login attempts
        user.setFailedLoginAttempts(failedAttempts + 1);
        LOG.debug("User {} has {} failed login attempts", username, failedAttempts + 1);
      }
    } else {
      LOG.warn("User {} is not found or is already locked", username);
    }
  }

  @Override
  @Caching(
      evict = {
        @CacheEvict(value = CacheConstants.USERS, key = "#username"),
        @CacheEvict(value = CacheConstants.USER_DETAILS, allEntries = true)
      })
  @Transactional
  public void resetBruteForceCounter(final String username) {
    ValidationUtils.validateInputs(username, UserConstants.BLANK_USERNAME);

    var user = userRepository.findByUsername(username);

    if (Objects.nonNull(user)) {
      user.setFailedLoginAttempts(0);
      user.setLastSuccessfulLogin(LocalDateTime.now());
      // if the user is not locked, then there is no need to update the accountNonLocked field
      if (!user.isAccountNonLocked()) {
        user.setAccountNonLocked(true);
      }
    } else {
      LOG.warn("User {} is not found", username);
    }
  }

  @Override
  public boolean isBruteForceAttack(final String username) {
    ValidationUtils.validateInputs(username, UserConstants.BLANK_USERNAME);

    if (userRepository.existsByUsernameAndFailedLoginAttemptsGreaterThanOrderById(
        username, maxFailedLogins)) {

      LOG.debug(
          "Possible bruteforce attack on username {} and maxFailedLogins {}",
          username,
          maxFailedLogins);
      return true;
    }
    return false;
  }

  protected FailedLogin getFailedLogin(final String username) {
    var failedLogin = cache.get(username.toLowerCase(Locale.getDefault()));

    if (Objects.isNull(failedLogin)) {
      // setup the initial data
      failedLogin = new FailedLogin(0, LocalDateTime.now());
      cache.put(username.toLowerCase(Locale.getDefault()), failedLogin);
      if (cache.size() > cacheMaxLimit) {
        // remove the oldest entry based on localDateTime
        cache.entrySet().stream()
            .min(Map.Entry.comparingByValue(Comparator.comparing(FailedLogin::getDate)))
            .ifPresent(entry -> cache.remove(entry.getKey()));
      }
    }
    return failedLogin;
  }

  @Data
  public static class FailedLogin {

    private int count;
    private LocalDateTime date;

    public FailedLogin() {
      this.count = 0;
      this.date = LocalDateTime.now();
    }

    public FailedLogin(int count, LocalDateTime date) {
      this.count = count;
      this.date = date;
    }
  }
}
