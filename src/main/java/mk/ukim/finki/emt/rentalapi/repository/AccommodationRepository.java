package mk.ukim.finki.emt.rentalapi.repository;

import mk.ukim.finki.emt.rentalapi.model.domain.Accommodation;
import mk.ukim.finki.emt.rentalapi.model.enums.AccommodationCategory;
import mk.ukim.finki.emt.rentalapi.model.enums.AccommodationCondition;
import mk.ukim.finki.emt.rentalapi.model.projection.AccommodationExtendedProjection;
import mk.ukim.finki.emt.rentalapi.model.projection.AccommodationShortProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {


    @EntityGraph(value = "Accommodation.withHostAndCountry", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Accommodation> findWithHostAndCountryById(Long id);

    @Query("SELECT a FROM Accommodation a")
    @EntityGraph(value = "Accommodation.withHostAndCountry",  type = EntityGraph.EntityGraphType.FETCH)
    List<Accommodation> findAllWithHostAndCountry();

    List<AccommodationShortProjection> findAllProjectedBy();

    List<AccommodationExtendedProjection> findAllExtendedProjectedBy();

    @Query("SELECT a FROM Accommodation a WHERE :condition IS NULL OR a.condition = :condition")
    List<Accommodation> findAllByCondition(@Param("condition") AccommodationCondition condition);

    @Query("""
        SELECT a FROM Accommodation a
        JOIN a.host h
        JOIN h.country c
        WHERE (:category IS NULL OR a.category = :category)
        AND (:hostId IS NULL OR h.id = :hostId)
        AND (:countryId IS NULL OR c.id = :countryId)
        AND (:minRooms IS NULL OR a.numRooms >= :minRooms)
        AND (:hasAvailableRooms IS NULL OR :hasAvailableRooms = FALSE
             OR a.rentedRooms < a.numRooms)
    """)
    Page<Accommodation> findAllFiltered(
            @Param("category") AccommodationCategory category,
            @Param("hostId") Long hostId,
            @Param("countryId") Long countryId,
            @Param("minRooms") Integer minRooms,
            @Param("hasAvailableRooms") Boolean hasAvailableRooms,
            Pageable pageable
    );
}