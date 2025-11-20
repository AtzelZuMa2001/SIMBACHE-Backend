package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.LocalityCreateDto;

import java.util.Map;

public interface LocalitiesService {
    Map<Integer, String> getLocalitiesByMunicipality(String token, short municipalityId);

    int addLocality(String token, LocalityCreateDto dto);
    void updateLocality(String token, BasicUpdateDto dto);
    void deleteLocality(String token, int localityId);
}
