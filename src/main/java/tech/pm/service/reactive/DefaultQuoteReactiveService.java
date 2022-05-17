package tech.pm.service.reactive;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import tech.pm.model.Quote;
import tech.pm.repository.QuoteReactiveRepository;

@Service
@RequiredArgsConstructor
public class DefaultQuoteReactiveService implements QuoteReactiveService {

  private final QuoteReactiveRepository quoteRepository;

  @Override
  public Flux<Quote> getAll() {
    return quoteRepository.findAll();
  }

  @Override
  public Flux<Quote> getAllByPage(int page, int size) {
    return quoteRepository.findAllByIdNotNullOrderByIdAsc(PageRequest.of(page, size));
  }

}
