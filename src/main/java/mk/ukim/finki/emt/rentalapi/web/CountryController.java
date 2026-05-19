package mk.ukim.finki.emt.rentalapi.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emt.rentalapi.model.domain.Country;
import mk.ukim.finki.emt.rentalapi.model.dto.CreateCountryRequest;
import mk.ukim.finki.emt.rentalapi.model.dto.UpdateCountryRequest;
import mk.ukim.finki.emt.rentalapi.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
@Tag(name = "Countries", description = "Operations related to countries")
// @Tag is a Swagger annotation that groups this controller under "Countries" in the UI
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    @Operation(summary = "Get all countries")
    // @Operation describes this endpoint in Swagger UI
    public ResponseEntity<List<Country>> findAll() {
        return ResponseEntity.ok(countryService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get country by ID")
    public ResponseEntity<Country> findById(@PathVariable Long id) {
        return countryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    @Operation(summary = "Create a new country")
    public ResponseEntity<Country> create(@Valid @RequestBody CreateCountryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(countryService.create(request));
    }

    @PutMapping("/{id}/edit")
    @Operation(summary = "Update a country")
    public ResponseEntity<Country> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCountryRequest request) {
        return ResponseEntity.ok(countryService.update(id, request));
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Delete a country")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        countryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
