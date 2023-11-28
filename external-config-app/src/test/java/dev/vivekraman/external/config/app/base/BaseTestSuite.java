package dev.vivekraman.external.config.app.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient(timeout = "5s")
@TestPropertySource(locations = "classpath:/application-test.properties")
public abstract class BaseTestSuite {
  @Autowired
  protected WebTestClient webTestClient;

  @Autowired
  protected ObjectMapper objectMapper;

  @BeforeEach
  public void beforeEach(TestInfo testInfo) {
    List<ReactiveCrudRepository<?, ?>> repositories = repositories();
    repositories.forEach(repo -> repo.deleteAll().block());
  }

  /**
   * Override in every test class that involves repositories
   */
  protected List<ReactiveCrudRepository<?, ?>> repositories() {
    return Collections.emptyList();
  }

  protected <T> T parseJsonFileResource(String resourcePath, TypeReference<T> typeReference)
      throws Exception {
    try (InputStream inputStream = this.getClass().getResourceAsStream(resourcePath)) {
      return this.objectMapper.readValue(inputStream, typeReference);
    }
  }

  protected WebTestClient.RequestBodySpec generateRequest(HttpMethod method, URI uri) {
    return this.webTestClient.method(method).uri(uri)
        .contentType(MediaType.APPLICATION_JSON);
  }
}
