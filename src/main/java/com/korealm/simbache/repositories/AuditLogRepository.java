package com.korealm.simbache.repositories;

import com.korealm.simbache.models.AuditLog;
import com.korealm.simbache.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    // By primary key (I might never need this, but I'll keep it tho)
    Optional<AuditLog> findByLogId(Long logId);

    // By user
    List<AuditLog> findAllByUserOrderByLogIdDesc(User user);

    // Arranged by date (using LogId order as insertion order proxy)
    List<AuditLog> findAllByOrderByLogIdDesc();
    Page<AuditLog> findAllByOrderByLogIdDesc(Pageable pageable);

    // By UserRole arranged by date
    List<AuditLog> findAllByUser_UserRole_RoleIdOrderByLogIdDesc(short roleId);

    // By specific action string (Same. I might never need this)
    List<AuditLog> findAllByActionOrderByLogIdDesc(String action);
}
