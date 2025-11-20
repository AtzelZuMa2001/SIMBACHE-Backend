package com.korealm.simbache.services.geography;

import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.Locality;
import com.korealm.simbache.repositories.LocalityRepository;
import com.korealm.simbache.services.VerificationService;
import com.korealm.simbache.services.interfaces.AuditLoggingService;
import com.korealm.simbache.services.interfaces.LocalitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalitiesServiceImpl implements LocalitiesService {
    private final LocalityRepository localityRepository;
    private final VerificationService verificationService;
    private final AuditLoggingService auditLoggingService;

    @Override
    public Map<Integer, String> getLocalitiesByMunicipality(String token, short municipalityId) {
        if (verificationService.isUserUnauthorized(token))
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var localities = localityRepository.findAllByMunicipality_MunicipalityId(municipalityId);

        // Audit log: localities query for a municipality
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_LOCALIDADES", "El usuario " + u.getUsername() +
                        " consultó la lista de localidades para el municipio " + municipalityId +
                        " (" + localities.size() + ") elementos).")
        );

        return localities
                .stream()
                .collect(Collectors.toMap(Locality::getLocalityId, Locality::getLocalityName));
    }
}
