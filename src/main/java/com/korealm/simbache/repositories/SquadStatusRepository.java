package com.korealm.simbache.repositories;

import com.korealm.simbache.models.SquadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SquadStatusRepository extends JpaRepository<SquadStatus, Short> {
    Optional<SquadStatus> findByStatusId(short statusId);
    Optional<SquadStatus> findByStatusName(String statusName);
}
