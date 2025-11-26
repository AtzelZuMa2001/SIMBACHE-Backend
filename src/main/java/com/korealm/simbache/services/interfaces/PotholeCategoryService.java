package com.korealm.simbache.services.interfaces;

import com.korealm.simbache.dtos.potholes.PotholeCategoryCreateDto;
import com.korealm.simbache.dtos.potholes.PotholeCategoryResponseDto;
import com.korealm.simbache.dtos.potholes.PotholeCategoryUpdateDto;

import java.util.List;

public interface PotholeCategoryService {

    List<PotholeCategoryResponseDto> getAll(String token);

    PotholeCategoryResponseDto getById(String token, Short categoryId);

    List<PotholeCategoryResponseDto> getByPriorityLevel(String token, int priorityLevel);

    Short add(String token, PotholeCategoryCreateDto dto);

    void update(String token, Short categoryId, PotholeCategoryUpdateDto dto);

    void delete(String token, Short categoryId);
}