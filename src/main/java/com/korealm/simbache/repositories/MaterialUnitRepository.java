package com.korealm.simbache.repositories;

import com.korealm.simbache.models.MaterialUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialUnitRepository extends JpaRepository<MaterialUnit, Short> {
    Optional<MaterialUnit> findByUnitId(Short unitId);
    Optional<MaterialUnit> findByUnitName(String unitName);
}
