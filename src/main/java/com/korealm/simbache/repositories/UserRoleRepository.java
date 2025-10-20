package com.korealm.simbache.repositories;

import com.korealm.simbache.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Short> {
    Optional<UserRole> findByRoleId(short roleId);
    Optional<UserRole> findByRoleName(String roleName);
}
