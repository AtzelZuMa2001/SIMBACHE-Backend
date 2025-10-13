package com.korealm.simbache.repositories;

import com.korealm.simbache.models.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Short> {
    Optional<VehicleType> findByTypeId(short typeId);
    Optional<VehicleType> findByTypeName(String typeName);

    void deleteByTypeId(short typeId);
}
