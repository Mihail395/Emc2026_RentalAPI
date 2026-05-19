package mk.ukim.finki.emt.rentalapi.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emt.rentalapi.model.domain.Country;
import mk.ukim.finki.emt.rentalapi.model.dto.CreateCountryRequest;
import mk.ukim.finki.emt.rentalapi.model.dto.UpdateCountryRequest;
import mk.ukim.finki.emt.rentalapi.repository.CountryRepository;
import mk.ukim.finki.emt.rentalapi.service.CountryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Used for DI
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    //DI

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    @Override
    public Optional<Country> findById(Long id) {
        return countryRepository.findById(id);
    }

    @Override
    public Country create(CreateCountryRequest request) {
        Country country = new Country();
        country.setName(request.name());
        country.setContinent(request.continent());
        return countryRepository.save(country);
    }

    @Override
    public Country update(Long id, UpdateCountryRequest request) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + id));
        country.setName(request.name());
        country.setContinent(request.continent());
        return countryRepository.save(country);
    }

    @Override
    public void delete(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new RuntimeException("Country not found with id: " + id);
        }
        countryRepository.deleteById(id);
    }
}