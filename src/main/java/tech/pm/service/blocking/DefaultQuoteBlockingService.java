package tech.pm.service.blocking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tech.pm.model.Quote;
import tech.pm.repository.QuoteBlockingRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultQuoteBlockingService implements QuoteBlockingService {

  private final QuoteBlockingRepository quoteRepository;

  @Override
  public List<Quote> getAll() {
    return quoteRepository.findAll();
  }

  @Override
  public List<Quote> getAllByPage(int page, int size) {
    return quoteRepository.findAllByIdNotNullOrderByIdAsc(PageRequest.of(page, size));
  }

}
