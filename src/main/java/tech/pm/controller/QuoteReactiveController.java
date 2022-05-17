package tech.pm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import tech.pm.model.Quote;
import tech.pm.service.reactive.QuoteReactiveService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reactive")
public class QuoteReactiveController {

  private final QuoteReactiveService quoteService;

  @GetMapping("/quotes")
  public Flux<Quote> getAll() {
    return quoteService.getAll();
  }

  @GetMapping("/quotes/paged")
  public Flux<Quote> getAll(@RequestParam(name = "page") int page,
                            @RequestParam(name = "size") int size) {
    return quoteService.getAllByPage(page, size);
  }

}