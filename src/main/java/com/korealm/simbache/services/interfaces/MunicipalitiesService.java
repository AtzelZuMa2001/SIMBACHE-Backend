package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.MunicipalityCreateDto;

import java.util.Map;

public interface MunicipalitiesService {
    Map<Short, String> getMunicipalitiesByState(String token, short stateId);

    short addMunicipality(String token, MunicipalityCreateDto dto);
    void updateMunicipality(String token, BasicUpdateDto dto);
    void deleteMunicipality(String token, short municipalityId);
}
