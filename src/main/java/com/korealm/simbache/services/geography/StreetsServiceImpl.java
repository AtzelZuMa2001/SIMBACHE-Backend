package com.korealm.simbache.services.geography;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.StreetCreateDto;
import com.korealm.simbache.exceptions.InvalidInsertException;
import com.korealm.simbache.exceptions.InvalidUpdateException;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.Street;
import com.korealm.simbache.repositories.ColoniaRepository;
import com.korealm.simbache.repositories.LocalityRepository;
import com.korealm.simbache.repositories.StreetRepository;
import com.korealm.simbache.services.VerificationService;
import com.korealm.simbache.services.interfaces.AuditLoggingService;
import com.korealm.simbache.services.interfaces.StreetsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StreetsServiceImpl implements StreetsService {
    private final StreetRepository streetRepository;
    private final LocalityRepository localityRepository;
    private final ColoniaRepository coloniaRepository;
    private final VerificationService verificationService;
    private final AuditLoggingService auditLoggingService;

    @Override
    public Map<Long, String> getStreetsByColonia(String token, Long coloniaId) {
        if (verificationService.isUserUnauthorized(token))
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var streets = streetRepository.findAllByColonia_ColoniaId(coloniaId);

        // Audit log: streets by colonia
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_CALLES_COLONIA", "El usuario " + u.getUsername() +
                        " consultó la lista de calles para la colonia " + coloniaId +
                        " (" + streets.size() + ") elementos).")
        );

        return streets
                .stream()
                .collect(Collectors.toMap(Street::getStreetId, Street::getStreetName));
    }

    @Override
    public Map<Long, String> getStreetsByLocality(String token, int localityId) {
        if (verificationService.isUserUnauthorized(token))
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var streets = streetRepository.findAllByLocality_LocalityId(localityId);

        // Audit log: streets by locality
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_CALLES_LOCALIDAD", "El usuario " + u.getUsername() +
                        " consultó la lista de calles para la localidad " + localityId +
                        " (" + streets.size() + ") elementos).")
        );

        return streets
                .stream()
                .collect(Collectors.toMap(Street::getStreetId, Street::getStreetName));
    }

    @Override
    public long addStreet(String token, StreetCreateDto dto) {
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var locality = localityRepository.findByLocalityId(dto.getLocalityId())
                .orElseThrow(() -> new InvalidInsertException("La localidad especificada no existe. No es posible crear la calle."));

        if (streetRepository.findByStreetNameAndLocality(dto.getStreetName(), locality).isPresent()) {
            throw new InvalidInsertException("La calle con ese ID ya existe. No es posible crearla de nuevo.");
        }

        var colonia = dto.getColoniaId() == null ? null : coloniaRepository.findByColoniaId(dto.getColoniaId())
                .orElseThrow(() -> new InvalidInsertException("La colonia especificada no existe."));

        if (colonia != null && colonia.getLocality().getLocalityId() != locality.getLocalityId())
            throw new InvalidInsertException("La colonia especificada no pertenece a la misma localidad.");

        // optional duplicate name per-locality check
        if (streetRepository.findByStreetNameAndLocality(dto.getStreetName(), locality).isPresent())
            throw new InvalidInsertException("Ya existe una calle con ese nombre en la localidad especificada.");

        var street = Street.builder()
                .streetName(dto.getStreetName())
                .locality(locality)
                .colonia(colonia)
                .build();

        var saved = streetRepository.save(street);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CREACION_CALLE", "El usuario " + u.getUsername() +
                        " creó la calle '" + dto.getStreetName() + "' con id " + saved.getStreetId() +
                        " para la localidad " + locality.getLocalityId() +
                        (colonia != null ? (", colonia " + colonia.getColoniaId()) : "") + ".")
        );

        return saved.getStreetId();
    }

    @Override
    public void updateStreet(String token, BasicUpdateDto dto) {
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        // We update by name; if multiple localities contain the same name, this simplistic approach may fail.
        var existing = streetRepository.findAllByStreetNameContainingIgnoreCase(dto.getCurrentName());
        var street = existing.stream()
                .filter(s -> s.getStreetName().equals(dto.getCurrentName()))
                .findFirst()
                .orElseThrow(() -> new InvalidUpdateException("La calle no existe. No es posible actualizarla."));

        street.setStreetName(dto.getNewName());
        streetRepository.save(street);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ACTUALIZACION_CALLE", "El usuario " + u.getUsername() +
                        " actualizó la calle de '" + dto.getCurrentName() + "' a '" + dto.getNewName() + "'.")
        );
    }

    @Override
    @Transactional
    public void deleteStreet(String token, long streetId) {
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var street = streetRepository.findByStreetId(streetId)
                .orElseThrow(() -> new InvalidUpdateException("La calle no existe. No es posible borrarla."));

        streetRepository.deleteById(streetId);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ELIMINACION_CALLE", "El usuario " + u.getUsername() +
                        " eliminó la calle '" + street.getStreetName() + "' (id " + streetId + ").")
        );
    }
}
