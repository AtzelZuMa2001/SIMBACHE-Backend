package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.vehicles.VehicleTypeUpdateDto;

import java.util.List;

public interface VehicleTypeService {
    List<String> getAllVehicleTypes(String token);

    short addVehicleType(String token, String newType);

    void updateVehicleType(String token, VehicleTypeUpdateDto vehicleTypeUpdateDto);

    void deleteVehicleType(String token, String typeName);
}
