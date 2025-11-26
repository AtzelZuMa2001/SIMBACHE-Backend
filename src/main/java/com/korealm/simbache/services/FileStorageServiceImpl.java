package com.korealm.simbache.services;

import com.korealm.simbache.exceptions.FileStorageException;
import com.korealm.simbache.services.interfaces.FileStorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.upload.dir:./uploads/potholes}")
    private String uploadDir;

    @Value("${app.upload.base-url:http://localhost:31234/uploads/potholes}")
    private String baseUrl;

    private Path uploadPath;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    @PostConstruct
    public void init() {
        try {
            uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);
            log.info("Upload directory initialized at: {}", uploadPath);
        } catch (IOException e) {
            throw new FileStorageException("No se pudo crear el directorio de uploads.", e);
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        return storeFile(file, null);
    }

    @Override
    public String storeFile(MultipartFile file, String subFolder) {
        validateFile(file);

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = getFileExtension(originalFilename);
        String newFilename = UUID.randomUUID() + "." + fileExtension;

        try {
            Path targetLocation;
            String fileUrl;

            if (subFolder != null && !subFolder.isBlank()) {
                Path subFolderPath = uploadPath.resolve(subFolder);
                Files.createDirectories(subFolderPath);
                targetLocation = subFolderPath.resolve(newFilename);
                fileUrl = baseUrl + "/" + subFolder + "/" + newFilename;
            } else {
                targetLocation = uploadPath.resolve(newFilename);
                fileUrl = baseUrl + "/" + newFilename;
            }

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            log.info("File stored successfully: {}", targetLocation);
            return fileUrl;

        } catch (IOException e) {
            throw new FileStorageException("No se pudo almacenar el archivo: " + originalFilename, e);
        }
    }

    @Override
    public boolean deleteFile(String filename) {
        try {
            Path filePath = uploadPath.resolve(filename).normalize();

            // Security check: ensure the file is within the upload directory
            if (!filePath.startsWith(uploadPath)) {
                throw new FileStorageException("No se puede acceder a archivos fuera del directorio de uploads.");
            }

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("File deleted successfully: {}", filePath);
                return true;
            } else {
                log.warn("File not found for deletion: {}", filePath);
                return false;
            }
        } catch (IOException e) {
            throw new FileStorageException("No se pudo eliminar el archivo: " + filename, e);
        }
    }

    @Override
    public String getFilenameFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return null;
        }
        return fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("El archivo está vacío o no fue proporcionado.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new FileStorageException("El nombre del archivo no es válido.");
        }

        // Validate extension
        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new FileStorageException(
                    "Tipo de archivo no permitido. Extensiones permitidas: " + String.join(", ", ALLOWED_EXTENSIONS)
            );
        }

        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new FileStorageException(
                    "Tipo de contenido no permitido. Solo se permiten imágenes (JPEG, PNG, GIF, WEBP)."
            );
        }
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            throw new FileStorageException("El archivo debe tener una extensión válida.");
        }
        return filename.substring(dotIndex + 1);
    }
}