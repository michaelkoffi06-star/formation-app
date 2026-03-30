package com.formation.formation_app.controller;

import com.formation.formation_app.model.Demande;
import com.formation.formation_app.model.StatutDemande;
import com.formation.formation_app.repository.DemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/responsable")
public class ResponsableController {

    @Autowired
    private DemandeRepository demandeRepository;

    // ─────────────────────────────────────────
    // GET /responsable/dashboard
    // Affiche le tableau de bord du responsable
    // ─────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long enAttente = demandeRepository.countByStatut(StatutDemande.EN_ATTENTE);
        long acceptees = demandeRepository.countByStatut(StatutDemande.ACCEPTEE);
        long refusees  = demandeRepository.countByStatut(StatutDemande.REFUSEE);
        long total     = demandeRepository.count();

        model.addAttribute("enAttente", enAttente);
        model.addAttribute("acceptees", acceptees);
        model.addAttribute("refusees",  refusees);
        model.addAttribute("total",     total);

        return "dashboard_responsable";
    }

    // ─────────────────────────────────────────
    // GET /responsable/demandes
    // Affiche toutes les demandes
    // ─────────────────────────────────────────
    @GetMapping("/demandes")
    public String gestionDemandes(Model model) {
        List<Demande> toutes = demandeRepository.findAll();
        model.addAttribute("demandes", toutes);
        return "gestion_demandes";
    }

    // ─────────────────────────────────────────
    // POST /responsable/demandes/{id}/accepter
    // Accepte une demande
    // ─────────────────────────────────────────
    @PostMapping("/demandes/{id}/accepter")
    public String accepterDemande(@PathVariable Long id) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande introuvable : " + id));
        demande.setStatut(StatutDemande.ACCEPTEE);
        demandeRepository.save(demande);
        return "redirect:/responsable/demandes";
    }

    // ─────────────────────────────────────────
    // POST /responsable/demandes/{id}/refuser
    // Refuse une demande
    // ─────────────────────────────────────────
    @PostMapping("/demandes/{id}/refuser")
    public String refuserDemande(@PathVariable Long id) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande introuvable : " + id));
        demande.setStatut(StatutDemande.REFUSEE);
        demandeRepository.save(demande);
        return "redirect:/responsable/demandes";
    }
}
