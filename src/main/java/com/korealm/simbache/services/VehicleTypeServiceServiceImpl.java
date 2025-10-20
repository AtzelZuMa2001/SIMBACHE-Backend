package com.korealm.simbache.services;

import com.korealm.simbache.dtos.vehicles.VehicleTypeUpdateDto;
import com.korealm.simbache.exceptions.InvalidInsertException;
import com.korealm.simbache.exceptions.InvalidUpdateException;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.VehicleType;
import com.korealm.simbache.repositories.VehicleTypeRepository;
import com.korealm.simbache.services.interfaces.VehicleTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleTypeServiceServiceImpl implements VehicleTypeService {
    private final VehicleTypeRepository vehicleTypeRepository;
    private final VerificationService verificationService;
    private final AuditLoggingServiceImpl auditLoggingService;

    @Override
    public List<String> getAllVehicleTypes(String token) {
        if (verificationService.isUserUnauthorized(token)) throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var result = vehicleTypeRepository.findAll();

        // Log de auditoría: consulta de tipos de vehículo
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_TIPOS_VEHICULO", "El usuario " + u.getUsername() + " consultó la lista de tipos de vehículo (" + result.size() + ") elementos).")
        );

        return result.stream().map(VehicleType::getTypeName).toList();
    }

    @Override
    public short addVehicleType(String token, String newType) {
        if (! verificationService.isUserAdmin(token)) throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        if (vehicleTypeRepository.findByTypeName(newType).isPresent()) throw new InvalidInsertException("El tipo de vehículo ya existe. No es posible crearlo de nuevo..");

        var vehicleType = VehicleType.builder()
                .typeName(newType)
                .build();

        var saved = vehicleTypeRepository.save(vehicleType);

        // Log de auditoría: creación
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CREACION_TIPO_VEHICULO", "El usuario " + u.getUsername() + " creó el tipo de vehículo '" + newType + "' con id " + saved.getTypeId() + ".")
        );

        return saved.getTypeId();
    }

    @Override
    public void updateVehicleType(String token, VehicleTypeUpdateDto updateDto) {
        if (! verificationService.isUserAdmin(token)) throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var vehicle = vehicleTypeRepository
                .findByTypeName(updateDto.getCurrentName())
                .orElseThrow(() -> new InvalidUpdateException("El tipo de vehículo no existe. No es posible actualizarlo."));

        vehicle.setTypeName(updateDto.getNewName());
        vehicleTypeRepository.save(vehicle);

        // Log de auditoría: actualización
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ACTUALIZACION_TIPO_VEHICULO", "El usuario " + u.getUsername() + " actualizó el tipo de vehículo de '" + updateDto.getCurrentName() + "' a '" + updateDto.getNewName() + "'.")
        );
    }

    @Override
    @Transactional
    public void deleteVehicleType(String token, String typeName) {
        if (! verificationService.isUserAdmin(token)) throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var vehicle = vehicleTypeRepository.findByTypeName(typeName)
                .orElseThrow(() -> new InvalidUpdateException("El tipo de vehículo no existe. No es posible borrarlo."));

        vehicleTypeRepository.deleteByTypeId(vehicle.getTypeId());

        // Log de auditoría: eliminación
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ELIMINACION_TIPO_VEHICULO", "El usuario " + u.getUsername() + " eliminó el tipo de vehículo '" + typeName + "' (id " + vehicle.getTypeId() + ").")
        );
    }
}
