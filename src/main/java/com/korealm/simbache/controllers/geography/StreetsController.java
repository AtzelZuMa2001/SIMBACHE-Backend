package com.korealm.simbache.controllers.geography;

import com.korealm.simbache.services.geography.StreetsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/geography/streets")
@RequiredArgsConstructor
public class StreetsController {
    private final StreetsServiceImpl service;

    @GetMapping("/list/by-colonia")
    public ResponseEntity<Map<Long, String>> getByColonia(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam Long coloniaId
    ) {
        var response = service.getStreetsByColonia(token, coloniaId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list/by-locality")
    public ResponseEntity<Map<Long, String>> getByLocality(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam int localityId
    ) {
        var response = service.getStreetsByLocality(token, localityId);
        return ResponseEntity.ok(response);
    }
}
