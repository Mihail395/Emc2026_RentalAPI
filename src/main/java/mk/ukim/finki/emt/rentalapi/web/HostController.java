package mk.ukim.finki.emt.rentalapi.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emt.rentalapi.model.domain.Host;
import mk.ukim.finki.emt.rentalapi.model.dto.CreateHostRequest;
import mk.ukim.finki.emt.rentalapi.model.dto.UpdateHostRequest;
import mk.ukim.finki.emt.rentalapi.service.HostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hosts")
@RequiredArgsConstructor
@Tag(name = "Hosts", description = "Operations related to hosts")
public class HostController {

    private final HostService hostService;

    @GetMapping
    @Operation(summary = "Get all hosts")
    public ResponseEntity<List<Host>> findAll() {
        return ResponseEntity.ok(hostService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get host by ID")
    public ResponseEntity<Host> findById(@PathVariable Long id) {
        return hostService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    @Operation(summary = "Create a new host")
    public ResponseEntity<Host> create(@Valid @RequestBody CreateHostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hostService.create(request));
    }

    @PutMapping("/{id}/edit")
    @Operation(summary = "Update a host")
    public ResponseEntity<Host> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateHostRequest request) {
        return ResponseEntity.ok(hostService.update(id, request));
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Delete a host")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hostService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
