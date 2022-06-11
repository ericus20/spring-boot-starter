package com.developersboard.backend.persistent.repository;

import com.developersboard.backend.persistent.domain.user.Role;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * Repository for the role.
 *
 * @author George Anguah
 * @version 1.0
 * @since 1.0
 */
@Repository
@RepositoryRestResource(exported = false)
public interface RoleRepository {

  /**
   * Merges an existing role with the role instance given.
   *
   * @param role the role
   * @return the persisted role with assigned id
   */
  Role merge(Role role);

  /**
   * Gets role associated with required name.
   *
   * @param name name of role.
   * @return Role found.
   */
  Role findByName(String name);
}
