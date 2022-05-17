package tech.pm.converter;

import org.springframework.stereotype.Component;
import tech.pm.controller.dto.QuoteDto;
import tech.pm.model.Quote;

@Component
public class QuoteConverter implements Converter<QuoteDto, Quote> {

  @Override
  public QuoteDto toDto(Quote model) {
    return new QuoteDto(model.getId(), model.getBook(), model.getContent());
  }

  @Override
  public Quote fromDto(QuoteDto dto) {
    return new Quote(dto.getId(), dto.getBook(), dto.getContent());
  }

}
