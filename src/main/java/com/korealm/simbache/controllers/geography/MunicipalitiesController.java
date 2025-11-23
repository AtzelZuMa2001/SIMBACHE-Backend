package com.korealm.simbache.controllers.geography;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.MunicipalityCreateDto;
import com.korealm.simbache.dtos.geography.MunicipalityDto;
import com.korealm.simbache.services.geography.MunicipalitiesServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geography/municipalities")
@RequiredArgsConstructor
public class MunicipalitiesController {
    private final MunicipalitiesServiceImpl service;

    @GetMapping("/list")
    public ResponseEntity<List<MunicipalityDto>> getByState(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam short stateId
    ) {
        var response = service.getMunicipalitiesByState(token, stateId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Short> addMunicipality(
            @RequestHeader("X-Auth-Token") String token,
            @Valid @RequestBody MunicipalityCreateDto dto
    ) {
        var id = service.addMunicipality(token, dto);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateMunicipality(
            @RequestHeader("X-Auth-Token") String token,
            @Valid @RequestBody BasicUpdateDto dto
    ) {
        service.updateMunicipality(token, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteMunicipality(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam short municipalityId
    ) {
        service.deleteMunicipality(token, municipalityId);
        return ResponseEntity.ok().build();
    }
}
