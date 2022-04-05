package com.developersboard.task;

import com.developersboard.annotation.Loggable;
import com.developersboard.backend.service.user.UserService;
import com.developersboard.shared.dto.UserDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Removes all users not enabled. That is users that do not verify their account after some time.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserPruningScheduler {

  private final UserService userService;

  /**
   * Every user that does not verify email after a certain time is deleted from the database.
   *
   * <pre>
   * Format is:
   * second minute hour day-of-month month day-of-week year command
   * # 1. Entry: Minute when the process will be started [0-60]
   * # 2. Entry: Hour when the process will be started [0-23]
   * # 3. Entry: Day of the month when the process will be started [1-28/29/30/31]
   * # 4. Entry: Month of the year when the process will be started [1-12]
   * # 5. Entry: Weekday when the process will be started [0-6] [0 is Sunday]
   * #
   * # all x min = *\/x.
   * '0 0 0 ? * SUN' for every Sunday.
   * '0 * * * * *' for every minute.
   * </pre>
   */
  @Loggable
  @Scheduled(cron = "0 0 0 ? * SUN")
  public void pruneUsers() {
    List<UserDto> usersToDelete = userService.findAllNotEnabledAfterAllowedDays();
    LOG.debug("Found {} user(s) to be removed", usersToDelete.size());

    // Delete all users that are not enabled after a certain time
    usersToDelete.parallelStream().map(UserDto::getPublicId).forEach(userService::deleteUser);
  }
}
