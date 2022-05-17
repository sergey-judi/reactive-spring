package tech.pm.service.blocking;

import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tech.pm.config.properties.ApplicationProperties;
import tech.pm.config.properties.DelayProperties;
import tech.pm.exception.EntityAlreadyExistsException;
import tech.pm.exception.EntityNotFoundException;
import tech.pm.model.Quote;
import tech.pm.repository.QuoteBlockingRepository;

import java.util.List;

import static java.lang.String.format;

@Service
public class DefaultQuoteBlockingService implements QuoteBlockingService {

  private final QuoteBlockingRepository quoteRepository;
  private final DelayProperties delayProperties;

  public DefaultQuoteBlockingService(QuoteBlockingRepository quoteRepository, ApplicationProperties applicationProperties) {
    this.quoteRepository = quoteRepository;
    this.delayProperties = applicationProperties.getDelay();
  }

  @Override
  public List<Quote> getAll() {
    delayIfNeeded();
    return quoteRepository.findAll();
  }

  @Override
  public List<Quote> getAllByPage(int page, int size) {
    delayIfNeeded();
    return quoteRepository.findAllByIdNotNullOrderByIdAsc(PageRequest.of(page, size));
  }

  @Override
  public Quote get(String id) {
    return quoteRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(format("Quote with id [%s] not found", id)));
  }

  @Override
  public Quote update(String id, Quote quote) {
    Quote quoteFromDb = get(id);

    quoteFromDb.setBook(quote.getBook());
    quoteFromDb.setContent(quote.getContent());

    return quoteRepository.save(quoteFromDb);
  }

  @Override
  public Quote create(Quote quote) {
    if (quoteRepository.findById(quote.getId()).isPresent()) {
      throw new EntityAlreadyExistsException(format("Quote with id [%s] already exists", quote.getId()));
    }

    return quoteRepository.save(quote);
  }

  @Override
  public void delete(String id) {
    get(id);
    quoteRepository.deleteById(id);
  }

  @SneakyThrows
  public void delayIfNeeded() {
    if (delayProperties.isEnabled()) {
      Thread.sleep(delayProperties.getDuration().toMillis() * quoteRepository.count());
    }
  }

}
