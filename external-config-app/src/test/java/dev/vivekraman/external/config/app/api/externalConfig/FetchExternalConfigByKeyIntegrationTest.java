package dev.vivekraman.external.config.app.api.externalConfig;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.vivekraman.external.config.app.base.BaseTestSuite;
import dev.vivekraman.external.config.app.base.TestResourcePath;
import dev.vivekraman.external.config.entity.ExternalConfig;
import dev.vivekraman.external.config.repository.ExternalConfigRepository;
import dev.vivekraman.monolith.model.Response;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.util.Collections;
import java.util.List;

class FetchExternalConfigByKeyIntegrationTest extends BaseTestSuite {

  @Autowired
  private ExternalConfigRepository externalConfigRepository;

  private URI buildURI(String configKey) {
    return URI.create("/external-config/" + configKey);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "externalConfig,fetchExternalConfigByKey,success,test",
      "externalConfig,fetchExternalConfigByKey,notFound,nope",
  })
  void fetchExternalConfigByKey(String controllerName, String methodName, String testName, String configKey)
      throws Exception {
    testName = methodName + "_" + testName;
    TestResourcePath testResourcePath = new TestResourcePath(controllerName, methodName, testName);

    List<ExternalConfig> beforeDB = this.parseJsonFileResource(
        testResourcePath.getBeforeDB() + "/external-config.json", DB_CLASS_TYPE);
    if (!beforeDB.isEmpty()) {
      this.externalConfigRepository.saveAll(beforeDB).collectList().block();
    }

    Response<ExternalConfig> expectedResponse = this.parseJsonFileResource(
        testResourcePath.getResponse(), new TypeReference<Response<ExternalConfig>>() {});
    this.generateRequest(HttpMethod.GET, buildURI(configKey)).exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody(new ParameterizedTypeReference<Response<ExternalConfig>>() {})
        .value(actualResponse -> {
          Assertions.assertThat(actualResponse)
              .usingRecursiveComparison(RecursiveComparisonConfiguration.builder()
                  .withIgnoredFields("data.id").build())
              .isEqualTo(expectedResponse);
        });

    List<ExternalConfig> afterDB = this.parseJsonFileResource(
        testResourcePath.getAfterDB() + "/external-config.json", DB_CLASS_TYPE);
    Assertions.assertThat(this.externalConfigRepository.count().block()).isEqualTo(afterDB.size());
    for (ExternalConfig expectedConfig : afterDB) {
      ExternalConfig actualConfig =
          this.externalConfigRepository.findByConfigKey(expectedConfig.getConfigKey()).block();
      Assertions.assertThat(actualConfig).isNotNull();
      Assertions.assertThat(actualConfig)
          .usingRecursiveComparison(RecursiveComparisonConfiguration.builder()
              .withIgnoredFields("id").build())
          .isEqualTo(expectedConfig);
    }
  }

  private static final TypeReference<List<ExternalConfig>> DB_CLASS_TYPE =
      new TypeReference<List<ExternalConfig>>() {};

  @Override
  protected List<ReactiveCrudRepository<?, ?>> repositories() {
    return Collections.singletonList(externalConfigRepository);
  }
}
