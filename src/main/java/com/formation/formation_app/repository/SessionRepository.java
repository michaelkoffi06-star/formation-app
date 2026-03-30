package com.formation.formation_app.repository;

import com.formation.formation_app.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    // Récupère toutes les sessions d'une formation donnée
    List<Session> findByFormationId(Long formationId);
}
