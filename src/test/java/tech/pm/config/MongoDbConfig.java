package tech.pm.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.ReactiveMongoClientFactoryBean;
import org.testcontainers.containers.MongoDBContainer;

@TestConfiguration
public class MongoDbConfig {

  private static final String MONGODB_IMAGE = "mongo:4.4.1";
  private static final Integer MONGODB_MAPPED_PORT = 27017;

  @Bean(initMethod = "start", destroyMethod = "stop")
  public MongoDBContainer mongoDbContainer() {
    return new MongoDBContainer(MONGODB_IMAGE)
        .withExposedPorts(MONGODB_MAPPED_PORT);
  }

  @Bean
  public MongoClientFactoryBean mongoClient(MongoDBContainer mongoDbContainer) {
    MongoClientFactoryBean mongo = new MongoClientFactoryBean();
    mongo.setHost(mongoDbContainer.getHost());
    mongo.setPort(mongoDbContainer.getMappedPort(MONGODB_MAPPED_PORT));
    return mongo;
  }

  @Bean
  public ReactiveMongoClientFactoryBean mongoClientReactive(MongoDBContainer mongoDbContainer) {
    ReactiveMongoClientFactoryBean mongo = new ReactiveMongoClientFactoryBean();
    mongo.setHost(mongoDbContainer.getHost());
    mongo.setPort(mongoDbContainer.getMappedPort(MONGODB_MAPPED_PORT));
    return mongo;
  }

}
