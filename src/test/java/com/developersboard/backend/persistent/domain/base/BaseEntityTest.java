package com.developersboard.backend.persistent.domain.base;

import com.developersboard.TestUtils;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * Test class for the BaseEntity.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
class BaseEntityTest {

  @Test
  void testBaseEntityWithString(TestInfo testInfo) {
    var baseEntity = createBaseEntity(testInfo.getDisplayName());

    Assertions.assertAll(
        () -> {
          Assertions.assertEquals(testInfo.getDisplayName(), baseEntity.getId());
          Assertions.assertEquals(0L, baseEntity.getVersion());
        });
  }

  @Test
  void testBaseEntityWithLong() {
    var id = 1L;
    var baseEntity = createBaseEntity(id);

    Assertions.assertEquals(id, baseEntity.getId());
  }

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(BaseEntity.class)
        .withOnlyTheseFields(TestUtils.getBaseEqualsAndHashCodeFields().toArray(new String[0]))
        .verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(BaseEntity.class).withClassName(NameStyle.SIMPLE_NAME).verify();
  }

  private <T extends Serializable> BaseEntity<T> createBaseEntity(T id) {
    var baseEntity = new BaseEntity<T>();
    baseEntity.setId(id);
    baseEntity.setCreatedBy("system");
    baseEntity.setUpdatedBy(baseEntity.getCreatedBy());
    baseEntity.setPublicId(UUID.randomUUID().toString());
    baseEntity.setCreatedAt(LocalDateTime.now(Clock.systemUTC()));
    baseEntity.setUpdatedAt(LocalDateTime.now(Clock.systemUTC()));

    return baseEntity;
  }
}
