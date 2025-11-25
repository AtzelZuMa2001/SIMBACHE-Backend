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
    Optional<Locality> findAllByLocalityNameAndMunicipality_MunicipalityId(String localityName, short municipalityId);

    List<Locality> findAllByPostalCode(int postalCode);
    List<Locality> findAllByMunicipality_MunicipalityId(short municipalityId);
    List<Locality> findAllByLocalityNameContainingIgnoreCase(String name);
}
