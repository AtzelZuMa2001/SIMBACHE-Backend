package com.korealm.simbache.repositories;

import com.korealm.simbache.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PotholeRepository extends JpaRepository<Pothole, Long> {
    Optional<Pothole> findByPotholeId(Long potholeId);

    List<Pothole> findAllByRegisteredBy(User user);
    List<Pothole> findAllByReportByCitizen(Citizens citizen);
    List<Pothole> findAllByLocation(Location location);
    List<Pothole> findAllByCategory(PotholeCategory category);
    List<Pothole> findAllByStatus(PotholeStatus status);

    List<Pothole> findAllByIsActiveTrue();
    Page<Pothole> findAllByIsActiveTrue(Pageable pageable);
    List<Pothole> findAllByDateReportedBetween(LocalDateTime start, LocalDateTime end);
}
