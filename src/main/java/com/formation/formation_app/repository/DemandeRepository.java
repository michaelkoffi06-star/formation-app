package com.formation.formation_app.repository;

import com.formation.formation_app.model.Demande;
import com.formation.formation_app.model.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
    List<Demande> findByEmployeId(Long employeId);
    long countByStatut(StatutDemande statut);
    List<Demande> findByStatut(StatutDemande statut);
}