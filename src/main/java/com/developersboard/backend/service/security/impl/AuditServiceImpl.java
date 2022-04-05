package com.developersboard.backend.service.security.impl;

import com.developersboard.backend.service.security.AuditService;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This is the implementation of the audit service.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditServiceImpl implements AuditService {

  private final EntityManager entityManager;

  /**
   * Retrieve all audit logs for the given entity.
   *
   * @param entity the entity
   * @return the audit logs
   */
  @Override
  public List<?> getAuditLogs(Class<?> entity) {
    return getAuditLogs(entity, false);
  }

  /**
   * Retrieve all audit logs for the given entity.
   *
   * @param entity the entity
   * @param selectMostRecent the most recent number of audit logs to return
   * @return the audit logs
   */
  @Override
  public List<?> getAuditLogs(Class<?> entity, boolean selectMostRecent) {
    return getAuditLogs(entity, selectMostRecent, false, false);
  }

  /**
   * Retrieve all audit logs for the given entity with selectDeletedEntities.
   *
   * @param entity the entity
   * @param selectMostRecent the most recent number of audit logs to return
   * @param selectDeletedEntities if true, deleted entities will be returned
   * @return the audit logs
   */
  @Override
  public List<?> getAuditLogs(
      Class<?> entity, boolean selectMostRecent, boolean selectDeletedEntities) {

    return getAuditLogs(entity, selectMostRecent, false, selectDeletedEntities);
  }

  /**
   * Retrieve all audit logs for the given entity with other options.
   *
   * @param entity the entity
   * @param selectMostRecent the most recent number of audit logs to return
   * @param selectEntitiesOnly if true, only the entities will be returned
   * @param selectDeletedEntities if true, deleted entities will be returned
   * @return the audit logs
   */
  @Override
  public List<?> getAuditLogs(
      Class<?> entity,
      boolean selectMostRecent,
      boolean selectEntitiesOnly,
      boolean selectDeletedEntities) {

    var auditReader = AuditReaderFactory.get(entityManager);
    var auditQuery =
        auditReader
            .createQuery()
            .forRevisionsOfEntity(entity, selectEntitiesOnly, selectDeletedEntities);

    if (selectMostRecent) {
      auditQuery.add(AuditEntity.revisionNumber().maximize().computeAggregationInInstanceContext());
    }

    auditQuery.addOrder(AuditEntity.revisionNumber().desc());

    return auditQuery.getResultList();
  }
}
