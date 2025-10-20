package com.korealm.simbache.repositories;

import com.korealm.simbache.models.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Long> {
}
