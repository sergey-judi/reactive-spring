package tech.pm.exception.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ErrorResponse {

  private String code;
  private String message;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC")
  private Instant at;

}
