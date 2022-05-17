package tech.pm.service.reactive;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import tech.pm.config.properties.ApplicationProperties;
import tech.pm.config.properties.DelayProperties;
import tech.pm.model.Quote;
import tech.pm.repository.QuoteReactiveRepository;

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

}
