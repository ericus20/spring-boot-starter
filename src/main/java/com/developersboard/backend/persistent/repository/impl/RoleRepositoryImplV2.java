package com.developersboard.backend.persistent.repository.impl;

import com.developersboard.backend.persistent.domain.user.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The RoleRepositoryImplV2 class exposes implementation from JpaRepository on Role entity .
 *
 * @author George Anguah
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface RoleRepositoryImplV2 extends JpaRepository<Role, Integer> {

  Optional<Role> findFirstByName(String name);
}
