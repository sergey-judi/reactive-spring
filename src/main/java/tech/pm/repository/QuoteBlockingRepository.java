package tech.pm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tech.pm.model.Quote;

import java.util.List;

@Repository
public interface QuoteBlockingRepository extends MongoRepository<Quote, String> {

  List<Quote> findAllByIdNotNullOrderByIdAsc(Pageable page);

}
