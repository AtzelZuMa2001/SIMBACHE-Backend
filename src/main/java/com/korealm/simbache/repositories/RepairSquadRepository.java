package com.korealm.simbache.repositories;

import com.korealm.simbache.models.RepairSquad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairSquadRepository extends JpaRepository<RepairSquad, Long> {
}
