package com.korealm.simbache.services.geography;

import com.korealm.simbache.dtos.geography.ZipCodeLookupDto;
import com.korealm.simbache.exceptions.ResourceNotFoundException;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.Locality;
import com.korealm.simbache.repositories.LocalityRepository;
import com.korealm.simbache.services.VerificationService;
import com.korealm.simbache.services.interfaces.AuditLoggingService;
import com.korealm.simbache.services.interfaces.ZipCodeLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZipCodeLookupServiceImpl implements ZipCodeLookupService {

    private final LocalityRepository localityRepository;
    private final VerificationService verificationService;
    private final AuditLoggingService auditLoggingService;

    @Override
    public List<ZipCodeLookupDto> getLocationsByZipCode(String token, int postalCode) {
        if (verificationService.isUserUnauthorized(token)) {
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");
        }

        List<Locality> localities = localityRepository.findAllByPostalCode(postalCode);

        if (localities.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron localidades con el código postal: " + postalCode);
        }

        // Audit log
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_CODIGO_POSTAL",
                        "El usuario " + u.getUsername() + " consultó las ubicaciones para el código postal " + postalCode +
                                " (" + localities.size() + " resultados).")
        );

        return localities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ZipCodeLookupDto getZipCodeByLocation(String token, short stateId, short municipalityId, int localityId) {
        if (verificationService.isUserUnauthorized(token)) {
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");
        }

        Locality locality = localityRepository.findByLocalityId(localityId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la localidad con id: " + localityId));

        // Validate that the locality belongs to the specified municipality and state
        if (locality.getMunicipality().getMunicipalityId() != municipalityId) {
            throw new ResourceNotFoundException("La localidad especificada no pertenece al municipio indicado.");
        }

        if (locality.getMunicipality().getState().getStateId() != stateId) {
            throw new ResourceNotFoundException("La localidad especificada no pertenece al estado indicado.");
        }

        // Audit log
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_UBICACION",
                        "El usuario " + u.getUsername() + " consultó el código postal para stateId=" + stateId +
                                ", municipalityId=" + municipalityId + ", localityId=" + localityId + ".")
        );

        return mapToDto(locality);
    }

    private ZipCodeLookupDto mapToDto(Locality locality) {
        return ZipCodeLookupDto.builder()
                .stateId(locality.getMunicipality().getState().getStateId())
                .municipalityId(locality.getMunicipality().getMunicipalityId())
                .localityId(locality.getLocalityId())
                .postalCode(locality.getPostalCode())
                .stateName(locality.getMunicipality().getState().getStateName())
                .municipalityName(locality.getMunicipality().getMunicipalityName())
                .localityName(locality.getLocalityName())
                .build();
    }
}