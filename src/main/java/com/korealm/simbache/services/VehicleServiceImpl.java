package com.korealm.simbache.services;

import com.korealm.simbache.dtos.vehicles.VehicleTypeDto;
import com.korealm.simbache.repositories.VehicleTypeRepository;
import com.korealm.simbache.services.interfaces.VehicleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleType {
    private final VehicleTypeRepository vehicleTypeRepository;

    @Override
    public VehicleTypeDto[] getAllVehicleTypes() {
        return null;
    }

    @Override
    public VehicleTypeDto getVehicleTypeById(short typeId) {
        return null;
    }

    @Override
    public short addVehicleType(VehicleTypeDto vehicleTypeDto) {
        return 0;
    }

    @Override
    public void updateVehicleType(VehicleTypeDto vehicleTypeDto) {

    }

    @Override
    public void deleteVehicleType(short typeId) {

    }
}
