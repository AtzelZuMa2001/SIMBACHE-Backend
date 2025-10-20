package com.korealm.simbache.repositories;

import com.korealm.simbache.models.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractorRepository extends JpaRepository<Contractor, Integer> {
}
