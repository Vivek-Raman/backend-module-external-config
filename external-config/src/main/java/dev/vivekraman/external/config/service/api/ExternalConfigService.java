package dev.vivekraman.external.config.service.api;

import dev.vivekraman.external.config.entity.ExternalConfig;
import reactor.core.publisher.Mono;

public interface ExternalConfigService {
  /**
   * Fetches ExternalConfig by key.
   * @param key to be queried
   * @return externalConfig with matching key
   */
  Mono<ExternalConfig> findByKey(String key);
}
