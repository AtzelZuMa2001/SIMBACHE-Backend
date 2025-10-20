package com.korealm.simbache.repositories;

import com.korealm.simbache.models.RepairUseMaterials;
import com.korealm.simbache.models.RepairUseMaterialsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairUseMaterialsRepository extends JpaRepository<RepairUseMaterials, RepairUseMaterialsId> {
}
