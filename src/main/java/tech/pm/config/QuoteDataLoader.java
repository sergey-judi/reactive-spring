package tech.pm.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import tech.pm.config.properties.ApplicationProperties;
import tech.pm.model.Quote;
import tech.pm.repository.QuoteReactiveRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuoteDataLoader implements ApplicationListener<ApplicationStartedEvent> {

  private final ApplicationProperties applicationProperties;
  private final QuoteReactiveRepository quoteReactiveRepository;

  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {
    log.info("Found {} entities in db", quoteReactiveRepository.count().block());

    if (quoteReactiveRepository.findAll().count().block().equals(0L)) {
      Supplier<String> idGenerator = getIdGenerator();

      BufferedReader dataReader = new BufferedReader(new InputStreamReader(
          requireNonNull(getClass().getClassLoader().getResourceAsStream(applicationProperties.getDataFile()))));

      List<Quote> quotes = dataReader.lines()
          .filter(line -> !line.trim().isEmpty())
          .map(line -> {
            String id = idGenerator.get();
            String book = String.format("book-%s", id);
            return new Quote(id, book, line);
          })
          .toList();

      Flux.fromIterable(quotes)
          .flatMap(quoteReactiveRepository::save)
          .subscribe();

      log.info("Successfully loaded {} entities into db", quoteReactiveRepository.count().block());
    }
  }

  private Supplier<String> getIdGenerator() {
    AtomicLong id = new AtomicLong();
    return () -> String.format("%05d", id.incrementAndGet());
  }

}
