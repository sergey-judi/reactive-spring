package tech.pm.config.properties;

import lombok.Data;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.MICROS;

@Data
public class DelayProperties {

  private boolean enabled;

  @DurationUnit(MICROS)
  private Duration duration;

}
