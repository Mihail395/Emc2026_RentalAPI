package mk.ukim.finki.emt.rentalapi.service;

import mk.ukim.finki.emt.rentalapi.model.domain.Country;
import mk.ukim.finki.emt.rentalapi.model.dto.CreateCountryRequest;
import mk.ukim.finki.emt.rentalapi.model.dto.UpdateCountryRequest;

import java.util.List;
import java.util.Optional;

public interface CountryService {

    // Returns all countries from the database
    List<Country> findAll();
    Optional<Country> findById(Long id);
    Country create(CreateCountryRequest request);
    Country update(Long id, UpdateCountryRequest request);
    void delete(Long id);
}