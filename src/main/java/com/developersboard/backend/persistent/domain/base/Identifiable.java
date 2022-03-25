package com.developersboard.backend.persistent.domain.base;

import java.io.Serializable;

/**
 * This interface is to provide a generic Identity for entities.
 *
 * @param <T> the ID type
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@FunctionalInterface
public interface Identifiable<T extends Serializable> {

  /**
   * Return a serialized generic id to be used as primary keys for entities.
   *
   * @return id
   */
  T getId();
}
