package com.korealm.simbache.controllers.geography;

import com.korealm.simbache.dtos.BasicUpdateDto;
import com.korealm.simbache.dtos.geography.ColoniaCreateDto;
import com.korealm.simbache.services.geography.ColoniasServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    public ResponseEntity<Long> addColonia(
            @RequestHeader("X-Auth-Token") String token,
            @Valid @RequestBody ColoniaCreateDto dto
    ) {
        var id = service.addColonia(token, dto);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateColonia(
            @RequestHeader("X-Auth-Token") String token,
            @Valid @RequestBody BasicUpdateDto dto
    ) {
        service.updateColonia(token, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteColonia(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam long coloniaId
    ) {
        service.deleteColonia(token, coloniaId);
        return ResponseEntity.ok().build();
    }
}
