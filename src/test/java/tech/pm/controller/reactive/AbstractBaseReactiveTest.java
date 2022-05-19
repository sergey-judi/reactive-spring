package tech.pm.controller.reactive;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import tech.pm.controller.AbstractBaseTest;
import tech.pm.controller.dto.QuoteDto;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "spring.main.web-application-type=reactive")
public abstract class AbstractBaseReactiveTest extends AbstractBaseTest {

  protected static final String QUOTES_ENDPOINT = "/reactive/quotes";

  @Autowired
  protected WebTestClient webTestClient;

  @Override
  @SneakyThrows
  protected QuoteDto insertQuote() {
    byte[] rawResponseBody = webTestClient.post()
        .uri(QUOTES_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(serialize(prepareQuoteDto()))
        .exchange()
        .expectStatus().isCreated()
        .returnResult(QuoteDto.class)
        .getResponseBodyContent();

    String responseBody = new String(rawResponseBody);
    return objectMapper.readValue(responseBody, QuoteDto.class);
  }

}
