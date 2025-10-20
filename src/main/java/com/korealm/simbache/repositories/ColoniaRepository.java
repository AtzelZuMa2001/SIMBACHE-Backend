package com.korealm.simbache.repositories;

import com.korealm.simbache.models.Colonia;
import com.korealm.simbache.models.Locality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColoniaRepository extends JpaRepository<Colonia, Long> {
    Optional<Colonia> findByColoniaId(Long coloniaId);
    Optional<Colonia> findByColoniaName(String coloniaName);

    List<Colonia> findAllByLocality(Locality locality);
    List<Colonia> findAllByLocality_LocalityId(int localityId);
    List<Colonia> findAllByColoniaNameContainingIgnoreCase(String name);
}
