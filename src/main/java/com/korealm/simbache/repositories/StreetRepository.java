package com.korealm.simbache.repositories;

import com.korealm.simbache.models.Colonia;
import com.korealm.simbache.models.Locality;
import com.korealm.simbache.models.Street;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StreetRepository extends JpaRepository<Street, Long> {
    Optional<Street> findByStreetId(Long streetId);
    Optional<Street> findByStreetNameAndLocality(String streetName, Locality locality);

    List<Street> findAllByLocality(Locality locality);
    List<Street> findAllByLocality_LocalityId(int localityId);
    List<Street> findAllByColonia(Colonia colonia);
    List<Street> findAllByColonia_ColoniaId(Long coloniaId);
    List<Street> findAllByStreetNameContainingIgnoreCase(String name);
}
