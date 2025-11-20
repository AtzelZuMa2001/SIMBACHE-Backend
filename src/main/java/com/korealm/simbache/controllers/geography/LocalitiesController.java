package com.korealm.simbache.controllers.geography;

import com.korealm.simbache.services.geography.LocalitiesServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/geography/localities")
@RequiredArgsConstructor
public class LocalitiesController {
    private final LocalitiesServiceImpl service;

    @GetMapping("/list")
    public ResponseEntity<Map<Integer, String>> getByMunicipality(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam short municipalityId
    ) {
        var response = service.getLocalitiesByMunicipality(token, municipalityId);
        return ResponseEntity.ok(response);
    }
}
