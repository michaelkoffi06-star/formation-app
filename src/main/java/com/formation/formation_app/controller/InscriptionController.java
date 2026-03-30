package com.formation.formation_app.controller;

import com.formation.formation_app.model.Employe;
import com.formation.formation_app.model.RoleEmploye;
import com.formation.formation_app.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class InscriptionController {

    @Autowired
    private EmployeRepository employeRepository;

    @GetMapping("/inscription")
    public String afficherInscription() {
        return "inscription";
    }

    @PostMapping("/inscription")
    public String traiterInscription(
            @RequestParam String nom,
            @RequestParam String email,
            @RequestParam String motDePasse,
            Model model) {

        // Vérifier si l'email est déjà utilisé
        Optional<Employe> existant = employeRepository.findByEmail(email);
        if (existant.isPresent()) {
            model.addAttribute("erreur", "Cet email est déjà utilisé.");
            return "inscription";
        }

        Employe employe = new Employe();
        employe.setNom(nom);
        employe.setEmail(email);
        employe.setMotDePasse(motDePasse);
        employe.setRole(RoleEmploye.EMPLOYE); // Toujours employé par défaut

        employeRepository.save(employe);

        model.addAttribute("succes", "Compte créé avec succès ! Vous pouvez vous connecter.");
        return "inscription";
    }
}
