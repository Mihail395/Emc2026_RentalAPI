package mk.ukim.finki.emt.rentalapi.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emt.rentalapi.model.domain.Accommodation;
import mk.ukim.finki.emt.rentalapi.model.domain.ActivityLog;
import mk.ukim.finki.emt.rentalapi.model.dto.*;
import mk.ukim.finki.emt.rentalapi.model.enums.AccommodationCategory;
import mk.ukim.finki.emt.rentalapi.model.enums.AccommodationCondition;
import mk.ukim.finki.emt.rentalapi.model.projection.AccommodationExtendedProjection;
import mk.ukim.finki.emt.rentalapi.model.projection.AccommodationShortProjection;
import mk.ukim.finki.emt.rentalapi.model.views.AccommodationStats;
import mk.ukim.finki.emt.rentalapi.model.views.AccommodationView;
import mk.ukim.finki.emt.rentalapi.service.AccommodationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
@Tag(name = "Accommodations", description = "Operations related to accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping
    @Operation(summary = "Get all accommodations")
    public ResponseEntity<List<Accommodation>> findAll() {
        return ResponseEntity.ok(accommodationService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get accommodation by ID")
    public ResponseEntity<Accommodation> findById(@PathVariable Long id) {
        return accommodationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    @Operation(summary = "Create a new accommodation")
    public ResponseEntity<Accommodation> create(@Valid @RequestBody CreateAccommodationRequest request) {
        Accommodation created = accommodationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/edit")
    @Operation(summary = "Update an existing accommodation")
    public ResponseEntity<Accommodation> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAccommodationRequest request) {
        return ResponseEntity.ok(accommodationService.update(id, request));
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Delete an accommodation")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accommodationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/rent")
    @Operation(summary = "Rent a number of rooms in an accommodation")
    public ResponseEntity<Accommodation> rentRooms(
            @PathVariable Long id,
            @Valid @RequestBody RentRoomsRequest request) {
        return ResponseEntity.ok(accommodationService.rentRooms(id, request));
    }

    @PatchMapping("/{id}/free")
    @Operation(summary = "Free a number of rooms in an accommodation")
    public ResponseEntity<Accommodation> freeRooms(
            @PathVariable Long id,
            @Valid @RequestBody FreeRoomsRequest request) {
        return ResponseEntity.ok(accommodationService.freeRooms(id, request));
    }

    @GetMapping("/filter")
    @Operation(summary = "List accommodations with pagination and filters")
    public ResponseEntity<Page<AccommodationSummaryDto>> findAllFiltered(
            @RequestParam(required = false) AccommodationCategory category,
            @RequestParam(required = false) Long hostId,
            @RequestParam(required = false) Long countryId,
            @RequestParam(required = false) Integer minRooms,
            @RequestParam(required = false) Boolean hasAvailableRooms,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(accommodationService.findAllFiltered(
                category, hostId, countryId, minRooms, hasAvailableRooms, pageable));
    }

    @GetMapping("/short")
    @Operation(summary = "Get all accommodations - short projection (id, name, category, numRooms)")
    public ResponseEntity<List<AccommodationShortProjection>> findAllShort() {
        return ResponseEntity.ok(accommodationService.findAllShortProjection());
    }

    @GetMapping("/extended")
    @Operation(summary = "Get all accommodations - extended projection with host and country info")
    public ResponseEntity<List<AccommodationExtendedProjection>> findAllExtended() {
        return ResponseEntity.ok(accommodationService.findAllExtendedProjection());
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "Get single accommodation with host and country loaded via EntityGraph")
    public ResponseEntity<Accommodation> findByIdWithDetails(@PathVariable Long id) {
        return accommodationService.findByIdWithHostAndCountry(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/with-details")
    @Operation(summary = "Get all accommodations with host and country loaded via EntityGraph")
    public ResponseEntity<List<Accommodation>> findAllWithDetails() {
        return ResponseEntity.ok(accommodationService.findAllWithHostAndCountry());
    }

    @GetMapping("/view")
    @Operation(summary = "Get all accommodations from database view")
    public ResponseEntity<List<AccommodationView>> findAllFromView() {
        return ResponseEntity.ok(accommodationService.findAllFromView());
    }

    @GetMapping("/stats")
    @Operation(summary = "Get accommodation statistics per category from materialized view")
    public ResponseEntity<List<AccommodationStats>> findAllStats() {
        return ResponseEntity.ok(accommodationService.findAllStats());
    }

    @GetMapping("/activity-logs")
    @Operation(summary = "Get activity logs with pagination")
    public ResponseEntity<Page<ActivityLog>> findActivityLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(accommodationService.findActivityLogs(pageable));
    }

    @GetMapping("/most-popular")
    @Operation(summary = "Get most popular accommodations sorted by number of rentals descending")
    public ResponseEntity<List<AccommodationPopularityDto>> findMostPopularAccommodations() {
        return ResponseEntity.ok(accommodationService.findMostPopularAccommodations());
    }

    @GetMapping("/most-popular-hosts")
    @Operation(summary = "Get most popular hosts sorted by number of rentals descending")
    public ResponseEntity<List<HostPopularityDto>> findMostPopularHosts() {
        return ResponseEntity.ok(accommodationService.findMostPopularHosts());
    }

    @GetMapping("/filter-by-condition")
    @Operation(summary = "Filter accommodations by condition")
    public ResponseEntity<List<Accommodation>> findAllByCondition(
            @RequestParam(required = false) AccommodationCondition condition) {
        return ResponseEntity.ok(accommodationService.findAllByCondition(condition));
    }
}