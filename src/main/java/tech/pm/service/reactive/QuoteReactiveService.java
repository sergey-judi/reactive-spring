package tech.pm.service.reactive;

import reactor.core.publisher.Flux;
import tech.pm.model.Quote;

public interface QuoteReactiveService {

  Flux<Quote> getAll();
  Flux<Quote> getAllByPage(int page, int size);

}

