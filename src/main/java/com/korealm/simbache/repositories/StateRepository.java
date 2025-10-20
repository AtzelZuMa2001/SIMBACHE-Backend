package com.korealm.simbache.repositories;

import com.korealm.simbache.models.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Short> {
    Optional<State> findByStateId(short stateId);
    Optional<State> findByStateName(String stateName);
    List<State> findAllByStateNameContainingIgnoreCase(String stateName);
}
