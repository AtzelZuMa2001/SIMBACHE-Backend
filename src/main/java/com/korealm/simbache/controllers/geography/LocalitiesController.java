package com.korealm.simbache.controllers.geography;

import com.korealm.simbache.dtos.geography.LocalityCreateDto;
import com.korealm.simbache.dtos.geography.LocalityDto;
import com.korealm.simbache.dtos.geography.LocalityUpdateDto;
import com.korealm.simbache.services.geography.LocalitiesServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geography/localities")
@RequiredArgsConstructor
public class LocalitiesController {
    private final LocalitiesServiceImpl service;

    @GetMapping("/list")
    public ResponseEntity<List<LocalityDto>> getByMunicipality(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam short municipalityId
    ) {
        var response = service.getLocalitiesByMunicipality(token, municipalityId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Integer> addLocality(
            @RequestHeader("X-Auth-Token") String token,
            @Valid @RequestBody LocalityCreateDto dto
    ) {
        var id = service.addLocality(token, dto);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateLocality(
            @RequestHeader("X-Auth-Token") String token,
            @Valid @RequestBody LocalityUpdateDto dto
    ) {
        service.updateLocality(token, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteLocality(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam int localityId
    ) {
        service.deleteLocality(token, localityId);
        return ResponseEntity.ok().build();
    }
}
