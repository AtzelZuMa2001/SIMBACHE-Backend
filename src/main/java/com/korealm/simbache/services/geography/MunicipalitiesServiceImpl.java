package com.korealm.simbache.services.geography;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.MunicipalityCreateDto;
import com.korealm.simbache.exceptions.InvalidInsertException;
import com.korealm.simbache.exceptions.InvalidUpdateException;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.Municipality;
import com.korealm.simbache.repositories.MunicipalityRepository;
import com.korealm.simbache.repositories.StateRepository;
import com.korealm.simbache.services.VerificationService;
import com.korealm.simbache.services.interfaces.AuditLoggingService;
import com.korealm.simbache.services.interfaces.MunicipalitiesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MunicipalitiesServiceImpl implements MunicipalitiesService {
    private final MunicipalityRepository municipalityRepository;
    private final StateRepository stateRepository;
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

    @Override
    public short addMunicipality(String token, MunicipalityCreateDto dto) {
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var state = stateRepository.findByStateId(dto.getStateId())
                .orElseThrow(() -> new InvalidInsertException("El estado especificado no existe. No es posible crear el municipio."));

        if (municipalityRepository.findByMunicipalityName(dto.getMunicipalityName()).isPresent())
            throw new InvalidInsertException("El municipio ya existe. No es posible crearlo de nuevo.");

        var municipality = Municipality.builder()
                .municipalityName(dto.getMunicipalityName())
                .state(state)
                .build();

        var saved = municipalityRepository.save(municipality);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CREACION_MUNICIPIO", "El usuario " + u.getUsername() +
                        " creó el municipio '" + dto.getMunicipalityName() + "' con id " + saved.getMunicipalityId() +
                        " para el estado " + state.getStateId() + ".")
        );

        return saved.getMunicipalityId();
    }

    @Override
    public void updateMunicipality(String token, BasicUpdateDto dto) {
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var municipality = municipalityRepository.findByMunicipalityName(dto.getCurrentName())
                .orElseThrow(() -> new InvalidUpdateException("El municipio no existe. No es posible actualizarlo."));

        municipality.setMunicipalityName(dto.getNewName());
        municipalityRepository.save(municipality);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ACTUALIZACION_MUNICIPIO", "El usuario " + u.getUsername() +
                        " actualizó el municipio de '" + dto.getCurrentName() + "' a '" + dto.getNewName() + "'.")
        );
    }

    @Override
    @Transactional
    public void deleteMunicipality(String token, short municipalityId) {
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var municipality = municipalityRepository.findByMunicipalityId(municipalityId)
                .orElseThrow(() -> new InvalidUpdateException("El municipio no existe. No es posible borrarlo."));

        municipalityRepository.deleteById(municipalityId);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ELIMINACION_MUNICIPIO", "El usuario " + u.getUsername() +
                        " eliminó el municipio '" + municipality.getMunicipalityName() + "' (id " + municipalityId + ").")
        );
    }
}
