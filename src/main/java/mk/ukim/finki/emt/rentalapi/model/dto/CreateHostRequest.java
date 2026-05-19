package mk.ukim.finki.emt.rentalapi.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateHostRequest(
        @NotBlank(message = "Name must not be blank")
        String name,

        @NotBlank(message = "Surname must not be blank")
        String surname,

        @NotNull(message = "Country ID must not be null")
        Long countryId
) {}