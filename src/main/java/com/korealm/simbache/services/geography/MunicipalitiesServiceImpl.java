package com.korealm.simbache.services.geography;

import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.Municipality;
import com.korealm.simbache.repositories.MunicipalityRepository;
import com.korealm.simbache.services.VerificationService;
import com.korealm.simbache.services.interfaces.AuditLoggingService;
import com.korealm.simbache.services.interfaces.MunicipalitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MunicipalitiesServiceImpl implements MunicipalitiesService {
    private final MunicipalityRepository municipalityRepository;
    private final VerificationService verificationService;
    private final AuditLoggingService auditLoggingService;

    @Override
    public Map<Short, String> getMunicipalitiesByState(String token, short stateId) {
        if (verificationService.isUserUnauthorized(token))
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var municipalities = municipalityRepository.findAllByState_StateId(stateId);

        // Audit log: municipalities query for a state
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_MUNICIPIOS", "El usuario " + u.getUsername() +
                        " consultó la lista de municipios para el estado " + stateId +
                        " (" + municipalities.size() + ") elementos).")
        );

        return municipalities
                .stream()
                .collect(Collectors.toMap(Municipality::getMunicipalityId, Municipality::getMunicipalityName));
    }
}
