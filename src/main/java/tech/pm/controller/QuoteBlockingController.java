package tech.pm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.pm.model.Quote;
import tech.pm.service.blocking.QuoteBlockingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blocking")
public class QuoteBlockingController {

  private final QuoteBlockingService quoteService;

  @GetMapping("/quotes")
  public List<Quote> getAll() {
    return quoteService.getAll();
  }

  @GetMapping("/quotes/paged")
  public List<Quote> getAll(@RequestParam(name = "page") int page,
                            @RequestParam(name = "size") int size) {
    return quoteService.getAllByPage(page, size);
  }

}
