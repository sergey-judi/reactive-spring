package tech.pm.converter;

public interface Converter<D, M> {
  D toDto(M model);
  M fromDto(D dto);
}
