package com.korealm.simbache.services.geography;

import com.korealm.simbache.dtos.geography.LocationCreateDto;
import com.korealm.simbache.dtos.geography.LocationDto;
import com.korealm.simbache.dtos.geography.LocationUpdateDto;
import com.korealm.simbache.exceptions.InvalidInsertException;
import com.korealm.simbache.exceptions.InvalidUpdateException;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.Location;
import com.korealm.simbache.repositories.*;
import com.korealm.simbache.services.VerificationService;
import com.korealm.simbache.services.interfaces.AuditLoggingService;
import com.korealm.simbache.services.interfaces.LocationsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationsServiceImpl implements LocationsService {
    private final LocationRepository locationRepository;
    private final StateRepository stateRepository;
    private final MunicipalityRepository municipalityRepository;
    private final LocalityRepository localityRepository;
    private final StreetRepository streetRepository;
    private final VerificationService verificationService;
    private final AuditLoggingService auditLoggingService;

    @Override
    public List<LocationDto> list(String token,
                                  Short stateId,
                                  Short municipalityId,
                                  Integer localityId,
                                  Long coloniaId,
                                  Long mainStreetId) {
        if (verificationService.isUserUnauthorized(token))
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        List<Location> items;

        // Use the most specific repository filter available, then post-filter if needed
        if (stateId != null) {
            items = locationRepository.findAllByState_StateId(stateId);
        } else if (municipalityId != null) {
            items = locationRepository.findAllByMunicipality_MunicipalityId(municipalityId);
        } else if (localityId != null) {
            items = locationRepository.findAllByLocality_LocalityId(localityId);
        } else if (mainStreetId != null) {
            items = locationRepository.findAllByMainStreet_StreetId(mainStreetId);
        } else {
            items = locationRepository.findAll();
        }

        // Additional in-memory filtering to honor combinations if more than one filter provided
        var filtered = items.stream()
                .filter(l -> stateId == null || l.getState().getStateId() == stateId)
                .filter(l -> municipalityId == null || l.getMunicipality().getMunicipalityId() == municipalityId)
                .filter(l -> localityId == null || l.getLocality().getLocalityId() == localityId)
                .filter(l -> mainStreetId == null || l.getMainStreet().getStreetId().equals(mainStreetId))
                .map(LocationDto::new)
                .collect(Collectors.toList());

        // Audit log
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_UBICACIONES", "El usuario " + u.getUsername() +
                        " consultó la lista de ubicaciones (" + filtered.size() + ") elementos).")
        );

        return filtered;
    }

    @Override
    public LocationDto getById(String token, long locationId) {
        if (verificationService.isUserUnauthorized(token))
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var location = locationRepository.findByLocationId(locationId)
                .orElseThrow(() -> new InvalidUpdateException("La ubicación no existe."));

        return new LocationDto(location);
    }

    @Override
    public long add(String token, LocationCreateDto dto) {
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var state = stateRepository.findByStateId(dto.getStateId())
                .orElseThrow(() -> new InvalidInsertException("El estado especificado no existe."));
        var municipality = municipalityRepository.findByMunicipalityId(dto.getMunicipalityId())
                .orElseThrow(() -> new InvalidInsertException("El municipio especificado no existe."));
        var locality = localityRepository.findByLocalityId(dto.getLocalityId())
                .orElseThrow(() -> new InvalidInsertException("La localidad especificada no existe."));

        // Optional references
        var mainStreet = streetRepository.findByStreetId(dto.getMainStreetId())
                .orElseThrow(() -> new InvalidInsertException("La calle principal especificada no existe."));

        var streetOne = dto.getStreetOneId() == null ? null : streetRepository.findByStreetId(dto.getStreetOneId())
                .orElseThrow(() -> new InvalidInsertException("La calle uno especificada no existe."));

        var streetTwo = dto.getStreetTwoId() == null ? null : streetRepository.findByStreetId(dto.getStreetTwoId())
                .orElseThrow(() -> new InvalidInsertException("La calle dos especificada no existe."));

        var location = Location.builder()
                .state(state)
                .municipality(municipality)
                .locality(locality)
//                .postalCode(dto.getPostalCode())
                .mainStreet(mainStreet)
                .streetOne(streetOne)
                .streetTwo(streetTwo)
                .build();

        var saved = locationRepository.save(location);

        // Audit log: creation
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CREACION_UBICACION", "El usuario " + u.getUsername() +
                        " creó la ubicación con id " + saved.getLocationId() + ".")
        );

        return saved.getLocationId();
    }

    @Override
    public void update(String token, LocationUpdateDto dto) {
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var location = locationRepository.findByLocationId(dto.getLocationId())
                .orElseThrow(() -> new InvalidUpdateException("La ubicación no existe. No es posible actualizarla."));

        if (dto.getStateId() != null) {
            var state = stateRepository.findByStateId(dto.getStateId())
                    .orElseThrow(() -> new InvalidUpdateException("El estado especificado no existe."));
            location.setState(state);
        }

        if (dto.getMunicipalityId() != null) {
            var municipality = municipalityRepository.findByMunicipalityId(dto.getMunicipalityId())
                    .orElseThrow(() -> new InvalidUpdateException("El municipio especificado no existe."));
            location.setMunicipality(municipality);
        }

        if (dto.getLocalityId() != null) {
            var locality = localityRepository.findByLocalityId(dto.getLocalityId())
                    .orElseThrow(() -> new InvalidUpdateException("La localidad especificada no existe."));
            location.setLocality(locality);
        }


//        if (dto.getPostalCode() != null) {
//            location.setPostalCode(dto.getPostalCode());
//        }

        if (dto.getMainStreetId() != null) {
            var mainStreet = streetRepository.findByStreetId(dto.getMainStreetId())
                    .orElseThrow(() -> new InvalidUpdateException("La calle principal especificada no existe."));
            location.setMainStreet(mainStreet);
        }

        // streetOne and streetTwo can be set to null intentionally
        if (dto.getStreetOneId() != null) {
            var streetOne = streetRepository.findByStreetId(dto.getStreetOneId())
                    .orElseThrow(() -> new InvalidUpdateException("La calle uno especificada no existe."));
            location.setStreetOne(streetOne);
        }

        if (dto.getStreetTwoId() != null) {
            var streetTwo = streetRepository.findByStreetId(dto.getStreetTwoId())
                    .orElseThrow(() -> new InvalidUpdateException("La calle dos especificada no existe."));
            location.setStreetTwo(streetTwo);
        }

        locationRepository.save(location);

        // Audit log: update
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ACTUALIZACION_UBICACION", "El usuario " + u.getUsername() +
                        " actualizó la ubicación " + dto.getLocationId() + ".")
        );
    }

    @Override
    @Transactional
    public void delete(String token, long locationId) {
        if (!verificationService.isUserAdmin(token))
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");

        var location = locationRepository.findByLocationId(locationId)
                .orElseThrow(() -> new InvalidUpdateException("La ubicación no existe. No es posible borrarla."));

        locationRepository.deleteById(locationId);

        // Audit log: delete
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ELIMINACION_UBICACION", "El usuario " + u.getUsername() +
                        " eliminó la ubicación (id " + location.getLocationId() + ").")
        );
    }
}
