package com.formation.formation_app.repository;

import com.formation.formation_app.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmployeRepository extends JpaRepository<Employe, Long> {
    Optional<Employe> findByEmailAndMotDePasse(String email, String motDePasse);
    Optional<Employe> findByEmail(String email);
}