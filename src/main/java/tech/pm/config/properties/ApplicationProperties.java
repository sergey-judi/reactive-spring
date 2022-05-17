package tech.pm.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("service")
public class ApplicationProperties {

  private String dataFile;
  private DelayProperties delay;

}
