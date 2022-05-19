package tech.pm.controller.blocking;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tech.pm.controller.AbstractBaseTest;
import tech.pm.controller.dto.QuoteDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public abstract class AbstractBaseBlockingTest extends AbstractBaseTest {

  protected static final String QUOTES_ENDPOINT = "/blocking/quotes";

  @Autowired
  protected MockMvc mockMvc;

  @Override
  @SneakyThrows
  protected QuoteDto insertQuote() {
    MvcResult mvcResult = mockMvc.perform(post(QUOTES_ENDPOINT)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(serialize(prepareQuoteDto())))
        .andExpect(status().isCreated())
        .andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString();
    return objectMapper.readValue(responseBody, QuoteDto.class);
  }

}
