package com.korealm.simbache.controllers;

import com.korealm.simbache.dtos.potholes.PotholeCreateDto;
import com.korealm.simbache.dtos.potholes.PotholeResponseDto;
import com.korealm.simbache.dtos.potholes.PotholeUpdateDto;
import com.korealm.simbache.services.PotholesServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/potholes")
@RequiredArgsConstructor
public class PotholesController {
    private final PotholesServiceImpl service;

    @GetMapping("/{id}")
    public ResponseEntity<PotholeResponseDto> getById(@RequestHeader("X-Auth-Token") String token, @PathVariable Long id) {
        var response = service.getById(token, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<PotholeResponseDto>> listAll(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var response = service.listAll(token, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<PotholeResponseDto>> listActive(@RequestHeader("X-Auth-Token") String token) {
        var response = service.listActive(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/paginated")
    public ResponseEntity<Page<PotholeResponseDto>> listActivePaginated(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var response = service.listActivePaginated(token, page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Long> add(@RequestHeader("X-Auth-Token") String token, @Valid @RequestBody PotholeCreateDto dto) {
        var newId = service.add(token, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newId);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(@RequestHeader("X-Auth-Token") String token, @PathVariable Long id, @Valid @RequestBody PotholeUpdateDto dto) {
        service.update(token, id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@RequestHeader("X-Auth-Token") String token, @PathVariable Long id) {
        service.delete(token, id);
        return ResponseEntity.ok().build();
    }
}
