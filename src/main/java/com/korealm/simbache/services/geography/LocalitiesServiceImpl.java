package com.korealm.simbache.services.geography;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.LocalityCreateDto;
import com.korealm.simbache.exceptions.InvalidInsertException;
import com.korealm.simbache.exceptions.InvalidUpdateException;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.Locality;
import com.korealm.simbache.repositories.LocalityRepository;
import com.korealm.simbache.repositories.MunicipalityRepository;
import com.korealm.simbache.services.VerificationService;
import com.korealm.simbache.services.interfaces.AuditLoggingService;
import com.korealm.simbache.services.interfaces.LocalitiesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalitiesServiceImpl implements LocalitiesService {
    private final LocalityRepository localityRepository;
    private final MunicipalityRepository municipalityRepository;
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

    @Override
    public int addLocality(String token, LocalityCreateDto dto) {
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var municipality = municipalityRepository.findByMunicipalityId(dto.getMunicipalityId())
                .orElseThrow(() -> new InvalidInsertException("El municipio especificado no existe. No es posible crear la localidad."));

        if (localityRepository.findByLocalityName(dto.getLocalityName()).isPresent())
            throw new InvalidInsertException("La localidad ya existe. No es posible crearla de nuevo.");

        var locality = Locality.builder()
                .localityName(dto.getLocalityName())
                .type(dto.getType())
                .municipality(municipality)
                .build();

        var saved = localityRepository.save(locality);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CREACION_LOCALIDAD", "El usuario " + u.getUsername() +
                        " creó la localidad '" + dto.getLocalityName() + "' con id " + saved.getLocalityId() +
                        " para el municipio " + municipality.getMunicipalityId() + ".")
        );

        return saved.getLocalityId();
    }

    @Override
    public void updateLocality(String token, BasicUpdateDto dto) {
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var locality = localityRepository.findByLocalityName(dto.getCurrentName())
                .orElseThrow(() -> new InvalidUpdateException("La localidad no existe. No es posible actualizarla."));

        locality.setLocalityName(dto.getNewName());
        localityRepository.save(locality);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ACTUALIZACION_LOCALIDAD", "El usuario " + u.getUsername() +
                        " actualizó la localidad de '" + dto.getCurrentName() + "' a '" + dto.getNewName() + "'.")
        );
    }

    @Override
    @Transactional
    public void deleteLocality(String token, int localityId) {
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var locality = localityRepository.findByLocalityId(localityId)
                .orElseThrow(() -> new InvalidUpdateException("La localidad no existe. No es posible borrarla."));

        localityRepository.deleteById(localityId);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ELIMINACION_LOCALIDAD", "El usuario " + u.getUsername() +
                        " eliminó la localidad '" + locality.getLocalityName() + "' (id " + localityId + ").")
        );
    }
}
