package com.korealm.simbache.services;

import com.korealm.simbache.dtos.vehicles.VehicleTypeUpdateDto;
import com.korealm.simbache.exceptions.InvalidInsertException;
import com.korealm.simbache.exceptions.InvalidUpdateException;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.VehicleType;
import com.korealm.simbache.repositories.SessionTokenRepository;
import com.korealm.simbache.repositories.VehicleTypeRepository;
import com.korealm.simbache.services.interfaces.VehicleTypeService;
import com.korealm.simbache.services.VerificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleTypeServiceServiceImpl implements VehicleTypeService {
    private final VehicleTypeRepository vehicleTypeRepository;
    private final SessionTokenRepository sessionTokenRepository;
    private final VerificationService verificationService;

    @Override
    public List<String> getAllVehicleTypes(String token) {
        if (! verificationService.isUserAuthorized(token) ) throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var result = vehicleTypeRepository.findAll();

        return result.stream().map(VehicleType::getTypeName).toList();
    }

    @Override
    public short addVehicleType(String token, String newType) {
        if (! verificationService.isUserAuthorized(token) ) throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        if (vehicleTypeRepository.findByTypeName(newType).isPresent()) throw new InvalidInsertException("The vehicle type already exists. Unable to insert it.");

        var vehicleType = VehicleType.builder()
                .typeName(newType)
                .build();

        var saved = vehicleTypeRepository.save(vehicleType);

        return saved.getTypeId();
    }

    @Override
    public void updateVehicleType(String token, VehicleTypeUpdateDto updateDto) {
        if (! verificationService.isUserAuthorized(token) ) throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var vehicle = vehicleTypeRepository
                .findByTypeName(updateDto.getCurrentName())
                .orElseThrow(() -> new InvalidUpdateException("The vehicle type does not exist. Unable to update it."));

        vehicle.setTypeName(updateDto.getNewName());
        vehicleTypeRepository.save(vehicle);
    }

    @Override
    @Transactional
    public void deleteVehicleType(String token, String typeName) {
        if (! verificationService.isUserAuthorized(token) ) throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var vehicle = vehicleTypeRepository.findByTypeName(typeName)
                .orElseThrow(() -> new InvalidUpdateException("The vehicle type does not exist. Unable to delete it."));

        vehicleTypeRepository.deleteByTypeId(vehicle.getTypeId());
    }
}
