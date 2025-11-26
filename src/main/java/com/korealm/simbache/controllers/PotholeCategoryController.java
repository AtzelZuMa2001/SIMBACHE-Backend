package com.korealm.simbache.controllers;

import com.korealm.simbache.dtos.potholes.PotholeCategoryCreateDto;
import com.korealm.simbache.dtos.potholes.PotholeCategoryResponseDto;
import com.korealm.simbache.dtos.potholes.PotholeCategoryUpdateDto;
import com.korealm.simbache.services.PotholeCategoryServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pothole-categories")
@RequiredArgsConstructor
public class PotholeCategoryController {

    private final PotholeCategoryServiceImpl service;

    @GetMapping("/list")
    public ResponseEntity<List<PotholeCategoryResponseDto>> getAll(
            @RequestHeader("X-Auth-Token") String token
    ) {
        var response = service.getAll(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<PotholeCategoryResponseDto> getById(
            @RequestHeader("X-Auth-Token") String token,
            @PathVariable Short categoryId
    ) {
        var response = service.getById(token, categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-priority")
    public ResponseEntity<List<PotholeCategoryResponseDto>> getByPriorityLevel(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam int priorityLevel
    ) {
        var response = service.getByPriorityLevel(token, priorityLevel);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Short> add(
            @RequestHeader("X-Auth-Token") String token,
            @Valid @RequestBody PotholeCategoryCreateDto dto
    ) {
        var id = service.add(token, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/update/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(
            @RequestHeader("X-Auth-Token") String token,
            @PathVariable Short categoryId,
            @Valid @RequestBody PotholeCategoryUpdateDto dto
    ) {
        service.update(token, categoryId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @RequestHeader("X-Auth-Token") String token,
            @PathVariable Short categoryId
    ) {
        service.delete(token, categoryId);
        return ResponseEntity.ok().build();
    }
}