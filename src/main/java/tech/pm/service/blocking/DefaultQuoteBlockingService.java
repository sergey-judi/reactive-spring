package tech.pm.service.blocking;

import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tech.pm.config.properties.ApplicationProperties;
import tech.pm.config.properties.DelayProperties;
import tech.pm.model.Quote;
import tech.pm.repository.QuoteBlockingRepository;

import java.util.List;

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

  @SneakyThrows
  public void delayIfNeeded() {
    if (delayProperties.isEnabled()) {
      Thread.sleep(delayProperties.getDuration().toMillis() * quoteRepository.count());
    }
  }

}
