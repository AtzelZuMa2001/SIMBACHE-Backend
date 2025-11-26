package com.korealm.simbache.services;

import com.korealm.simbache.dtos.potholes.PotholeCategoryCreateDto;
import com.korealm.simbache.dtos.potholes.PotholeCategoryResponseDto;
import com.korealm.simbache.dtos.potholes.PotholeCategoryUpdateDto;
import com.korealm.simbache.exceptions.InvalidInsertException;
import com.korealm.simbache.exceptions.InvalidUpdateException;
import com.korealm.simbache.exceptions.ResourceNotFoundException;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.PotholeCategory;
import com.korealm.simbache.repositories.PotholeCategoryRepository;
import com.korealm.simbache.services.interfaces.AuditLoggingService;
import com.korealm.simbache.services.interfaces.PotholeCategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PotholeCategoryServiceImpl implements PotholeCategoryService {

    private final PotholeCategoryRepository repository;
    private final VerificationService verificationService;
    private final AuditLoggingService auditLoggingService;

    @Override
    public List<PotholeCategoryResponseDto> getAll(String token) {
        if (verificationService.isUserUnauthorized(token)) {
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");
        }

        var categories = repository.findAll();

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_CATEGORIAS_BACHE",
                        "El usuario " + u.getUsername() + " consultó la lista de categorías de baches (" + categories.size() + " elementos).")
        );

        return categories.stream()
                .map(PotholeCategoryResponseDto::new)
                .toList();
    }

    @Override
    public PotholeCategoryResponseDto getById(String token, Short categoryId) {
        if (verificationService.isUserUnauthorized(token)) {
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");
        }

        var category = repository.findByCategoryId(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("La categoría con id " + categoryId + " no existe."));

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_CATEGORIA_BACHE",
                        "El usuario " + u.getUsername() + " consultó la categoría de bache id " + categoryId + ".")
        );

        return new PotholeCategoryResponseDto(category);
    }

    @Override
    public List<PotholeCategoryResponseDto> getByPriorityLevel(String token, int priorityLevel) {
        if (verificationService.isUserUnauthorized(token)) {
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");
        }

        var categories = repository.findAllByPriorityLevel(priorityLevel);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_CATEGORIAS_POR_PRIORIDAD",
                        "El usuario " + u.getUsername() + " consultó las categorías con prioridad " + priorityLevel + " (" + categories.size() + " elementos).")
        );

        return categories.stream()
                .map(PotholeCategoryResponseDto::new)
                .toList();
    }

    @Override
    public Short add(String token, PotholeCategoryCreateDto dto) {
        if (!verificationService.isUserAdmin(token)) {
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");
        }

        if (repository.findByCategoryId(dto.getCategoryId()).isPresent()) {
            throw new InvalidInsertException("Ya existe una categoría con el id " + dto.getCategoryId() + ".");
        }

        if (repository.findByName(dto.getName()).isPresent()) {
            throw new InvalidInsertException("Ya existe una categoría con el nombre '" + dto.getName() + "'.");
        }

        var category = PotholeCategory.builder()
                .categoryId(dto.getCategoryId())
                .name(dto.getName())
                .description(dto.getDescription())
                .priorityLevel(dto.getPriorityLevel())
                .build();

        var saved = repository.save(category);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CREACION_CATEGORIA_BACHE",
                        "El usuario " + u.getUsername() + " creó la categoría de bache '" + dto.getName() + "' con id " + saved.getCategoryId() + ".")
        );

        return saved.getCategoryId();
    }

    @Override
    public void update(String token, Short categoryId, PotholeCategoryUpdateDto dto) {
        if (!verificationService.isUserAdmin(token)) {
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");
        }

        var category = repository.findByCategoryId(categoryId)
                .orElseThrow(() -> new InvalidUpdateException("La categoría con id " + categoryId + " no existe. No es posible actualizarla."));

        if (dto.getName() != null && !dto.getName().isBlank()) {
            var existing = repository.findByName(dto.getName());
            if (existing.isPresent() && !existing.get().getCategoryId().equals(categoryId)) {
                throw new InvalidUpdateException("Ya existe otra categoría con el nombre '" + dto.getName() + "'.");
            }
            category.setName(dto.getName());
        }

        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            category.setDescription(dto.getDescription());
        }

        if (dto.getPriorityLevel() != null) {
            category.setPriorityLevel(dto.getPriorityLevel());
        }

        repository.save(category);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ACTUALIZACION_CATEGORIA_BACHE",
                        "El usuario " + u.getUsername() + " actualizó la categoría de bache id " + categoryId + ".")
        );
    }

    @Override
    @Transactional
    public void delete(String token, Short categoryId) {
        if (!verificationService.isUserAdmin(token)) {
            throw new UnauthorizedAccessException("El usuario no tiene permisos para la acción solicitada.");
        }

        var category = repository.findByCategoryId(categoryId)
                .orElseThrow(() -> new InvalidUpdateException("La categoría con id " + categoryId + " no existe. No es posible eliminarla."));

        repository.delete(category);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "ELIMINACION_CATEGORIA_BACHE",
                        "El usuario " + u.getUsername() + " eliminó la categoría de bache '" + category.getName() + "' (id " + categoryId + ").")
        );
    }
}