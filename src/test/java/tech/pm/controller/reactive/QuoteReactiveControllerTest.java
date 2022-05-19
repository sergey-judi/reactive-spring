package tech.pm.controller.reactive;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tech.pm.controller.dto.QuoteDto;
import tech.pm.exception.model.ErrorCode;

class QuoteReactiveControllerTest extends AbstractBaseReactiveTest {

  private QuoteDto quote;

  @BeforeEach
  void setup() {
    quote = insertQuote();
  }

  @Test
  void getQuote_isOk() {
    webTestClient.get()
        .uri(QUOTES_ENDPOINT + "/{id}", quote.getId())
        .exchange().expectStatus().isOk()
        .expectBody()
        .jsonPath("id").isEqualTo(quote.getId())
        .jsonPath("book").isEqualTo(quote.getBook())
        .jsonPath("content").isEqualTo(quote.getContent());
  }

  @Test
  void getQuote_notExisting_returnsErrorResponse() {
    String notExistingId = getUniqueId();

    String expectedErrorCode = ErrorCode.ENTITY_NOT_FOUND;
    String expectedErrorMessage = String.format("Quote with id [%s] not found", notExistingId);

    webTestClient.get()
        .uri(QUOTES_ENDPOINT + "/{id}", notExistingId)
        .exchange().expectStatus().isNotFound()
        .expectBody()
        .jsonPath("code").isEqualTo(expectedErrorCode)
        .jsonPath("message").isEqualTo(expectedErrorMessage)
        .jsonPath("at").exists();
  }

