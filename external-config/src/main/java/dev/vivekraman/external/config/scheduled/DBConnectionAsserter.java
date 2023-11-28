package dev.vivekraman.external.config.scheduled;

import dev.vivekraman.external.config.entity.ExternalConfig;
import dev.vivekraman.external.config.repository.ExternalConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.r2dbc.BadSqlGrammarException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class DBConnectionAsserter implements InitializingBean, ApplicationContextAware {
  private static final int MAX_RETRIES = 20;
  private static final Duration RETRY_INTERVAL = Duration.ofSeconds(5);
  private static final String KEY = "db_initialized";

  private final ExternalConfigRepository externalConfigRepository;
  @Setter private ApplicationContext applicationContext;

  @Override
  public void afterPropertiesSet() throws Exception {
    assertDBConnection();
  }

  @Scheduled(fixedDelay = 6L, timeUnit = TimeUnit.HOURS)
  public void heartbeat() throws Exception {
    assertDBConnection();
  }

  private void assertDBConnection() throws Exception {
    if (applicationContext.getEnvironment().matchesProfiles("test")) {
      log.info("Skipping DB connection assertion due to active test profile");
      return;
    }

    Throwable exception = null;
    for (int i = 0; i < MAX_RETRIES; i++) {
      try {
        ExternalConfig doc = externalConfigRepository.findByConfigKey(KEY).block();
        log.info("DB connection established, Successfully fetched document {}", doc);
        return;
      } catch (BadSqlGrammarException e) {
        exception = e;
        log.warn("Failed {}/{} times to fetch document with key {}, retrying in {} seconds",
            i + 1, MAX_RETRIES, KEY, RETRY_INTERVAL.toSeconds());
        Thread.sleep(RETRY_INTERVAL.toMillis());
      } catch (Exception e) {
        exception = e;
        log.error("Encountered unhandled exception!");
        break;
      }
    }

    log.error("DB connected could not be asserted", exception);
  }
}
