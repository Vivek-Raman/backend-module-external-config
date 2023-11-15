package dev.vivekraman.external.config.repository;

import dev.vivekraman.external.config.entity.ExternalConfig;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ExternalConfigRepository extends ReactiveCrudRepository<ExternalConfig, String> {
  Mono<ExternalConfig> findByConfigKey(String key);
}
