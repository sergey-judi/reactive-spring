package tech.pm.service.reactive;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.pm.config.properties.ApplicationProperties;
import tech.pm.config.properties.DelayProperties;
import tech.pm.exception.EntityAlreadyExistsException;
import tech.pm.exception.EntityNotFoundException;
import tech.pm.model.Quote;
import tech.pm.repository.QuoteReactiveRepository;

import static java.lang.String.format;

@Service
public class DefaultQuoteReactiveService implements QuoteReactiveService {

  private final QuoteReactiveRepository quoteRepository;
  private final DelayProperties delayProperties;

  public DefaultQuoteReactiveService(QuoteReactiveRepository quoteRepository, ApplicationProperties applicationProperties) {
    this.quoteRepository = quoteRepository;
    this.delayProperties = applicationProperties.getDelay();
  }

  @Override
  public Flux<Quote> getAll() {
    Flux<Quote> quotes = quoteRepository.findAll();

    return delayProperties.isEnabled()
        ? quotes.delayElements(delayProperties.getDuration())
        : quotes;
  }

  @Override
  public Flux<Quote> getAllByPage(int page, int size) {
    Flux<Quote> quotes = quoteRepository.findAllByIdNotNullOrderByIdAsc(PageRequest.of(page, size));

    return delayProperties.isEnabled()
        ? quotes.delayElements(delayProperties.getDuration())
        : quotes;
  }

  @Override
  public Mono<Quote> get(String id) {
    return quoteRepository.findById(id)
        .switchIfEmpty(Mono.error(new EntityNotFoundException(format("Quote with id [%s] not found", id))));
  }

  @Override
  public Mono<Quote> update(String id, Quote quote) {
    return get(id)
        .map(existing -> {
          existing.setBook(quote.getBook());
          existing.setContent(quote.getContent());
          return existing;
        })
        .flatMap(quoteRepository::save);
  }

  @Override
  public Mono<Quote> create(Quote quote) {
    return quoteRepository.findById(quote.getId())
        .flatMap(existing -> Mono.error(new EntityAlreadyExistsException(format("Quote with id [%s] already exists", quote.getId()))))
        .then(Mono.defer(() -> quoteRepository.save(quote)));
  }

  @Override
  public Mono<Void> delete(String id) {
    return get(id).flatMap(existing -> quoteRepository.deleteById(id));
  }

}
