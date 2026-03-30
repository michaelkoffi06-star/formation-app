package com.formation.formation_app.controller;

import com.formation.formation_app.model.Demande;
import com.formation.formation_app.model.Employe;
import com.formation.formation_app.model.StatutDemande;
import com.formation.formation_app.repository.DemandeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private DemandeRepository demandeRepository;

    @GetMapping("/dashboard")
    public String afficherDashboard(HttpSession session, Model model) {

        Employe employe = (Employe) session.getAttribute("employe");
        if (employe == null) {
            return "redirect:/";
        }

        List<Demande> demandes = demandeRepository.findByEmployeId(employe.getId());

        long total     = demandes.size();
        long acceptees = demandes.stream().filter(d -> d.getStatut() == StatutDemande.ACCEPTEE).count();
        long enAttente = demandes.stream().filter(d -> d.getStatut() == StatutDemande.EN_ATTENTE).count();
        long refusees  = demandes.stream().filter(d -> d.getStatut() == StatutDemande.REFUSEE).count();

        model.addAttribute("employe", employe);
        model.addAttribute("demandes", demandes);
        model.addAttribute("total", total);
        model.addAttribute("acceptees", acceptees);
        model.addAttribute("enAttente", enAttente);
        model.addAttribute("refusees", refusees);

        return "dashboard";
    }
}