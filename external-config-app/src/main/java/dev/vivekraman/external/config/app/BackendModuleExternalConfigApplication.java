package dev.vivekraman.external.config.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "dev.vivekraman.*")
public class BackendModuleExternalConfigApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackendModuleExternalConfigApplication.class, args);
	}
}
