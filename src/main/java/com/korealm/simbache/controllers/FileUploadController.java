package com.korealm.simbache.controllers;

import com.korealm.simbache.dtos.files.FileUploadResponseDto;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.services.FileStorageServiceImpl;
import com.korealm.simbache.services.VerificationService;
import com.korealm.simbache.services.interfaces.AuditLoggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileStorageServiceImpl fileStorageService;
    private final VerificationService verificationService;
    private final AuditLoggingService auditLoggingService;

    /**
     * Uploads an image file for potholes.
     *
     * @param token The authentication token
     * @param file  The image file to upload
     * @return The file upload response with the URL
     */
    @PostMapping(value = "/upload/pothole-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponseDto> uploadPotholeImage(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam("file") MultipartFile file
    ) {
        if (verificationService.isUserUnauthorized(token)) {
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");
        }

        String fileUrl = fileStorageService.storeFile(file);

        // Audit log
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "SUBIDA_IMAGEN",
                        "El usuario " + u.getUsername() + " subió una imagen: " + file.getOriginalFilename())
        );

        var response = FileUploadResponseDto.builder()
                .filename(file.getOriginalFilename())
                .url(fileUrl)
                .contentType(file.getContentType())
                .size(file.getSize())
                .message("Archivo subido exitosamente.")
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Deletes an uploaded file.
     *
     * @param token    The authentication token
     * @param filename The name of the file to delete
     * @return Success or failure response
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(
            @RequestHeader("X-Auth-Token") String token,
            @RequestParam String filename
    ) {
        if (!verificationService.isUserAdmin(token)) {
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");
        }

        boolean deleted = fileStorageService.deleteFile(filename);

        // Audit log
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ELIMINACION_IMAGEN",
                        "El usuario " + u.getUsername() + " eliminó el archivo: " + filename)
        );

        if (deleted) {
            return ResponseEntity.ok("Archivo eliminado exitosamente.");
        } else {
            return ResponseEntity.ok("El archivo no fue encontrado.");
        }
    }
}