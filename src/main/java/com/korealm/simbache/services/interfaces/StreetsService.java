package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.StreetCreateDto;
import com.korealm.simbache.dtos.geography.StreetDto;

import java.util.List;

public interface StreetsService {
    List<StreetDto> getStreetsByColonia(String token, Long coloniaId);
    List<StreetDto> getStreetsByLocality(String token, int localityId);

    long addStreet(String token, StreetCreateDto dto);
    void updateStreet(String token, BasicUpdateDto dto);
    void deleteStreet(String token, long streetId);
}
