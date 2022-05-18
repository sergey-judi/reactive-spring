package tech.pm.controller.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class QuoteDto {

  @NotBlank(groups = OnCreate.class)
  String id;

  @NotBlank(groups = OnCreate.class)
  String book;

  @NotBlank(groups = OnCreate.class)
  String content;

  public interface OnCreate {}

}
