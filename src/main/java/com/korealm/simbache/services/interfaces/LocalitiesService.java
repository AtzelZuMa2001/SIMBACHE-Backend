package com.korealm.simbache.services.interfaces;

import java.util.Map;

public interface LocalitiesService {
    Map<Integer, String> getLocalitiesByMunicipality(String token, short municipalityId);
}
