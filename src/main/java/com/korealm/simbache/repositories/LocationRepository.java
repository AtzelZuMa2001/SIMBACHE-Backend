package com.korealm.simbache.repositories;

import com.korealm.simbache.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLocationId(Long locationId);

    List<Location> findAllByPostalCode(int postalCode);

    List<Location> findAllByState(State state);
    List<Location> findAllByState_StateId(short stateId);

    List<Location> findAllByMunicipality(Municipality municipality);
    List<Location> findAllByMunicipality_MunicipalityId(short municipalityId);

    List<Location> findAllByLocality(Locality locality);
    List<Location> findAllByLocality_LocalityId(int localityId);

    List<Location> findAllByColonia(Colonia colonia);
    List<Location> findAllByColonia_ColoniaId(Long coloniaId);

    List<Location> findAllByStreet(Street street);
    List<Location> findAllByStreet_StreetId(Long streetId);
}
