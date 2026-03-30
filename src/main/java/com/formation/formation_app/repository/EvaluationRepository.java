package com.formation.formation_app.repository;

import com.formation.formation_app.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    // Vérifie si une évaluation existe déjà pour une demande donnée
    boolean existsByDemandeId(Long demandeId);
}
