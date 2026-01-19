package com.polytechnique.backend.service;

import com.polytechnique.backend.dto.request.LoginRequestDTO;
import com.polytechnique.backend.dto.response.LoginResponseDTO;

public interface AuthService {
    /**
     * Login unifié - recherche dans Administrateur puis Medecin
     * 
     * @param loginRequest email et mot de passe
     * @return LoginResponseDTO avec le rôle (admin ou medecin)
     */
    LoginResponseDTO login(LoginRequestDTO loginRequest);

    void initiatePasswordReset(String email);

    boolean verifyResetToken(String email, String token);

    void resetPassword(String email, String token, String newPassword);
}
