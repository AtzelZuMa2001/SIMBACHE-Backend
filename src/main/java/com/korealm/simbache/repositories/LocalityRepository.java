package com.korealm.simbache.repositories;

import com.korealm.simbache.models.Locality;
import com.korealm.simbache.models.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalityRepository extends JpaRepository<Locality, Integer> {
    Optional<Locality> findByLocalityId(int localityId);
    Optional<Locality> findByLocalityName(String localityName);

    List<Locality> findAllByMunicipality(Municipality municipality);
    List<Locality> findAllByMunicipality_MunicipalityId(short municipalityId);
    List<Locality> findAllByType(String type);
    List<Locality> findAllByLocalityNameContainingIgnoreCase(String name);
}
