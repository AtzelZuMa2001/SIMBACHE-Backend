package com.korealm.simbache.services.interfaces;

import java.util.Map;

public interface StreetsService {
    Map<Long, String> getStreetsByColonia(String token, Long coloniaId);
    Map<Long, String> getStreetsByLocality(String token, int localityId);
}
