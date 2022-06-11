package com.developersboard.backend.persistent.repository.impl;

import com.developersboard.backend.persistent.domain.user.Role;
import com.developersboard.backend.persistent.repository.RoleRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * The RoleRepositoryImpl class provides implementation for the RoleRepository definitions.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {
  @PersistenceContext private final EntityManager entityManager;

  @Override
  public Role merge(Role role) {
    entityManager.merge(role);
    return role;
  }

  @Override
  public Role findByName(String name) {

    return entityManager
        .createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
        .setParameter("name", name)
        .getResultList()
        .stream()
        .findFirst()
        .orElse(null);
  }
}
