package com.korealm.simbache.controllers.geography;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.StreetCreateDto;
import com.korealm.simbache.dtos.geography.StreetDto;
import com.korealm.simbache.services.geography.StreetsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geography/streets")
@RequiredArgsConstructor
public class StreetsController {
    private final StreetsServiceImpl service;

    @GetMapping("/list/by-locality")
    public ResponseEntity<List<StreetDto>> getByLocality(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam int localityId
    ) {
        var response = service.getStreetsByLocality(token, localityId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Long> addStreet(
            @RequestHeader("X-Auth-Token") String token,
            @Valid @RequestBody StreetCreateDto dto
    ) {
        var id = service.addStreet(token, dto);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateStreet(
            @RequestHeader("X-Auth-Token") String token,
            @Valid @RequestBody BasicUpdateDto dto
    ) {
        service.updateStreet(token, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteStreet(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam long streetId
    ) {
        service.deleteStreet(token, streetId);
        return ResponseEntity.ok().build();
    }
}
