package com.developersboard.backend.service.security;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.backend.persistent.domain.user.User;
import com.developersboard.backend.service.user.UserService;
import com.developersboard.enums.UserHistoryType;
import com.developersboard.shared.dto.UserDto;
import com.developersboard.shared.util.StringUtils;
import com.developersboard.shared.util.UserUtils;
import java.util.List;
import org.hibernate.envers.RevisionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuditServiceIntegrationTest extends IntegrationTestUtils {

  @Autowired private transient AuditService auditService;

  @Autowired private transient UserService userService;

  private transient UserDto userDto;

  @BeforeEach
  void setUp() {
    userDto = createAndAssertUser(userService, UserUtils.createUserDto(false));
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
    Assertions.assertEquals(userDto.getId(), user.getId());
  }

  @Test
  void testReturnsAuditLogsForCreatedEntityWithDeletedEntities() {
    UserDto userDto = createAndAssertUser(userService, UserUtils.createUserDto(false));
    userDto.setLastName(StringUtils.FAKER.name().lastName());
    userService.updateUser(userDto, UserHistoryType.PROFILE_UPDATE);

    List<?> auditLogsWithRevision = auditService.getAuditLogs(User.class);
    Assertions.assertFalse(auditLogsWithRevision.isEmpty());
  }
}
