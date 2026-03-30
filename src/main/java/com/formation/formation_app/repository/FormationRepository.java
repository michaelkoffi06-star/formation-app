package com.formation.formation_app.repository;

import com.formation.formation_app.model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FormationRepository extends JpaRepository<Formation, Long> {

    // Recherche par titre (insensible à la casse)
    List<Formation> findByTitreContainingIgnoreCase(String titre);
}
