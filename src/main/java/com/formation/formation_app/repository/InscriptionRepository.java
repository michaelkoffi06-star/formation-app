package com.formation.formation_app.repository;

import com.formation.formation_app.model.Inscription;
import com.formation.formation_app.model.StatutInscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findByDemandeEmployeId(Long employeId);
    Optional<Inscription> findByDemandeId(Long demandeId);
    List<Inscription> findByStatut(StatutInscription statut);
}
