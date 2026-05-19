package mk.ukim.finki.emt.rentalapi.service;

import mk.ukim.finki.emt.rentalapi.model.domain.Host;
import mk.ukim.finki.emt.rentalapi.model.dto.CreateHostRequest;
import mk.ukim.finki.emt.rentalapi.model.dto.UpdateHostRequest;

import java.util.List;
import java.util.Optional;

public interface HostService {

    // Returns all hosts from the database
    List<Host> findAll();
    Optional<Host> findById(Long id);
    Host create(CreateHostRequest request);
    Host update(Long id, UpdateHostRequest request);
    void delete(Long id);
}