package mk.ukim.finki.emt.rentalapi.service;

import mk.ukim.finki.emt.rentalapi.model.dto.AuthResponse;
import mk.ukim.finki.emt.rentalapi.model.dto.LoginRequest;
import mk.ukim.finki.emt.rentalapi.model.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}