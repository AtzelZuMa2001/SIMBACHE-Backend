package com.korealm.simbache.controllers.geography;

import com.korealm.simbache.dtos.geography.LocationCreateDto;
import com.korealm.simbache.dtos.geography.LocationDto;
import com.korealm.simbache.dtos.geography.LocationUpdateDto;
import com.korealm.simbache.services.geography.LocationsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geography/locations")
@RequiredArgsConstructor
public class LocationsController {
    private final LocationsServiceImpl service;

    @GetMapping("/list")
    public ResponseEntity<List<LocationDto>> list(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam(required = false) Short stateId,
            @RequestParam(required = false) Short municipalityId,
            @RequestParam(required = false) Integer localityId,
            @RequestParam(required = false) Long coloniaId,
            @RequestParam(required = false) Long mainStreetId
    ) {
        var response = service.list(token, stateId, municipalityId, localityId, coloniaId, mainStreetId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-id")
    public ResponseEntity<LocationDto> getById(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam long locationId
    ) {
        var response = service.getById(token, locationId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Long> add(
            @RequestHeader("X-Auth-Token") String token,
            @Valid @RequestBody LocationCreateDto dto
    ) {
        var id = service.add(token, dto);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(
            @RequestHeader("X-Auth-Token") String token,
            @Valid @RequestBody LocationUpdateDto dto
    ) {
        service.update(token, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam long locationId
    ) {
        service.delete(token, locationId);
        return ResponseEntity.ok().build();
    }
}
