package dev.vivekraman.external.config.service.impl;

import dev.vivekraman.external.config.entity.ExternalConfig;
import dev.vivekraman.external.config.repository.ExternalConfigRepository;
import dev.vivekraman.external.config.service.api.ExternalConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExternalConfigServiceImpl implements ExternalConfigService {
  private final ExternalConfigRepository externalConfigRepository;

  @Override
  public Mono<ExternalConfig> findByKey(String key) {
    if (StringUtils.isBlank(key)) {
      return Mono.empty();
    }

    return externalConfigRepository.findByKey(key);
  }
}
