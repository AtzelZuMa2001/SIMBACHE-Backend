package com.korealm.simbache.repositories;

import com.korealm.simbache.models.SquadWorker;
import com.korealm.simbache.models.SquadWorkerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SquadWorkerRepository extends JpaRepository<SquadWorker, SquadWorkerId> {
}
