package com.formation.formation_app.controller;

import com.formation.formation_app.model.Employe;
import com.formation.formation_app.model.Formation;
import com.formation.formation_app.repository.FormationRepository;
import com.formation.formation_app.repository.SessionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CatalogueController {

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping("/catalogue")
    public String afficherCatalogue(
            @RequestParam(required = false) String recherche,
            HttpSession session,
            Model model) {

        // Vérifier que l'employé est connecté
        Employe employe = (Employe) session.getAttribute("employe");
        if (employe == null) {
            return "redirect:/";
        }

        // Récupérer les formations (avec ou sans recherche)
        List<Formation> formations;
        if (recherche != null && !recherche.isEmpty()) {
            formations = formationRepository.findByTitreContainingIgnoreCase(recherche);
        } else {
            formations = formationRepository.findAll();
        }

        // Pour chaque formation, calculer le nombre de sessions disponibles
        // et l'ajouter dans la vue via un attribut de la formation
        // On passe aussi les sessions au modèle pour que Thymeleaf puisse
        // afficher "X session(s)" sur chaque carte
        model.addAttribute("employe", employe);
        model.addAttribute("formations", formations);
        model.addAttribute("recherche", recherche);

        // Compter les sessions par formation (Map : formationId -> nbSessions)
        java.util.Map<Long, Long> nbSessionsParFormation = new java.util.HashMap<>();
        for (Formation f : formations) {
            long nb = sessionRepository.findByFormationId(f.getId()).size();
            nbSessionsParFormation.put(f.getId(), nb);
        }
        model.addAttribute("nbSessionsParFormation", nbSessionsParFormation);

        return "catalogue";
    }
}
