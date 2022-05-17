package tech.pm.controller.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class QuoteDto {

  @NotBlank String id;
  @NotBlank String book;
  @NotBlank String content;

}
