package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.potholes.PotholeCreateDto;
import com.korealm.simbache.dtos.potholes.PotholeResponseDto;
import com.korealm.simbache.dtos.potholes.PotholeUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PotholesService {
    PotholeResponseDto getById(String token, Long id);
    Page<PotholeResponseDto> listAll(String token, int page, int size);
    List<PotholeResponseDto> listActive(String token);
    Page<PotholeResponseDto> listActivePaginated(String token, int page, int size);

    Long add(String token, PotholeCreateDto dto);
    void update(String token, Long id, PotholeUpdateDto dto);
    void delete(String token, Long id);
}
