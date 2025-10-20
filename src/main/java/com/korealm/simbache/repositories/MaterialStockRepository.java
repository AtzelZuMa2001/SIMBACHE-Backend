package com.korealm.simbache.repositories;

import com.korealm.simbache.models.Location;
import com.korealm.simbache.models.Material;
import com.korealm.simbache.models.MaterialStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialStockRepository extends JpaRepository<MaterialStock, Long> {
    Optional<MaterialStock> findByStockId(Long stockId);

    List<MaterialStock> findAllByMaterial(Material material);
    List<MaterialStock> findAllByMaterial_MaterialId(Long materialId);

    List<MaterialStock> findAllByWarehouseLocation(Location location);
    List<MaterialStock> findAllByWarehouseLocation_LocationId(Long locationId);
}
