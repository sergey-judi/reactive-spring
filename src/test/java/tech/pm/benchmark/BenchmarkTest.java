package tech.pm.benchmark;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import tech.pm.controller.dto.QuoteDto;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Disabled // comment to run benchmark
public class BenchmarkTest {

  private static final int THREADS_AMOUNT = 32;

  private static final String BLOCKING_QUOTES_ENDPOINT = "/blocking/quotes";
  private static final String REACTIVE_QUOTES_ENDPOINT = "/reactive/quotes";

  private static final String PAGED_REQUEST = "/paged?page=5&size=100";

  private static final String BASE_URL = "http://localhost:8444";

  @ParameterizedTest
  @MethodSource("requests")
  void benchmark_blocking(int threadsAmount) {
    run(threadsAmount, THREADS_AMOUNT, BLOCKING_QUOTES_ENDPOINT + PAGED_REQUEST);
  }

  @ParameterizedTest
  @MethodSource("requests")
  void benchmark_reactive(int threadsAmount) {
    run(threadsAmount, THREADS_AMOUNT, REACTIVE_QUOTES_ENDPOINT + PAGED_REQUEST);
  }

  @SneakyThrows
  private void run(int requestsAmount, int threadsAmount, String endpoint) {
    long start = System.nanoTime();

    WebClient webClient = WebClient.create(BASE_URL);
    Map<Integer, BenchmarkRequestResult<List<QuoteDto>>> results = new HashMap<>();

    List<Callable<BenchmarkRequestResult<List<QuoteDto>>>> requestCallableList = IntStream.range(0, requestsAmount)
        .mapToObj(i -> createMonoRequest(i, webClient, endpoint))
        .collect(Collectors.toList());

    log.info("========== NEW BENCHMARK --> RequestsAmount: {}, ThreadsAmount: {}, Endpoint: {}", requestsAmount, threadsAmount, endpoint);
    log.info("========== Requests created");

    ExecutorService executorService = Executors.newFixedThreadPool(threadsAmount);
    ExecutorCompletionService<BenchmarkRequestResult<List<QuoteDto>>> completionService = new ExecutorCompletionService<>(executorService);
    requestCallableList.forEach(completionService::submit);

    log.info(" ========== Requests submitted @ {}", Duration.ofNanos(System.nanoTime() - start));

    for (int n = 0; n < requestCallableList.size(); n++) {
      BenchmarkRequestResult<List<QuoteDto>> benchmarkRequestResult = completionService.take().get();
      results.put(benchmarkRequestResult.getRequestId(), benchmarkRequestResult);
    }

    log.info("========== Requests completed @ {}", Duration.ofNanos(System.nanoTime() - start));

    log.info("========== RESULTS ==========");

    double avg = results.values()
        .stream()
        .mapToLong(BenchmarkRequestResult::getTookTimeNs)
        .average()
        .getAsDouble();

    log.info("Average time per request: {}", Duration.ofNanos(Math.round(avg)));

    double successRate = results.values().stream().
        filter(r -> r.getResponseEntity().getStatusCode().equals(HttpStatus.OK))
        .count() * 100.0 / results.size();

    double errorRate = 100.0 - successRate;

    log.info("Success Rate: {}", successRate);
    log.info("Error Rate:   {}", errorRate);

    long objectsNumber = results.values()
        .stream()
        .map(r -> r.getResponseEntity().getBody().size())
        .reduce(Integer::sum)
        .get();

    log.info("Total Number of objects: {}", objectsNumber);

    long end = System.nanoTime();
    log.info("========== Benchmark took {} ", Duration.ofNanos(end - start));
  }

  private Callable<BenchmarkRequestResult<List<QuoteDto>>> createMonoRequest(int requestId, WebClient webClient, final String uri) {
    return () -> {
      long start = System.nanoTime();

      ResponseEntity<List<QuoteDto>> responseEntity = webClient.get()
          .uri(uri)
          .retrieve()
          .toEntity(new ParameterizedTypeReference<List<QuoteDto>>() {})
          .block();

      long end = System.nanoTime();

      return new BenchmarkRequestResult<>(requestId, end - start, responseEntity);
    };
  }

  private static Stream<Arguments> requests() {
    return Stream.of(
        Arguments.of(1),
        Arguments.of(16),
        Arguments.of(64),
        Arguments.of(128),
        Arguments.of(256),
        Arguments.of(512)
    );
  }

}
