package com.polytechnique.backend.service;

import com.polytechnique.backend.dto.request.AdministrateurRequestDTO;
import com.polytechnique.backend.dto.request.ChangePasswordRequestDTO;
import com.polytechnique.backend.dto.request.LoginRequestDTO;
import com.polytechnique.backend.dto.response.AdministrateurResponseDTO;

import java.util.List;

public interface AdministrateurService {
    AdministrateurResponseDTO createAdministrateur(AdministrateurRequestDTO requestDTO);

    AdministrateurResponseDTO getAdministrateurById(int id);

    List<AdministrateurResponseDTO> getAllAdministrateurs();

    AdministrateurResponseDTO updateAdministrateur(int id, AdministrateurRequestDTO requestDTO);

    void deleteAdministrateur(int id);

    AdministrateurResponseDTO login(LoginRequestDTO loginRequest);

    void updatePassword(int id, ChangePasswordRequestDTO changePasswordRequest);

    AdministrateurResponseDTO updateStatut(int id, String statut);

    AdministrateurResponseDTO updateLockTimeout(int id, int timeout);
}
