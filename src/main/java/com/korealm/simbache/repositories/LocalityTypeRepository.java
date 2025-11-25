package com.korealm.simbache.repositories;

import com.korealm.simbache.models.LocalityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalityTypeRepository extends JpaRepository<LocalityType, Short> {
    Optional<LocalityType> findById(short localityTypeId);
    Optional<LocalityType> findByName(String localityTypeName);
}
