package com.korealm.simbache.repositories;

import com.korealm.simbache.models.RepairStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairStatusRepository extends JpaRepository<RepairStatus, Short> {
}
