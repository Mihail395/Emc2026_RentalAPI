package mk.ukim.finki.emt.rentalapi.model.dto;

public record AuthResponse(
        String token,
        String username,
        String role
) {}