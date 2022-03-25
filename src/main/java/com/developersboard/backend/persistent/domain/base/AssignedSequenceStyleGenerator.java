package com.developersboard.backend.persistent.domain.base;

import java.io.Serializable;
import java.util.Objects;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

/**
 * A custom sequence generator that can also accommodate manually assigned identifier.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public class AssignedSequenceStyleGenerator extends SequenceStyleGenerator {

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object) {
    if (object instanceof Identifiable<?> identifiable) {
      Serializable id = identifiable.getId();
      if (Objects.nonNull(id)) {
        return id;
      }
    }
    return super.generate(session, object);
  }
}
