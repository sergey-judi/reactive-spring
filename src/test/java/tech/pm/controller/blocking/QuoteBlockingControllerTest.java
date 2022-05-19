package tech.pm.controller.blocking;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tech.pm.controller.dto.QuoteDto;
import tech.pm.exception.model.ErrorCode;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class QuoteBlockingControllerTest extends AbstractBaseBlockingTest {

  private QuoteDto quote;

  @BeforeEach
  void setup() {
    quote = insertQuote();
  }

  @Test
  @SneakyThrows
  void getQuote_isOk() {
    mockMvc.perform(get(QUOTES_ENDPOINT + "/{id}", quote.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value(quote.getId()))
        .andExpect(jsonPath("book").value(quote.getBook()))
        .andExpect(jsonPath("content").value(quote.getContent()));
  }

  @Test
  @SneakyThrows
  void getQuote_notExisting_returnsErrorResponse() {
    String notExistingId = getUniqueId();

    String expectedErrorCode = ErrorCode.ENTITY_NOT_FOUND;
    String expectedErrorMessage = String.format("Quote with id [%s] not found", notExistingId);

    mockMvc.perform(get(QUOTES_ENDPOINT + "/{id}", notExistingId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("code").value(expectedErrorCode))
        .andExpect(jsonPath("message").value(expectedErrorMessage))
        .andExpect(jsonPath("at").exists());
  }

  @Test
  @SneakyThrows
  void createQuote_isOk() {
    QuoteDto newQuote = prepareQuoteDto();

    mockMvc.perform(post(QUOTES_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serialize(newQuote)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id").value(newQuote.getId()))
        .andExpect(jsonPath("book").value(newQuote.getBook()))
        .andExpect(jsonPath("content").value(newQuote.getContent()));

    mockMvc.perform(get(QUOTES_ENDPOINT + "/{id}", newQuote.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value(newQuote.getId()))
        .andExpect(jsonPath("book").value(newQuote.getBook()))
        .andExpect(jsonPath("content").value(newQuote.getContent()));
  }

  @Test
  @SneakyThrows
  void createQuote_withExistingId_returnsErrorResponse() {
    QuoteDto duplicatedIdQuote = new QuoteDto(quote.getId(), getUniqueId(), getUniqueId());

    String expectedErrorCode = ErrorCode.ENTITY_ALREADY_EXISTS;
    String expectedErrorMessage = String.format("Quote with id [%s] already exists", duplicatedIdQuote.getId());

    mockMvc.perform(post(QUOTES_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serialize(duplicatedIdQuote)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value(expectedErrorCode))
        .andExpect(jsonPath("message").value(expectedErrorMessage))
        .andExpect(jsonPath("at").exists());
  }

  @Test
  @SneakyThrows
  void createQuote_withNullId_returnsErrorResponse() {
    QuoteDto invalidQuote = new QuoteDto(null, getUniqueId(), getUniqueId());

    String expectedErrorCode = ErrorCode.INVALID_PARAMS;

    mockMvc.perform(post(QUOTES_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serialize(invalidQuote)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value(expectedErrorCode))
        .andExpect(jsonPath("message").exists())
        .andExpect(jsonPath("at").exists());
  }

  @Test
  @SneakyThrows
  void createQuote_withNullBook_returnsErrorResponse() {
    QuoteDto invalidQuote = new QuoteDto(getUniqueId(), null, getUniqueId());

    String expectedErrorCode = ErrorCode.INVALID_PARAMS;

    mockMvc.perform(post(QUOTES_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serialize(invalidQuote)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value(expectedErrorCode))
        .andExpect(jsonPath("message").exists())
        .andExpect(jsonPath("at").exists());
  }

  @Test
  @SneakyThrows
  void createQuote_withNullContent_returnsErrorResponse() {
    QuoteDto invalidQuote = new QuoteDto(getUniqueId(), getUniqueId(), null);

    String expectedErrorCode = ErrorCode.INVALID_PARAMS;

    mockMvc.perform(post(QUOTES_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serialize(invalidQuote)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value(expectedErrorCode))
        .andExpect(jsonPath("message").exists())
        .andExpect(jsonPath("at").exists());
  }

  @Test
  @SneakyThrows
  void updateQuote_isOk() {
    QuoteDto updatedQuote = prepareQuoteDto();

    mockMvc.perform(put(QUOTES_ENDPOINT + "/{id}", quote.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serialize(updatedQuote)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value(quote.getId()))
        .andExpect(jsonPath("book").value(updatedQuote.getBook()))
        .andExpect(jsonPath("content").value(updatedQuote.getContent()));

    mockMvc.perform(get(QUOTES_ENDPOINT + "/{id}", quote.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value(quote.getId()))
        .andExpect(jsonPath("book").value(updatedQuote.getBook()))
        .andExpect(jsonPath("content").value(updatedQuote.getContent()));
  }

  @Test
  @SneakyThrows
  void updateQuote_notExisting_returnsErrorResponse() {
    String notExistingId = getUniqueId();

    String expectedErrorCode = ErrorCode.ENTITY_NOT_FOUND;
    String expectedErrorMessage = String.format("Quote with id [%s] not found", notExistingId);

    mockMvc.perform(put(QUOTES_ENDPOINT + "/{id}", notExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serialize(prepareQuoteDto())))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("code").value(expectedErrorCode))
        .andExpect(jsonPath("message").value(expectedErrorMessage))
        .andExpect(jsonPath("at").exists());
  }

  @Test
  @SneakyThrows
  void deleteQuote_isOk() {
    mockMvc.perform(delete(QUOTES_ENDPOINT + "/{id}", quote.getId()))
        .andExpect(status().isOk());

    String expectedErrorCode = ErrorCode.ENTITY_NOT_FOUND;
    String expectedErrorMessage = String.format("Quote with id [%s] not found", quote.getId());

    mockMvc.perform(get(QUOTES_ENDPOINT + "/{id}", quote.getId()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("code").value(expectedErrorCode))
        .andExpect(jsonPath("message").value(expectedErrorMessage))
        .andExpect(jsonPath("at").exists());
  }

  @Test
  @SneakyThrows
  void deleteQuote_notExisting_returnsErrorResponse() {
    String notExistingId = getUniqueId();

    String expectedErrorCode = ErrorCode.ENTITY_NOT_FOUND;
    String expectedErrorMessage = String.format("Quote with id [%s] not found", notExistingId);

    mockMvc.perform(delete(QUOTES_ENDPOINT + "/{id}", notExistingId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("code").value(expectedErrorCode))
        .andExpect(jsonPath("message").value(expectedErrorMessage))
        .andExpect(jsonPath("at").exists());
  }

}