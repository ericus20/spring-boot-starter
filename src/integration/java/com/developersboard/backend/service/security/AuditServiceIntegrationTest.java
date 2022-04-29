package com.developersboard.backend.service.security;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.backend.persistent.domain.user.User;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.UserUtils;
import java.util.List;
import org.hibernate.envers.RevisionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class AuditServiceIntegrationTest extends IntegrationTestUtils {

  private transient UserDto storedUserDto;

  @BeforeAll
  void beforeAll() {
    UserDto userDto = UserUtils.createUserDto(false);
    storedUserDto = createAndAssertUser(userDto);
  }

  @Test
  void testReturnsAuditLogsForCreatedEntity() {

    List<?> auditLogsWithRevision = auditService.getAuditLogs(User.class);
    Assertions.assertFalse(auditLogsWithRevision.isEmpty());

    Object[] auditLogs = (Object[]) auditLogsWithRevision.get(0);
    Assertions.assertEquals(3, auditLogs.length);

    RevisionType revisionType = (RevisionType) auditLogs[2];
    Assertions.assertEquals(revisionType, RevisionType.ADD);
  }

  @Test
  void testReturnAuditLogsForEntitiesOnly() {
    List<?> auditLogsWithRevision = auditService.getAuditLogs(User.class, false, true, false);

    // Can directly access user entity since we asked for audit logs for entities only
    User user = (User) auditLogsWithRevision.get(0);
    Assertions.assertNotNull(user);
    Assertions.assertEquals(storedUserDto.getId(), user.getId());
  }

  @Test
  void testReturnsAuditLogsForMostRecentEntityWithDeletedEntities() {
    var userDto = createAndAssertUser(UserUtils.createUserDto(false));

    userService.deleteUser(userDto.getPublicId());
    Assertions.assertFalse(userService.existsByUsername(userDto.getUsername()));

    var auditLogs = auditService.getAuditLogs(User.class, true, true);
    Assertions.assertFalse(auditLogs.isEmpty());

    // since we are selecting most recent, the deletion should be the first entry
    var auditLog = (Object[]) auditLogs.get(0);
    var user = (User) auditLog[0];

    Assertions.assertEquals(user.getId(), userDto.getId());
    Assertions.assertEquals(RevisionType.DEL, auditLog[2]);
  }
}
