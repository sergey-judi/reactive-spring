package tech.pm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tech.pm.config.properties.ApplicationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class ReactiveSpringApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReactiveSpringApplication.class, args);
  }

}
