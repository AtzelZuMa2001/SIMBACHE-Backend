package com.korealm.simbache.services;

import com.korealm.simbache.dtos.citizens.CitizenCreateDto;
import com.korealm.simbache.dtos.citizens.CitizenLookupDto;
import com.korealm.simbache.dtos.citizens.CitizenResponseDto;
import com.korealm.simbache.dtos.citizens.CitizenUpdateDto;
import com.korealm.simbache.exceptions.InvalidInsertException;
import com.korealm.simbache.exceptions.InvalidUpdateException;
import com.korealm.simbache.exceptions.ResourceNotFoundException;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.Citizens;
import com.korealm.simbache.repositories.CitizensRepository;
import com.korealm.simbache.repositories.LocationRepository;
import com.korealm.simbache.services.interfaces.AuditLoggingService;
import com.korealm.simbache.services.interfaces.CitizensService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CitizensServiceImpl implements CitizensService {

    private final CitizensRepository citizensRepository;
    private final LocationRepository locationRepository;
    private final VerificationService verificationService;
    private final AuditLoggingService auditLoggingService;

    @Override
    public CitizenLookupDto lookupByPhoneNumber(String token, Long phoneNumber) {
        if (verificationService.isUserUnauthorized(token)) {
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");
        }

        var citizen = citizensRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró ningún ciudadano con el número de teléfono: " + phoneNumber));

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_CIUDADANO_TELEFONO",
                        "El usuario " + u.getUsername() + " buscó un ciudadano por teléfono: " + phoneNumber + ".")
        );

        return new CitizenLookupDto(citizen);
    }

    @Override
    public CitizenResponseDto getById(String token, Long citizenId) {
        if (verificationService.isUserUnauthorized(token)) {
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");
        }

        var citizen = citizensRepository.findByCitizenId(citizenId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el ciudadano con id: " + citizenId));

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_CIUDADANO",
                        "El usuario " + u.getUsername() + " consultó el ciudadano id " + citizenId + ".")
        );

        return new CitizenResponseDto(citizen);
    }

    @Override
    public Long add(String token, CitizenCreateDto dto) {
        if (verificationService.isUserUnauthorized(token)) {
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");
        }

        // Check for duplicate email
        if (citizensRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new InvalidInsertException("Ya existe un ciudadano registrado con el correo: " + dto.getEmail());
        }

        // Check for a duplicate phone number (if provided)
        if (dto.getPhoneNumber() != null && citizensRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
            throw new InvalidInsertException("Ya existe un ciudadano registrado con el número de teléfono: " + dto.getPhoneNumber());
        }

        var location = locationRepository.findByLocationId(dto.getRegisteredLocationId())
                .orElseThrow(() -> new InvalidInsertException("La ubicación especificada no existe."));

        var citizen = Citizens.builder()
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .secondLastName(dto.getSecondLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .registeredLocation(location)
                .build();

        var saved = citizensRepository.save(citizen);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CREACION_CIUDADANO",
                        "El usuario " + u.getUsername() + " registró al ciudadano '" + dto.getFirstName() + " " + dto.getLastName() + "' con id " + saved.getCitizenId() + ".")
        );

        return saved.getCitizenId();
    }

    @Override
    public void update(String token, Long citizenId, CitizenUpdateDto dto) {
        if (verificationService.isUserUnauthorized(token)) {
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");
        }

        var citizen = citizensRepository.findByCitizenId(citizenId)
                .orElseThrow(() -> new InvalidUpdateException("El ciudadano con id " + citizenId + " no existe. No es posible actualizarlo."));

        // Validate email uniqueness if changing
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            var existingByEmail = citizensRepository.findByEmail(dto.getEmail());
            if (existingByEmail.isPresent() && existingByEmail.get().getCitizenId() != citizenId) {
                throw new InvalidUpdateException("Ya existe otro ciudadano con el correo: " + dto.getEmail());
            }
            citizen.setEmail(dto.getEmail());
        }

        // Validate phone uniqueness if changing
        if (dto.getPhoneNumber() != null) {
            var existingByPhone = citizensRepository.findByPhoneNumber(dto.getPhoneNumber());
            if (existingByPhone.isPresent() && existingByPhone.get().getCitizenId() != citizenId) {
                throw new InvalidUpdateException("Ya existe otro ciudadano con el número de teléfono: " + dto.getPhoneNumber());
            }
            citizen.setPhoneNumber(dto.getPhoneNumber());
        }

        if (dto.getFirstName() != null && !dto.getFirstName().isBlank()) {
            citizen.setFirstName(dto.getFirstName());
        }

        if (dto.getMiddleName() != null) {
            citizen.setMiddleName(dto.getMiddleName());
        }

        if (dto.getLastName() != null && !dto.getLastName().isBlank()) {
            citizen.setLastName(dto.getLastName());
        }

        if (dto.getSecondLastName() != null) {
            citizen.setSecondLastName(dto.getSecondLastName());
        }

        if (dto.getRegisteredLocationId() != null) {
            var location = locationRepository.findByLocationId(dto.getRegisteredLocationId())
                    .orElseThrow(() -> new InvalidUpdateException("La ubicación especificada no existe."));
            citizen.setRegisteredLocation(location);
        }

        citizensRepository.save(citizen);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ACTUALIZACION_CIUDADANO",
                        "El usuario " + u.getUsername() + " actualizó al ciudadano id " + citizenId + ".")
        );
    }
}