package com.developersboard.backend.service.user.impl;

import com.developersboard.backend.persistent.domain.user.Role;
import com.developersboard.backend.persistent.repository.RoleRepository;
import com.developersboard.backend.service.user.RoleService;
import com.developersboard.constant.CacheConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The RoleServiceImpl class is an implementation for the RoleService Interface.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

  private final transient RoleRepository roleRepository;

  /**
   * Create the roleEntity with the roleEntity instance given.
   *
   * @param roleEntity the roleEntity
   * @return the persisted roleEntity with assigned id
   */
  @Override
  @Transactional
  public Role save(Role roleEntity) {
    Validate.notNull(roleEntity, "The roleEntity cannot be null");

    var persistedRole = roleRepository.merge(roleEntity);
    LOG.info("Role merged successfully {}", persistedRole);

    return persistedRole;
  }

  /**
   * Retrieves the role with the specified name.
   *
   * @param name the name of the role to retrieve
   * @return the role tuple that matches the id given
   */
  @Override
  @Cacheable(CacheConstants.ROLES)
  public Role findByName(String name) {
    Validate.notNull(name, "The name cannot be null");

    return roleRepository.findByName(name);
  }
}
