package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.ColoniaCreateDto;
import com.korealm.simbache.dtos.geography.ColoniaDto;

import java.util.List;

public interface ColoniasService {
    List<ColoniaDto> getColoniasByLocality(String token, int localityId);

    long addColonia(String token, ColoniaCreateDto dto);
    void updateColonia(String token, BasicUpdateDto dto);
    void deleteColonia(String token, long coloniaId);
}
