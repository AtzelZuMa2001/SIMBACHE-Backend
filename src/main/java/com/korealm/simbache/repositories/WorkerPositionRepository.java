package com.korealm.simbache.repositories;

import com.korealm.simbache.models.WorkerPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerPositionRepository extends JpaRepository<WorkerPosition, Short> {
}
