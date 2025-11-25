package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.geography.LocationCreateDto;
import com.korealm.simbache.dtos.geography.LocationDto;
import com.korealm.simbache.dtos.geography.LocationUpdateDto;

import java.util.List;

public interface LocationsService {
    List<LocationDto> list(String token,
                           Short stateId,
                           Short municipalityId,
                           Integer localityId,
                           Long coloniaId,
                           Long mainStreetId);

    LocationDto getById(String token, long locationId);

    long add(String token, LocationCreateDto dto);

    void update(String token, LocationUpdateDto dto);

    void delete(String token, long locationId);
}
