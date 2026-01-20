package com.polytechnique.backend.service;

import com.polytechnique.backend.dto.request.InfirmierLocalRequestDTO;
import com.polytechnique.backend.dto.response.InfirmierLocalResponseDTO;

import java.util.List;

public interface InfirmierLocalService {
    InfirmierLocalResponseDTO createInfirmier(InfirmierLocalRequestDTO requestDTO);

    InfirmierLocalResponseDTO getInfirmierById(int id);

    List<InfirmierLocalResponseDTO> getAllInfirmiers(Integer adminId);

    InfirmierLocalResponseDTO updateInfirmier(int id, InfirmierLocalRequestDTO requestDTO);

    void deleteInfirmier(int id);

    InfirmierLocalResponseDTO updateStatut(int id, String statut);
}
