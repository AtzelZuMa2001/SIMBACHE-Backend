package com.korealm.simbache.services.geography;

import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.Colonia;
import com.korealm.simbache.repositories.ColoniaRepository;
import com.korealm.simbache.services.VerificationService;
import com.korealm.simbache.services.interfaces.AuditLoggingService;
import com.korealm.simbache.services.interfaces.ColoniasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColoniasServiceImpl implements ColoniasService {
    private final ColoniaRepository coloniaRepository;
    private final VerificationService verificationService;
    private final AuditLoggingService auditLoggingService;

    @Override
    public Map<Long, String> getColoniasByLocality(String token, int localityId) {
        if (verificationService.isUserUnauthorized(token))
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var colonias = coloniaRepository.findAllByLocality_LocalityId(localityId);

        // Audit log: colonias by locality
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_COLONIAS", "El usuario " + u.getUsername() +
                        " consultó la lista de colonias para la localidad " + localityId +
                        " (" + colonias.size() + ") elementos).")
        );

        return colonias
                .stream()
                .collect(Collectors.toMap(Colonia::getColoniaId, Colonia::getColoniaName));
    }
}
