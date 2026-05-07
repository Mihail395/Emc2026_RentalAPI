package mk.ukim.finki.emt.rentalapi.service;

import mk.ukim.finki.emt.rentalapi.model.domain.Accommodation;
import mk.ukim.finki.emt.rentalapi.model.domain.ActivityLog;
import mk.ukim.finki.emt.rentalapi.model.dto.*;
import mk.ukim.finki.emt.rentalapi.model.enums.AccommodationCategory;
import mk.ukim.finki.emt.rentalapi.model.enums.AccommodationCondition;
import mk.ukim.finki.emt.rentalapi.model.projection.AccommodationExtendedProjection;
import mk.ukim.finki.emt.rentalapi.model.projection.AccommodationShortProjection;
import mk.ukim.finki.emt.rentalapi.model.views.AccommodationStats;
import mk.ukim.finki.emt.rentalapi.model.views.AccommodationView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AccommodationService {

    // Returns all accommodations
    List<Accommodation> findAll();

    // Returns single accommodation by its ID, or empty if not found
    Optional<Accommodation> findById(Long id);

    // Creates new accommodation from the request data
    Accommodation create(CreateAccommodationRequest request);

    // Updates existing accommodation identified by ID
    Accommodation update(Long id, UpdateAccommodationRequest request);

    // Deletes an accommodation by ID
    void delete(Long id);

    // Rent a specific number of rooms
    Accommodation rentRooms(Long id, RentRoomsRequest request);

    // Free up a specific number of rooms
    Accommodation freeRooms(Long id, FreeRoomsRequest request);


    // Filtered pagination
    Page<AccommodationSummaryDto> findAllFiltered(
            AccommodationCategory category,
            Long hostId,
            Long countryId,
            Integer minRooms,
            Boolean hasAvailableRooms,
            Pageable pageable
    );

    // Short projection
    List<AccommodationShortProjection> findAllShortProjection();

    // Extended projection
    List<AccommodationExtendedProjection> findAllExtendedProjection();

    // Find accommodation with host and country with single query using entity graph
    Optional<Accommodation> findByIdWithHostAndCountry(Long id);

    // Same as above but find all
    List<Accommodation> findAllWithHostAndCountry();

    // Find all accommodations from view
    List<AccommodationView> findAllFromView();

    // Find stats per category for all accommodations using materialized view
    List<AccommodationStats> findAllStats();

    // Find all activity logs that are stored in the table in the database with pagination
    Page<ActivityLog> findActivityLogs(Pageable pageable);

    // Find the most popular accommodations sorted in descending order
    List<AccommodationPopularityDto> findMostPopularAccommodations();

    // Find the most popular hosts sorted in descending order (based on rents)
    List<HostPopularityDto> findMostPopularHosts();

    // Filter accommodations based on condition
    List<Accommodation> findAllByCondition(AccommodationCondition condition);
}