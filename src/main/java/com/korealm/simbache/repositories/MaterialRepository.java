package com.korealm.simbache.repositories;

import com.korealm.simbache.models.Material;
import com.korealm.simbache.models.MaterialUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByMaterialId(Long materialId);
    Optional<Material> findByMaterialName(String materialName);

    List<Material> findAllByUnit(MaterialUnit unit);
    List<Material> findAllByUnit_UnitId(Short unitId);
}
