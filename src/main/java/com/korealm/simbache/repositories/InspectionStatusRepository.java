package com.korealm.simbache.repositories;

import com.korealm.simbache.models.InspectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionStatusRepository extends JpaRepository<InspectionStatus, Short> {
}
