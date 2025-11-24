package com.korealm.simbache.services;

import com.korealm.simbache.dtos.potholes.PotholeCreateDto;
import com.korealm.simbache.dtos.potholes.PotholeResponseDto;
import com.korealm.simbache.dtos.potholes.PotholeUpdateDto;
import com.korealm.simbache.exceptions.InvalidInsertException;
import com.korealm.simbache.exceptions.InvalidUpdateException;
import com.korealm.simbache.exceptions.UnauthorizedAccessException;
import com.korealm.simbache.models.Pothole;
import com.korealm.simbache.repositories.*;
import com.korealm.simbache.services.interfaces.PotholesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PotholesServiceImpl implements PotholesService {
    private final PotholeRepository potholeRepository;
    private final CitizensRepository citizensRepository;
    private final LocationRepository locationRepository;
    private final PotholeCategoryRepository potholeCategoryRepository;
    private final PotholeStatusRepository potholeStatusRepository;

    private final VerificationService verificationService;
    private final AuditLoggingServiceImpl auditLoggingService;

    @Override
    public PotholeResponseDto getById(String token, Long id) {
        if (verificationService.isUserUnauthorized(token))
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var pothole = potholeRepository.findByPotholeId(id)
                .orElseThrow(() -> new InvalidUpdateException("El bache especificado no existe."));

        // Audit log
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "CONSULTA_BACHE", "El usuario " + u.getUsername() + " consultó el bache id " + id + "."));

        return toDto(pothole);
    }

    @Override
    public Page<PotholeResponseDto> listAll(String token, int page, int size) {
        if (verificationService.isUserUnauthorized(token))
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var pageable = PageRequest.of(page, size);
        var pageResult = potholeRepository.findAll(pageable);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "LISTA_BACHES", "El usuario " + u.getUsername() +
                        " consultó la lista de baches paginada (" + pageResult.getTotalElements() + ") elementos en total)."));

        return pageResult.map(this::toDto);
    }

    @Override
    public List<PotholeResponseDto> listActive(String token) {
        if (verificationService.isUserUnauthorized(token))
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var list = potholeRepository.findAllByIsActiveTrue();
        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "LISTA_BACHES_ACTIVOS", "El usuario " + u.getUsername() + " consultó la lista de baches activos (" + list.size() + ") elementos)."));
        return list.stream().map(this::toDto).toList();
    }

    @Override
    public Page<PotholeResponseDto> listActivePaginated(String token, int page, int size) {
        if (verificationService.isUserUnauthorized(token))
            throw new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes.");

        var pageable = PageRequest.of(page, size);
        var pageResult = potholeRepository.findAllByIsActiveTrue(pageable);

        verificationService.getUserByToken(token).ifPresent(u ->
                auditLoggingService.log(u, "LISTA_BACHES_ACTIVOS_PAGINADA", "El usuario " + u.getUsername() +
                        " consultó la lista paginada de baches activos (" + pageResult.getTotalElements() + ") elementos en total)."));

        return pageResult.map(this::toDto);
    }

    @Override
    public Long add(String token, PotholeCreateDto dto) {
        var user = verificationService.getUserByToken(token)
                .orElseThrow(() -> new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes."));

        var location = locationRepository.findByLocationId(dto.getLocationId())
                .orElseThrow(() -> new InvalidInsertException("La ubicación especificada no existe."));
        var category = potholeCategoryRepository.findByCategoryId(dto.getCategoryId())
                .orElseThrow(() -> new InvalidInsertException("La categoría especificada no existe."));
        var status = potholeStatusRepository.findByStatusId(dto.getStatusId())
                .orElseThrow(() -> new InvalidInsertException("El estatus especificado no existe."));

        var citizen = dto.getReportByCitizenId() == null ? null : citizensRepository.findByCitizenId(dto.getReportByCitizenId())
                .orElseThrow(() -> new InvalidInsertException("El ciudadano que reporta no existe."));

        var pothole = Pothole.builder()
                .reportByCitizen(citizen)
                .registeredBy(user)
                .location(location)
                .category(category)
                .status(status)
                .photoUrl(dto.getPhotoUrl())
                .dateReported(dto.getDateReported() != null ? dto.getDateReported() : LocalDateTime.now())
                .isActive(true)
                .build();

        var saved = potholeRepository.save(pothole);

        auditLoggingService.log(user, "CREACION_BACHE", "El usuario " + user.getUsername() + " registró el bache id " + saved.getPotholeId() + ".");

        return saved.getPotholeId();
    }

    @Override
    public void update(String token, Long id, PotholeUpdateDto dto) {
        var user = verificationService.getUserByToken(token)
                .orElseThrow(() -> new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes."));

        var pothole = potholeRepository.findByPotholeId(id)
                .orElseThrow(() -> new InvalidUpdateException("El bache especificado no existe."));

        if (dto.getReportByCitizenId() != null) {
            var citizen = citizensRepository.findByCitizenId(dto.getReportByCitizenId())
                    .orElseThrow(() -> new InvalidUpdateException("El ciudadano especificado no existe."));
            pothole.setReportByCitizen(citizen);
        }
        if (dto.getLocationId() != null) {
            var location = locationRepository.findByLocationId(dto.getLocationId())
                    .orElseThrow(() -> new InvalidUpdateException("La ubicación especificada no existe."));
            pothole.setLocation(location);
        }
        if (dto.getCategoryId() != null) {
            var category = potholeCategoryRepository.findByCategoryId(dto.getCategoryId())
                    .orElseThrow(() -> new InvalidUpdateException("La categoría especificada no existe."));
            pothole.setCategory(category);
        }
        if (dto.getStatusId() != null) {
            var status = potholeStatusRepository.findByStatusId(dto.getStatusId())
                    .orElseThrow(() -> new InvalidUpdateException("El estatus especificado no existe."));
            pothole.setStatus(status);
        }
        if (dto.getPhotoUrl() != null) pothole.setPhotoUrl(dto.getPhotoUrl());
        if (dto.getDateValidated() != null) pothole.setDateValidated(dto.getDateValidated());
        if (dto.getDateClosed() != null) pothole.setDateClosed(dto.getDateClosed());
        if (dto.getIsActive() != null) pothole.setActive(dto.getIsActive());

        potholeRepository.save(pothole);

        auditLoggingService.log(user, "ACTUALIZACION_BACHE", "El usuario " + user.getUsername() + " actualizó el bache id " + id + ".");
    }

    @Override
    @Transactional
    public void delete(String token, Long id) {
        var user = verificationService.getUserByToken(token)
                .orElseThrow(() -> new UnauthorizedAccessException("El usuario no está autenticado. Inicia sesión para poder hacer solicitudes."));

        var pothole = potholeRepository.findByPotholeId(id)
                .orElseThrow(() -> new InvalidUpdateException("El bache especificado no existe."));

        pothole.setActive(false);
        if (pothole.getDateClosed() == null) pothole.setDateClosed(LocalDateTime.now());
        potholeRepository.save(pothole);

        auditLoggingService.log(user, "ELIMINACION_BACHE", "El usuario " + user.getUsername() + " eliminó (soft delete) el bache id " + id + ".");
    }

    private PotholeResponseDto toDto(Pothole p) {
        // Reporter citizen
        var reporter = p.getReportByCitizen() == null ? null : com.korealm.simbache.dtos.potholes.ReporterCitizenDto.builder()
                .firstName(p.getReportByCitizen().getFirstName())
                .lastName(p.getReportByCitizen().getLastName())
                .email(p.getReportByCitizen().getEmail())
                .phoneNumber(p.getReportByCitizen().getPhoneNumber())
                .build();

        // Registered user
        var user = p.getRegisteredBy();
        var registeredBy = user == null ? null : com.korealm.simbache.dtos.potholes.RegisteredUserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roleName(user.getUserRole() != null ? user.getUserRole().getRoleName() : null)
                .build();

        // Location details
        var loc = p.getLocation();
        var locationDetails = loc == null ? null : com.korealm.simbache.dtos.potholes.LocationDetailsDto.builder()
                .stateName(loc.getState() != null ? loc.getState().getStateName() : null)
                .municipalityName(loc.getMunicipality() != null ? loc.getMunicipality().getMunicipalityName() : null)
                .localityName(loc.getLocality() != null ? loc.getLocality().getLocalityName() : null)
                .coloniaName(loc.getColonia() != null ? loc.getColonia().getColoniaName() : null)
                .postalCode(loc.getPostalCode())
                .mainStreetName(loc.getMainStreet() != null ? loc.getMainStreet().getStreetName() : null)
                .streetOneName(loc.getStreetOne() != null ? loc.getStreetOne().getStreetName() : null)
                .streetTwoName(loc.getStreetTwo() != null ? loc.getStreetTwo().getStreetName() : null)
                .build();

        // Category details
        var cat = p.getCategory();
        var categoryDetails = cat == null ? null : com.korealm.simbache.dtos.potholes.CategoryDetailsDto.builder()
                .categoryName(cat.getName())
                .description(cat.getDescription())
                .priorityLevel(cat.getPriorityLevel())
                .build();

        return PotholeResponseDto.builder()
                .potholeId(p.getPotholeId())
                .reporterCitizen(reporter)
                .registeredByUser(registeredBy)
                .location(locationDetails)
                .category(categoryDetails)
                .status(p.getStatus().getStatusName())
                .photoUrl(p.getPhotoUrl())
                .dateReported(p.getDateReported())
                .dateValidated(p.getDateValidated())
                .dateClosed(p.getDateClosed())
                .isActive(p.isActive())
                .build();
    }
}
