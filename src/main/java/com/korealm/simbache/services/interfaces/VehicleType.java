package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.vehicles.VehicleTypeDto;

public interface VehicleType {
    VehicleTypeDto[] getAllVehicleTypes();

    VehicleTypeDto getVehicleTypeById(short typeId);

    short addVehicleType(VehicleTypeDto vehicleTypeDto);

    void updateVehicleType(VehicleTypeDto vehicleTypeDto);

    void deleteVehicleType(short typeId);
}
