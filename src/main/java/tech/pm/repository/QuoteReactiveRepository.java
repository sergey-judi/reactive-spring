package tech.pm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import tech.pm.model.Quote;

@Repository
public interface QuoteReactiveRepository extends ReactiveMongoRepository<Quote, String> {

  Flux<Quote> findAllByIdNotNullOrderByIdAsc(Pageable page);

}
