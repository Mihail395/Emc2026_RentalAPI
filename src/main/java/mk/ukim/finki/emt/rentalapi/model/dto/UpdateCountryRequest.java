package mk.ukim.finki.emt.rentalapi.model.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCountryRequest(
        @NotBlank(message = "Name must not be blank")
        String name,

        @NotBlank(message = "Continent must not be blank")
        String continent
) {}