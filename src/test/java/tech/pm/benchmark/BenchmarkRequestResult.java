package tech.pm.benchmark;

import lombok.Value;
import org.springframework.http.ResponseEntity;

@Value
public class BenchmarkRequestResult<T> {

  int requestId;
  long tookTimeNs;
  ResponseEntity<T> responseEntity;

}
