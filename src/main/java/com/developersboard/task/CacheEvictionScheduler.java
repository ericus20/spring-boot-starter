package com.developersboard.task;

import com.developersboard.annotation.Loggable;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Removes all entries in the cache.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheEvictionScheduler {

  public static final int EVICT_ALL_CACHE_INTERVAL = 3000;
  private final CacheManager cacheManager;

  /**
   * Ensure that the cache is cleared every 5 minutes.
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
   *
   * fixedRate = 300000 in milliseconds (5 minutes)
   */
  @Loggable(level = "trace")
  @Scheduled(fixedRate = EVICT_ALL_CACHE_INTERVAL)
  public void evictAllCaches() {
    cacheManager
        .getCacheNames()
        .forEach(
            cacheName -> {
              var cache = cacheManager.getCache(cacheName);
              if (Objects.nonNull(cache)) {
                cache.clear();
              }
            });
  }
}
