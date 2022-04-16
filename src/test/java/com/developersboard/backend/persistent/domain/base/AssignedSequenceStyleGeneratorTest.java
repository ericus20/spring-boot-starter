package com.developersboard.backend.persistent.domain.base;

import java.io.Serializable;
import java.util.UUID;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssignedSequenceStyleGeneratorTest {

  @Mock private transient SharedSessionContractImplementor implementor;

  @Mock private transient SequenceStyleGenerator generator;

  private transient AssignedSequenceStyleGenerator assignedSequenceStyleGenerator;

  @BeforeEach
  void setUp() {
    assignedSequenceStyleGenerator = new AssignedSequenceStyleGenerator();
  }

  @Test
  void generateSequenceWithLongId() {
    final Long longId = 1L;
    Identifiable<Long> id = () -> longId;

    Serializable generatedId = assignedSequenceStyleGenerator.generate(implementor, id);
    Assertions.assertEquals(longId, generatedId);

    Mockito.verify(generator, Mockito.never()).generate(implementor, id);
  }

  @Test
  void generateSequenceWithIntegerId() {
    final Integer integerId = 1;
    Identifiable<Integer> id = () -> integerId;

    Serializable generatedId = assignedSequenceStyleGenerator.generate(implementor, id);
    Assertions.assertEquals(integerId, generatedId);

    Mockito.verify(generator, Mockito.never()).generate(implementor, id);
  }

  @Test
  void generateSequenceWithStringId() {
    final String stringId = UUID.randomUUID().toString();
    Identifiable<String> id = () -> stringId;

    Serializable generatedId = assignedSequenceStyleGenerator.generate(implementor, id);
    Assertions.assertEquals(stringId, generatedId);

    Mockito.verify(generator, Mockito.never()).generate(implementor, id);
  }
}
