package com.korealm.simbache.repositories;

import com.korealm.simbache.models.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleStatusRepository extends JpaRepository<VehicleStatus, Short> {
    Optional<VehicleStatus> findByStatusId(short statusId);
    Optional<VehicleStatus> findByStatusName(String statusName);
}
