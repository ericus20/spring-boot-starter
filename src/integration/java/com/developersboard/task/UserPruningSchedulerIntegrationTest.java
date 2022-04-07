package com.developersboard.task;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.backend.service.user.UserService;
import com.developersboard.shared.dto.UserDto;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;

class UserPruningSchedulerIntegrationTest extends IntegrationTestUtils {

  @Autowired private transient UserService userService;

  @Autowired private transient UserPruningScheduler userPruningScheduler;

  // We are mocking the entire dateTimeProvider since there is only one method in it.
  @MockBean private transient DateTimeProvider dateTimeProvider;

  // We want to mock just the dateTimeProvider method within the auditHandler
  @SpyBean private transient AuditingHandler auditingHandler;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    auditingHandler.setDateTimeProvider(dateTimeProvider);
  }

  @Test
  void findAllNotEnabledAfterAllowedDays(TestInfo testInfo) {
    // Create a fixed clock to 60 days back in time.
    var instantExpected = LocalDateTime.now().minusDays(60).toInstant(ZoneOffset.UTC).toString();
    var fixedClock = Clock.fixed(Instant.parse(instantExpected), ZoneId.systemDefault());

    // When dateTimeProvider is called, return fixedClock to simulate creating user in the past.
    Mockito.when(dateTimeProvider.getNow()).thenReturn(Optional.of(LocalDateTime.now(fixedClock)));

    UserDto userDto = createAndAssertUser(userService, testInfo.getDisplayName(), false);

    var users = userService.findAllNotEnabledAfterAllowedDays();
    Assertions.assertFalse(users.isEmpty());
    Assertions.assertTrue(users.contains(userDto));

    // Pruning should remove the user
    userPruningScheduler.pruneUsers();

    // Assert that the user is no longer in the database
    Assertions.assertFalse(userService.existsByUsername(userDto.getUsername()));
  }
}
