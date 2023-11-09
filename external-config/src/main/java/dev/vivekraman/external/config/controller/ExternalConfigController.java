package dev.vivekraman.external.config.controller;

import dev.vivekraman.external.config.config.Constants;
import dev.vivekraman.external.config.entity.ExternalConfig;
import dev.vivekraman.external.config.service.api.ExternalConfigService;
import dev.vivekraman.monolith.annotation.MonolithController;
import dev.vivekraman.monolith.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@MonolithController(moduleName = Constants.MODULE_NAME)
@RequestMapping("/" + Constants.MODULE_NAME)
@RequiredArgsConstructor
public class ExternalConfigController {
  private final Scheduler scheduler;
  private final ExternalConfigService externalConfigService;

  @GetMapping("/{key}")
  public Mono<Response<ExternalConfig>> fetchExternalConfigByKey(@PathVariable String key) {
    return externalConfigService.findByKey(key)
        .map(Response::of)
        .subscribeOn(scheduler);
  }
}
