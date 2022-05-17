package tech.pm.service.blocking;

import tech.pm.model.Quote;

import java.util.List;

public interface QuoteBlockingService {

  List<Quote> getAll();
  List<Quote> getAllByPage(int page, int size);

}
