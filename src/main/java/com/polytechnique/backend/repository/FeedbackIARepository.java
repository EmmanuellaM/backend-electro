package com.polytechnique.backend.repository;

import com.polytechnique.backend.entity.FeedbackIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour les feedbacks IA
 */
@Repository
public interface FeedbackIARepository extends JpaRepository<FeedbackIA, Integer> {

    List<FeedbackIA> findByMedecinId(Integer medecinId);

    List<FeedbackIA> findByParametresId(Integer parametresId);

    List<FeedbackIA> findByClassePredite(String classePredite);

    java.util.Optional<FeedbackIA> findByMedecinIdAndParametresId(Integer medecinId, Integer parametresId);

    long countByNoteMedecinGreaterThanEqual(Integer note);
}
