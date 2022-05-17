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
import tech.pm.controller.dto.QuoteDto;
import tech.pm.converter.QuoteConverter;
import tech.pm.service.blocking.QuoteBlockingService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/blocking/quotes")
public class QuoteBlockingController {

  private final QuoteBlockingService quoteService;
  private final QuoteConverter quoteConverter;

  @GetMapping
  public List<QuoteDto> getAll() {
    log.info("Retrieving all quotes");
    List<QuoteDto> quoteDtos = quoteService.getAll()
        .stream()
        .map(quoteConverter::toDto)
        .toList();
    log.info("Successfully retrieved all quotes");
    return quoteDtos;
  }

  @GetMapping("/paged")
  public List<QuoteDto> getAll(@RequestParam("page") int page,
                               @RequestParam("size") int size) {
    log.info("Retrieving all quotes by page [{}] and size [{}]", page, size);
    List<QuoteDto> quoteDtos = quoteService.getAllByPage(page, size)
        .stream()
        .map(quoteConverter::toDto)
        .toList();
    log.info("Successfully retrieved by page [{}] and size [{}]", page, size);
    return quoteDtos;
  }

  @GetMapping("/{id}")
  public QuoteDto getById(@PathVariable String id) {
    log.info("Retrieving quote by id [{}]", id);
    QuoteDto retrieved = quoteConverter.toDto(quoteService.get(id));
    log.info("Successfully retrieved quote [{}]", retrieved);
    return retrieved;
  }

  @PostMapping
  public QuoteDto create(@Validated @RequestBody QuoteDto quoteDto) {
    log.info("Creating quote [{}]", quoteDto);
    QuoteDto created = quoteConverter.toDto(
        quoteService.create(quoteConverter.fromDto(quoteDto)));
    log.info("Successfully created quote [{}]", created);
    return created;
  }

  @PutMapping("/{id}")
  public QuoteDto update(@PathVariable String id,
                         @Validated @RequestBody QuoteDto quoteDto) {
    log.info("Updating quote by id [{}], [{}]", id, quoteDto);
    QuoteDto updated = quoteConverter.toDto(
        quoteService.update(id, quoteConverter.fromDto(quoteDto)));
    log.info("Successfully updated quote [{}]", updated);
    return updated;
  }

  @DeleteMapping("/{id}")
  public void deleteById(@PathVariable String id) {
    log.info("Deleting quote by id [{}]", id);
    quoteService.delete(id);
    log.info("Successfully deleted quote by id [{}]", id);
  }

}
