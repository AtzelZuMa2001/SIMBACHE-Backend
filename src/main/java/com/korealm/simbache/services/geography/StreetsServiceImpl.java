package com.korealm.simbache.services.geography;

import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.Street;
import com.korealm.simbache.repositories.StreetRepository;
import com.korealm.simbache.services.VerificationService;
import com.korealm.simbache.services.interfaces.AuditLoggingService;
import com.korealm.simbache.services.interfaces.StreetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StreetsServiceImpl implements StreetsService {
    private final StreetRepository streetRepository;
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
}
