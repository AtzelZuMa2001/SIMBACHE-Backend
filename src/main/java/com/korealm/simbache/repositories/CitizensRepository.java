package com.korealm.simbache.repositories;

import com.korealm.simbache.models.Citizens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CitizensRepository extends JpaRepository<Citizens, Long> {
    Optional<Citizens> findByCitizenId(Long citizenId);
    Optional<Citizens> findByEmail(String email);
    Optional<Citizens> findByPhoneNumber(Long phoneNumber);
}
