package tech.pm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import tech.pm.config.MongoDbConfig;
import tech.pm.config.QuoteDataLoader;
import tech.pm.controller.dto.QuoteDto;

import java.util.concurrent.atomic.AtomicLong;

@Import(MongoDbConfig.class)
public abstract class AbstractBaseTest {

  protected static final AtomicLong uniqueId = new AtomicLong();

  @Autowired
  protected ObjectMapper objectMapper;

  @MockBean
  protected QuoteDataLoader loader;

  protected abstract QuoteDto insertQuote();

  protected QuoteDto prepareQuoteDto() {
    String id = getUniqueId();
    String book = "book-" + id;
    String content = "content-" + id;

    return new QuoteDto(id, book, content);
  }

  @SneakyThrows
  protected String serialize(Object object) {
    return objectMapper.writeValueAsString(object);
  }

  protected String getUniqueId() {
    return String.valueOf(uniqueId.incrementAndGet());
  }

}
