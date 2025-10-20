package com.korealm.simbache.repositories;

import com.korealm.simbache.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportationRepository extends JpaRepository<Transportation, Long> {
    Optional<Transportation> findByTransportationId(Long transportationId);
    Optional<Transportation> findByPlateNumber(String plateNumber);

    List<Transportation> findAllByVehicleType(VehicleType vehicleType);
    List<Transportation> findAllByVehicleType_TypeId(short typeId);

    List<Transportation> findAllByVehicleStatus(VehicleStatus vehicleStatus);
    List<Transportation> findAllByVehicleStatus_StatusId(short statusId);
}
