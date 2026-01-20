package com.polytechnique.backend.repository;

import com.polytechnique.backend.entity.InfirmierLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfirmierLocalRepository extends JpaRepository<InfirmierLocal, Integer> {
    List<InfirmierLocal> findByZoneAffectation(String zoneAffectation);

    List<InfirmierLocal> findByStatut(String statut);

    List<InfirmierLocal> findByAdministrateurId(Integer id);
}
