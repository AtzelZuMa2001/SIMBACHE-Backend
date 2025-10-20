package com.korealm.simbache.repositories;

import com.korealm.simbache.models.SquadRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SquadRoleRepository extends JpaRepository<SquadRole, Short> {
    Optional<SquadRole> findByRoleId(short roleId);
    Optional<SquadRole> findByRoleName(String roleName);
}
