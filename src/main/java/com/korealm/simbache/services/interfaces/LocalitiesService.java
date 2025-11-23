package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.LocalityCreateDto;
import com.korealm.simbache.dtos.geography.LocalityDto;

import java.util.List;

public interface LocalitiesService {
    List<LocalityDto> getLocalitiesByMunicipality(String token, short municipalityId);

    int addLocality(String token, LocalityCreateDto dto);
    void updateLocality(String token, BasicUpdateDto dto);
    void deleteLocality(String token, int localityId);
}
