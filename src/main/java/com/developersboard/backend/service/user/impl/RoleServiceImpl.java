package com.developersboard.backend.service.user.impl;

import com.developersboard.backend.persistent.domain.user.Role;
import com.developersboard.backend.persistent.repository.RoleRepository;
import com.developersboard.backend.service.user.RoleService;
import com.developersboard.constant.CacheConstants;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The RoleServiceImpl class is an implementation for the RoleService Interface.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

  private final transient RoleRepository roleEntityRepository;

  /**
   * The controller for injection.
   *
   * @param roleEntityRepository the roleEntityRepository
   */
  @Autowired
  public RoleServiceImpl(@NonNull RoleRepository roleEntityRepository) {
    this.roleEntityRepository = roleEntityRepository;
  }

  /**
   * Create the roleEntity with the roleEntity instance given.
   *
   * @param roleEntity the roleEntity
   * @return the persisted roleEntity with assigned id
   */
  @Override
  @Transactional
  public Role saveRole(Role roleEntity) {
    Validate.notNull(roleEntity, "The roleEntity cannot be null");
    return roleEntityRepository.save(roleEntity);
  }

  /**
   * Retrieves the role with the specified id.
   *
   * @param id the id of the role to retrieve
   * @return the role tuple that matches the id given
   */
  @Override
  public Role getRoleById(Integer id) {
    Validate.notNull(id, "The id cannot be null");
    return roleEntityRepository.findById(id).orElse(null);
  }

  /**
   * Retrieves the role with the specified name.
   *
   * @param name the name of the role to retrieve
   * @return the role tuple that matches the id given
   */
  @Override
  @Cacheable(CacheConstants.ROLES)
  public Role getRoleByName(String name) {
    Validate.notNull(name, "The name cannot be null");
    return roleEntityRepository.findByName(name);
  }
}
