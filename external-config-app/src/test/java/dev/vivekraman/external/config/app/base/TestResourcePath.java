package dev.vivekraman.external.config.app.base;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TestResourcePath {
  @Getter(AccessLevel.PRIVATE) private final String base;
  private final String beforeDB;
  private final String response;
  private final String afterDB;

  public TestResourcePath(String controllerName, String methodName, String testName) {
    this.base = "/api/%s/%s/%s".formatted(controllerName, methodName, testName);
    this.beforeDB = this.base + "/before/db";
    this.response = this.base + "/after/response.json";
    this.afterDB = this.base + "/after/db";
  }
}
