package com.korealm.simbache.repositories;

import com.korealm.simbache.models.Municipality;
import com.korealm.simbache.models.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MunicipalityRepository extends JpaRepository<Municipality, Short> {
    Optional<Municipality> findByMunicipalityId(short municipalityId);
    Optional<Municipality> findByMunicipalityName(String municipalityName);

    List<Municipality> findAllByState(State state);
    List<Municipality> findAllByState_StateId(short stateId);
    List<Municipality> findAllByMunicipalityNameContainingIgnoreCase(String name);
}
