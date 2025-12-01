package com.korealm.simbache.repositories;

import com.korealm.simbache.models.Repair;
import com.korealm.simbache.models.Pothole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {
    Optional<Repair> findByPothole(Pothole pothole);
}
