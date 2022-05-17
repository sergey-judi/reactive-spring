package tech.pm.exception.model;

public final class ErrorCode {

  private ErrorCode() {
    throw new UnsupportedOperationException();
  }

  public static final String GENERAL = "error.service.general";

  public static final String ENTITY_NOT_FOUND = "error.service.entity.not-found";
  public static final String ENTITY_ALREADY_EXISTS = "error.service.entity.already-exists";

  public static final String INVALID_PARAMS = "error.service.invalid-params";

}
