package com.korealm.simbache.repositories;

import com.korealm.simbache.models.PotholeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PotholeCategoryRepository extends JpaRepository<PotholeCategory, Short> {
    Optional<PotholeCategory> findByCategoryId(Short categoryId);
    Optional<PotholeCategory> findByName(String name);
    List<PotholeCategory> findAllByPriorityLevel(int priorityLevel);
}
