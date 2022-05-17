package tech.pm.exception;

public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(String message) {
    super(message);
  }

}
