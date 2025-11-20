package com.korealm.simbache.services.interfaces;

import java.util.Map;

public interface MunicipalitiesService {
    Map<Short, String> getMunicipalitiesByState(String token, short stateId);
}
