package com.korealm.simbache.controllers.geography;

import com.korealm.simbache.services.geography.ColoniasServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/geography/colonias")
@RequiredArgsConstructor
public class ColoniasController {
    private final ColoniasServiceImpl service;

    @GetMapping("/list")
    public ResponseEntity<Map<Long, String>> getByLocality(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam int localityId
    ) {
        var response = service.getColoniasByLocality(token, localityId);
        return ResponseEntity.ok(response);
    }
}
