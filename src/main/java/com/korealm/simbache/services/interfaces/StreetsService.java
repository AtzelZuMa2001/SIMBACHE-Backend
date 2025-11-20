package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.StreetCreateDto;

import java.util.Map;

public interface StreetsService {
    Map<Long, String> getStreetsByColonia(String token, Long coloniaId);
    Map<Long, String> getStreetsByLocality(String token, int localityId);

    long addStreet(String token, StreetCreateDto dto);
    void updateStreet(String token, BasicUpdateDto dto);
    void deleteStreet(String token, long streetId);
}
