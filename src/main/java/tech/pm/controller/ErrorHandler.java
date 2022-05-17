package tech.pm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import tech.pm.exception.EntityAlreadyExistsException;
import tech.pm.exception.EntityNotFoundException;
import tech.pm.exception.model.ErrorResponse;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static tech.pm.exception.model.ErrorCode.ENTITY_ALREADY_EXISTS;
import static tech.pm.exception.model.ErrorCode.ENTITY_NOT_FOUND;
import static tech.pm.exception.model.ErrorCode.GENERAL;
import static tech.pm.exception.model.ErrorCode.INVALID_PARAMS;

@Slf4j
@RestControllerAdvice(assignableTypes = {QuoteBlockingController.class, QuoteReactiveController.class})
public class ErrorHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(NOT_FOUND)
  public Mono<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
    log.error("Handling entity not found exception: [{}]", ex.getMessage());
    return Mono.just(buildErrorResponse(ENTITY_NOT_FOUND, ex.getMessage()));
  }

  @ExceptionHandler(EntityAlreadyExistsException.class)
  @ResponseStatus(BAD_REQUEST)
  public Mono<ErrorResponse> handleEntityNotFoundException(EntityAlreadyExistsException ex) {
    log.error("Handling entity already exists exception: [{}]", ex.getMessage());
    return Mono.just(buildErrorResponse(ENTITY_ALREADY_EXISTS, ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(BAD_REQUEST)
  public Mono<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult().getGlobalErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .sorted()
        .collect(Collectors.toList());

    Map<String, List<String>> validation = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> Map.entry(error.getField(), error.getDefaultMessage()))
        .collect(Collectors.groupingBy(
            Map.Entry::getKey,
            Collectors.mapping(
                Map.Entry::getValue,
                Collectors.toList()
            )));

    errors.addAll(
        validation.entrySet()
            .stream()
            .map(entry -> entry.getKey() + ": " + String.join(", ", entry.getValue()))
            .sorted()
            .toList()
    );

    String errorMessage = String.join(", ", errors);

    log.error("Handling validation exception: [{}]", errorMessage);
    return Mono.just(buildErrorResponse(INVALID_PARAMS, errorMessage));
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  public Mono<ErrorResponse> handleGeneralException(Exception ex) {
    log.error("Handling general exception: [{}]", ex.getMessage());
    return Mono.just(buildErrorResponse(GENERAL, ex.getMessage()));
  }

  private ErrorResponse buildErrorResponse(String code, String description) {
    return ErrorResponse.builder()
        .code(code)
        .message(description)
        .at(Instant.now())
        .build();
  }

}