  @Test
  void createQuote_isOk() {
    QuoteDto newQuote = prepareQuoteDto();

    webTestClient.post()
        .uri(QUOTES_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(serialize(newQuote))
        .exchange().expectStatus().isCreated()
        .expectBody()
        .jsonPath("id").isEqualTo(newQuote.getId())
        .jsonPath("book").isEqualTo(newQuote.getBook())
        .jsonPath("content").isEqualTo(newQuote.getContent());

    webTestClient.get()
        .uri(QUOTES_ENDPOINT + "/{id}", newQuote.getId())
        .exchange().expectStatus().isOk()
        .expectBody()
        .jsonPath("id").isEqualTo(newQuote.getId())
        .jsonPath("book").isEqualTo(newQuote.getBook())
        .jsonPath("content").isEqualTo(newQuote.getContent());
  }

  @Test
  void createQuote_withExistingId_returnsErrorResponse() {
    QuoteDto duplicatedIdQuote = new QuoteDto(quote.getId(), getUniqueId(), getUniqueId());

    String expectedErrorCode = ErrorCode.ENTITY_ALREADY_EXISTS;
    String expectedErrorMessage = String.format("Quote with id [%s] already exists", duplicatedIdQuote.getId());

    webTestClient.post()
        .uri(QUOTES_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(serialize(duplicatedIdQuote))
        .exchange().expectStatus().isBadRequest()
        .expectBody()
        .jsonPath("code").isEqualTo(expectedErrorCode)
        .jsonPath("message").isEqualTo(expectedErrorMessage)
        .jsonPath("at").exists();
  }

  @Test
  void createQuote_withNullId_returnsErrorResponse() {
    QuoteDto invalidQuote = new QuoteDto(null, getUniqueId(), getUniqueId());

    String expectedErrorCode = ErrorCode.INVALID_PARAMS;

    webTestClient.post()
        .uri(QUOTES_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(serialize(invalidQuote))
        .exchange().expectStatus().isBadRequest()
        .expectBody()
        .jsonPath("code").isEqualTo(expectedErrorCode)
        .jsonPath("message").exists()
        .jsonPath("at").exists();
  }

  @Test
  void createQuote_withNullBook_returnsErrorResponse() {
    QuoteDto invalidQuote = new QuoteDto(getUniqueId(), null, getUniqueId());

    String expectedErrorCode = ErrorCode.INVALID_PARAMS;

    webTestClient.post()
        .uri(QUOTES_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(serialize(invalidQuote))
        .exchange().expectStatus().isBadRequest()
        .expectBody()
        .jsonPath("code").isEqualTo(expectedErrorCode)
        .jsonPath("message").exists()
        .jsonPath("at").exists();
  }

  @Test
  void createQuote_withNullContent_returnsErrorResponse() {
    QuoteDto invalidQuote = new QuoteDto(getUniqueId(), getUniqueId(), null);

    String expectedErrorCode = ErrorCode.INVALID_PARAMS;

    webTestClient.post()
        .uri(QUOTES_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(serialize(invalidQuote))
        .exchange().expectStatus().isBadRequest()
        .expectBody()
        .jsonPath("code").isEqualTo(expectedErrorCode)
        .jsonPath("message").exists()
        .jsonPath("at").exists();
  }

  @Test
  void updateQuote_isOk() {
    QuoteDto updatedQuote = prepareQuoteDto();

    webTestClient.put()
        .uri(QUOTES_ENDPOINT + "/{id}", quote.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(serialize(updatedQuote))
        .exchange().expectStatus().isOk()
        .expectBody()
        .jsonPath("id").isEqualTo(quote.getId())
        .jsonPath("book").isEqualTo(updatedQuote.getBook())
        .jsonPath("content").isEqualTo(updatedQuote.getContent());

    webTestClient.get()
        .uri(QUOTES_ENDPOINT + "/{id}", quote.getId())
        .exchange().expectStatus().isOk()
        .expectBody()
        .jsonPath("id").isEqualTo(quote.getId())
        .jsonPath("book").isEqualTo(updatedQuote.getBook())
        .jsonPath("content").isEqualTo(updatedQuote.getContent());
  }

  @Test
  void updateQuote_notExisting_returnsErrorResponse() {
    String notExistingId = getUniqueId();

    String expectedErrorCode = ErrorCode.ENTITY_NOT_FOUND;
    String expectedErrorMessage = String.format("Quote with id [%s] not found", notExistingId);

    webTestClient.put()
        .uri(QUOTES_ENDPOINT + "/{id}", notExistingId)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(serialize(prepareQuoteDto()))
        .exchange().expectStatus().isNotFound()
        .expectBody()
        .jsonPath("code").isEqualTo(expectedErrorCode)
        .jsonPath("message").isEqualTo(expectedErrorMessage)
        .jsonPath("at").exists();
  }

  @Test
  void deleteQuote_isOk() {
    webTestClient.delete()
        .uri(QUOTES_ENDPOINT + "/{id}", quote.getId())
        .exchange().expectStatus().isOk();

    String expectedErrorCode = ErrorCode.ENTITY_NOT_FOUND;
    String expectedErrorMessage = String.format("Quote with id [%s] not found", quote.getId());

    webTestClient.get()
        .uri(QUOTES_ENDPOINT + "/{id}", quote.getId())
        .exchange().expectStatus().isNotFound()
        .expectBody()
        .jsonPath("code").isEqualTo(expectedErrorCode)
        .jsonPath("message").isEqualTo(expectedErrorMessage)
        .jsonPath("at").exists();
  }

  @Test
  void deleteQuote_notExisting_returnsErrorResponse() {
    String notExistingId = getUniqueId();

    String expectedErrorCode = ErrorCode.ENTITY_NOT_FOUND;
    String expectedErrorMessage = String.format("Quote with id [%s] not found", notExistingId);

    webTestClient.delete()
        .uri(QUOTES_ENDPOINT + "/{id}", notExistingId)
        .exchange().expectStatus().isNotFound()
        .expectBody()
        .jsonPath("code").isEqualTo(expectedErrorCode)
        .jsonPath("message").isEqualTo(expectedErrorMessage)
        .jsonPath("at").exists();
  }

}
