package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.ColoniaCreateDto;

import java.util.Map;

public interface ColoniasService {
    Map<Long, String> getColoniasByLocality(String token, int localityId);

    long addColonia(String token, ColoniaCreateDto dto);
    void updateColonia(String token, BasicUpdateDto dto);
    void deleteColonia(String token, long coloniaId);
}
