package tech.pm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.pm.controller.dto.QuoteDto;
import tech.pm.converter.QuoteConverter;
import tech.pm.service.reactive.QuoteReactiveService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reactive")
public class QuoteReactiveController {

  private final QuoteReactiveService quoteService;
  private final QuoteConverter quoteConverter;

  @GetMapping("/quotes")
  public Flux<QuoteDto> getAll() {
    log.info("Retrieving all quotes");
    return quoteService.getAll()
        .map(quoteConverter::toDto)
        .doOnComplete(() -> log.info("Successfully retrieved all quotes"));
  }

  @GetMapping("/quotes/paged")
  public Flux<QuoteDto> getAll(@RequestParam("page") int page,
                               @RequestParam("size") int size) {
    log.info("Retrieving all quotes by page [{}] and size [{}]", page, size);
    return quoteService.getAllByPage(page, size)
        .map(quoteConverter::toDto)
        .doOnComplete(() -> log.info("Successfully retrieved by page [{}] and size [{}]", page, size));
  }

  @GetMapping("/{id}")
  public Mono<QuoteDto> getById(@PathVariable String id) {
    log.info("Retrieving quote by id [{}]", id);
    return quoteService.get(id)
        .map(quoteConverter::toDto)
        .doOnSuccess(retrieved -> log.info("Successfully retrieved quote [{}]", retrieved));
  }

  @PostMapping
  public Mono<QuoteDto> create(@Validated @RequestBody QuoteDto quoteDto) {
    log.info("Creating quote [{}]", quoteDto);
    return quoteService.create(quoteConverter.fromDto(quoteDto))
        .map(quoteConverter::toDto)
        .doOnSuccess(created -> log.info("Successfully created quote [{}]", created));
  }

  @PutMapping("/{id}")
  public Mono<QuoteDto> update(@PathVariable String id,
                               @Validated @RequestBody QuoteDto quoteDto) {
    log.info("Updating quote by id [{}], [{}]", id, quoteDto);
    return quoteService.update(id, quoteConverter.fromDto(quoteDto))
        .map(quoteConverter::toDto)
        .doOnSuccess(updated -> log.info("Successfully updated quote [{}]", updated));
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteById(@PathVariable String id) {
    log.info("Deleting quote by id [{}]", id);
    return quoteService.delete(id)
        .doOnSuccess(deleted -> log.info("Successfully deleted quote by id [{}]", id));
  }

}