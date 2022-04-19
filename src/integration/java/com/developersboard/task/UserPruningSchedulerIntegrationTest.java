package com.developersboard.task;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserPruningSchedulerIntegrationTest extends IntegrationTestUtils {

  @BeforeEach
  void setUp() {
    auditingHandler.setDateTimeProvider(dateTimeProvider);
  }

  @Test
  void findAllNotEnabledAfterAllowedDays() {
    // Create a fixed clock to 60 days back in time.
    var instantExpected = LocalDateTime.now().minusDays(60).toInstant(ZoneOffset.UTC).toString();
    var fixedClock = Clock.fixed(Instant.parse(instantExpected), ZoneId.systemDefault());

    // When dateTimeProvider is called, return fixedClock to simulate creating user in the past.
    Mockito.when(dateTimeProvider.getNow()).thenReturn(Optional.of(LocalDateTime.now(fixedClock)));

    UserDto userDto = createAndAssertUser(UserUtils.createUserDto(false));

    var users = userService.findAllNotEnabledAfterAllowedDays();
    Assertions.assertFalse(users.isEmpty());
    Assertions.assertTrue(users.contains(userDto));

    // Pruning should remove the user
    userPruningScheduler.pruneUsers();

    // Assert that the user is no longer in the database
    Assertions.assertFalse(userService.existsByUsername(userDto.getUsername()));
  }
}
