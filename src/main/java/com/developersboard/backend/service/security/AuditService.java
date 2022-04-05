package com.developersboard.backend.service.security;

import java.util.List;

/**
 * This is the contract for the audit service operations.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public interface AuditService {

  /**
   * Retrieve all audit logs for the given entity.
   *
   * @param entity the entity
   * @return the audit logs
   */
  List<?> getAuditLogs(Class<?> entity);

  /**
   * Retrieve all audit logs for the given entity.
   *
   * @param entity the entity
   * @param selectMostRecent the most recent number of audit logs to return
   * @return the audit logs
   */
  List<?> getAuditLogs(Class<?> entity, boolean selectMostRecent);

  /**
   * Retrieve all audit logs for the given entity with selectDeletedEntities.
   *
   * @param entity the entity
   * @param selectMostRecent the most recent number of audit logs to return
   * @param selectDeletedEntities if true, deleted entities will be returned
   * @return the audit logs
   */
  List<?> getAuditLogs(Class<?> entity, boolean selectMostRecent, boolean selectDeletedEntities);

  /**
   * Retrieve all audit logs for the given entity with other options.
   *
   * @param entity the entity
   * @param selectMostRecent the most recent number of audit logs to return
   * @param selectEntitiesOnly if true, only the entities will be returned
   * @param selectDeletedEntities if true, deleted entities will be returned
   * @return the audit logs
   */
  List<?> getAuditLogs(
      Class<?> entity,
      boolean selectMostRecent,
      boolean selectEntitiesOnly,
      boolean selectDeletedEntities);
}
