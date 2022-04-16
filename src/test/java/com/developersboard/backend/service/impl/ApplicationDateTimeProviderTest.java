package com.developersboard.backend.service.impl;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApplicationDateTimeProviderTest {

  @Mock private transient Clock clock;

  @Test
  void testGetNowWithFixedClock() {
    var fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    Mockito.doReturn(fixedClock.instant()).when(clock).instant();
    Mockito.doReturn(fixedClock.getZone()).when(clock).getZone();

    var dateTimeProvider = new ApplicationDateTimeProvider(clock);
    Optional<TemporalAccessor> optionalTemporalAccessor = dateTimeProvider.getNow();
    Assertions.assertAll(
        () -> {
          Assertions.assertTrue(optionalTemporalAccessor.isPresent());
          Assertions.assertEquals(LocalDateTime.now(fixedClock), optionalTemporalAccessor.get());
        });
  }
}
