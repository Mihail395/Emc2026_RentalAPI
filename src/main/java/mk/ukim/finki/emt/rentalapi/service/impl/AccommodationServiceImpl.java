package mk.ukim.finki.emt.rentalapi.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emt.rentalapi.event.AccommodationFullyBookedEvent;
import mk.ukim.finki.emt.rentalapi.event.RoomsRentedEvent;
import mk.ukim.finki.emt.rentalapi.model.domain.Accommodation;
import mk.ukim.finki.emt.rentalapi.model.domain.ActivityLog;
import mk.ukim.finki.emt.rentalapi.model.domain.Host;
import mk.ukim.finki.emt.rentalapi.model.dto.*;
import mk.ukim.finki.emt.rentalapi.model.enums.AccommodationCategory;
import mk.ukim.finki.emt.rentalapi.model.enums.AccommodationCondition;
import mk.ukim.finki.emt.rentalapi.model.projection.AccommodationExtendedProjection;
import mk.ukim.finki.emt.rentalapi.model.projection.AccommodationShortProjection;
import mk.ukim.finki.emt.rentalapi.model.views.AccommodationStats;
import mk.ukim.finki.emt.rentalapi.model.views.AccommodationView;
import mk.ukim.finki.emt.rentalapi.repository.*;
import mk.ukim.finki.emt.rentalapi.service.AccommodationService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import mk.ukim.finki.emt.rentalapi.model.exception.*;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final HostRepository hostRepository;
    private final AccommodationViewRepository accommodationViewRepository;
    private final AccommodationStatsRepository accommodationStatsRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ActivityLogRepository activityLogRepository;

    @Override
    public List<Accommodation> findAll() {
        return accommodationRepository.findAll();
    }

    @Override
    public Optional<Accommodation> findById(Long id) {
        return accommodationRepository.findById(id);
    }

    @Override
    public Accommodation create(CreateAccommodationRequest request) {
        Host host = hostRepository.findById(request.hostId())
                .orElseThrow(() -> new HostNotFoundException(request.hostId()));

        Accommodation accommodation = new Accommodation();
        accommodation.setName(request.name());
        accommodation.setCategory(request.category());
        accommodation.setCondition(AccommodationCondition.GOOD); // new accommodations start as GOOD
        accommodation.setHost(host);
        accommodation.setNumRooms(request.numRooms());
        accommodation.setRentedRooms(0); // 0 rented rooms at the beginning

        return accommodationRepository.save(accommodation);
    }

    @Override
    public Accommodation update(Long id, UpdateAccommodationRequest request) {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new AccommodationNotFoundException(id));

        Host host = hostRepository.findById(request.hostId())
                .orElseThrow(() -> new HostNotFoundException(request.hostId()));

        // Update the fields
        accommodation.setName(request.name());
        accommodation.setCategory(request.category());
        accommodation.setHost(host);
        accommodation.setCondition(request.condition());
        accommodation.setNumRooms(request.numRooms());

        return accommodationRepository.save(accommodation);
    }

    @Override
    public void delete(Long id) {
        if (!accommodationRepository.existsById(id)) {
            throw new AccommodationNotFoundException(id);
        }
        accommodationRepository.deleteById(id);
    }

    @Override
    public Accommodation rentRooms(Long id, RentRoomsRequest request) {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new AccommodationNotFoundException(id));

        // Cannot rent a BAD condition accommodation
        if (accommodation.getCondition() == AccommodationCondition.BAD) {
            throw new InvalidAccommodationStateException(
                    "Cannot rent rooms in accommodation with id: " + id + " because it is in BAD condition"
            );
        }

        int availableRooms = accommodation.getNumRooms() - accommodation.getRentedRooms();

        // Check if there are enough free rooms
        if (request.numRooms() > availableRooms) {
            throw new NotEnoughRoomsException(id, request.numRooms(), availableRooms);
        }

        accommodation.setRentedRooms(accommodation.getRentedRooms() + request.numRooms());
        Accommodation saved = accommodationRepository.save(accommodation);

        eventPublisher.publishEvent(new RoomsRentedEvent(this, saved, request.numRooms()));

        if (saved.getRentedRooms().equals(saved.getNumRooms())) {
            eventPublisher.publishEvent(new AccommodationFullyBookedEvent(this, saved));
        }
        return saved;
    }

    @Override
    public Accommodation freeRooms(Long id, FreeRoomsRequest request) {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new AccommodationNotFoundException(id));

        // Cannot free more rooms than what is currently rented
        if (request.numRooms() > accommodation.getRentedRooms()) {
            throw new InvalidAccommodationStateException(
                    "Cannot free " + request.numRooms() + " rooms because only "
                            + accommodation.getRentedRooms() + " rooms are currently rented"
            );
        }

        accommodation.setRentedRooms(accommodation.getRentedRooms() - request.numRooms());
        return accommodationRepository.save(accommodation);
    }

    @Override
    public Page<AccommodationSummaryDto> findAllFiltered(
            AccommodationCategory category,
            Long hostId,
            Long countryId,
            Integer minRooms,
            Boolean hasAvailableRooms,
            Pageable pageable) {

        return accommodationRepository
                .findAllFiltered(category, hostId, countryId, minRooms, hasAvailableRooms, pageable)
                .map(a -> new AccommodationSummaryDto(
                        a.getId(),
                        a.getName(),
                        a.getCategory(),
                        a.getCondition(),
                        a.getNumRooms(),
                        a.getRentedRooms(),
                        a.getNumRooms() - a.getRentedRooms(),
                        a.getHost().getName() + " " + a.getHost().getSurname(),
                        a.getHost().getCountry().getName()
                ));
    }

    @Override
    public List<AccommodationShortProjection> findAllShortProjection() {
        return accommodationRepository.findAllProjectedBy();
    }

    @Override
    public List<AccommodationExtendedProjection> findAllExtendedProjection() {
        return accommodationRepository.findAllExtendedProjectedBy();
    }

    @Override
    public Optional<Accommodation> findByIdWithHostAndCountry(Long id) {
        return accommodationRepository.findWithHostAndCountryById(id);
    }

    @Override
    public List<Accommodation> findAllWithHostAndCountry() {
        return accommodationRepository.findAllWithHostAndCountry();
    }

    @Override
    public List<AccommodationView> findAllFromView() {
        return accommodationViewRepository.findAll();
    }

    @Override
    public List<AccommodationStats> findAllStats() {
        return accommodationStatsRepository.findAll();
    }

    // Add this method
    @Override
    public Page<ActivityLog> findActivityLogs(Pageable pageable) {
        return activityLogRepository.findAllByOrderByEventTimeDesc(pageable);
    }

    @Override
    public List<AccommodationPopularityDto> findMostPopularAccommodations() {
        return activityLogRepository.findMostPopularAccommodations();
    }

    @Override
    public List<HostPopularityDto> findMostPopularHosts() {
        return activityLogRepository.findMostPopularHosts();
    }

    @Override
    public List<Accommodation> findAllByCondition(AccommodationCondition condition) {
        return accommodationRepository.findAllByCondition(condition);
    }
}