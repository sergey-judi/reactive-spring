package tech.pm.service.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.pm.model.Quote;

public interface QuoteReactiveService {

  Flux<Quote> getAll();
  Flux<Quote> getAllByPage(int page, int size);

  Mono<Quote> get(String id);
  Mono<Quote> update(String id, Quote quote);
  Mono<Quote> create(Quote quote);
  Mono<Void> delete(String id);

}

