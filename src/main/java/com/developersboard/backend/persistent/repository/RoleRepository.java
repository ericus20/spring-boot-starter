package com.developersboard.backend.persistent.repository;

import com.developersboard.backend.persistent.domain.user.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the role.
 *
 * @author George Anguah
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

  /**
   * Gets role associated with required name.
   *
   * @param name name of role.
   * @return Role found.
   */
  Role findByName(String name);
}
