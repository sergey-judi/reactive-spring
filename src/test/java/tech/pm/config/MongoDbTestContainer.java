package tech.pm.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class MongoDbTestContainer {

  private static final String MONGODB_IMAGE = "mongo:4.4.1";
  private static final Integer MONGODB_MAPPED_PORT = 27017;

  @Container
  static MongoDBContainer mongoDBContainer = new MongoDBContainer(MONGODB_IMAGE);

  @DynamicPropertySource
  static void mongoDbProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.port", () -> mongoDBContainer.getMappedPort(MONGODB_MAPPED_PORT));
    registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
  }

}
