package dev.vivekraman.external.config.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories(basePackages = "dev.vivekraman.external.config.repository")
public class ExternalConfigConfig {
  @Bean
  public GroupedOpenApi externalConfigApiGroup() {
    return GroupedOpenApi.builder()
        .group(Constants.MODULE_NAME)
        .packagesToScan("dev.vivekraman.external.config.controller")
        .build();
  }
}
