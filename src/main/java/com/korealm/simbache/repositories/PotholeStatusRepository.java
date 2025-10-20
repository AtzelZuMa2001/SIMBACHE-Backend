package com.korealm.simbache.repositories;

import com.korealm.simbache.models.PotholeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PotholeStatusRepository extends JpaRepository<PotholeStatus, Short> {
    Optional<PotholeStatus> findByStatusId(short statusId);
    Optional<PotholeStatus> findByStatusName(String statusName);
}
