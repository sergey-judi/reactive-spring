package tech.pm.service.blocking;

import tech.pm.model.Quote;

import java.util.List;

public interface QuoteBlockingService {

  List<Quote> getAll();
  List<Quote> getAllByPage(int page, int size);

  Quote get(String id);
  Quote update(String id, Quote quote);
  Quote create(Quote quote);
  void delete(String id);

}
