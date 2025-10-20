package com.korealm.simbache.repositories;

import com.korealm.simbache.models.RepairUseTransportation;
import com.korealm.simbache.models.RepairUseTransportationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairUseTransportationRepository extends JpaRepository<RepairUseTransportation, RepairUseTransportationId> {
}
