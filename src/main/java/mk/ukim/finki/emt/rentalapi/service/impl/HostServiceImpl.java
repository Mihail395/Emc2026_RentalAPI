package mk.ukim.finki.emt.rentalapi.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emt.rentalapi.model.domain.Country;
import mk.ukim.finki.emt.rentalapi.model.domain.Host;
import mk.ukim.finki.emt.rentalapi.model.dto.CreateHostRequest;
import mk.ukim.finki.emt.rentalapi.model.dto.UpdateHostRequest;
import mk.ukim.finki.emt.rentalapi.model.exception.HostNotFoundException;
import mk.ukim.finki.emt.rentalapi.repository.CountryRepository;
import mk.ukim.finki.emt.rentalapi.repository.HostRepository;
import mk.ukim.finki.emt.rentalapi.service.HostService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HostServiceImpl implements HostService {

    private final HostRepository hostRepository;
    private final CountryRepository countryRepository;

    @Override
    public List<Host> findAll() {
        return hostRepository.findAll();
    }

    @Override
    public Optional<Host> findById(Long id) {
        return hostRepository.findById(id);
    }

    @Override
    public Host create(CreateHostRequest request) {
        Country country = countryRepository.findById(request.countryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));

        Host host = new Host();
        host.setName(request.name());
        host.setSurname(request.surname());
        host.setCountry(country);

        return hostRepository.save(host);
    }

    @Override
    public Host update(Long id, UpdateHostRequest request) {
        Host host = hostRepository.findById(id)
                .orElseThrow(() -> new HostNotFoundException(id));

        Country country = countryRepository.findById(request.countryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));

        host.setName(request.name());
        host.setSurname(request.surname());
        host.setCountry(country);

        return hostRepository.save(host);
    }

    @Override
    public void delete(Long id) {
        if (!hostRepository.existsById(id)) {
            throw new HostNotFoundException(id);
        }
        hostRepository.deleteById(id);
    }
}