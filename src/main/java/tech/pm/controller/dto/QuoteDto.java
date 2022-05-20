package tech.pm.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteDto {

  @NotBlank(groups = OnCreate.class)
  String id;

  @NotBlank(groups = OnCreate.class)
  String book;

  @NotBlank(groups = OnCreate.class)
  String content;

  public interface OnCreate {}

}
