package com.formation.formation_app.controller;

import com.formation.formation_app.model.*;
import com.formation.formation_app.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/responsable/admin")
public class AdminController {

    @Autowired private EmployeRepository employeRepository;
    @Autowired private FormationRepository formationRepository;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private DemandeRepository demandeRepository;
    @Autowired private InscriptionRepository inscriptionRepository;
    @Autowired private EvaluationRepository evaluationRepository;

    private boolean estResponsable(HttpSession session) {
        Employe e = (Employe) session.getAttribute("employe");
        return e != null && e.getRole() == RoleEmploye.RESPONSABLE;
    }

    // ─── PAGE ADMIN ───────────────────────────────────────────
    @GetMapping("")
    public String afficherAdmin(HttpSession session, Model model,
                                @RequestParam(required = false) String succes,
                                @RequestParam(required = false) String erreur) {
        if (!estResponsable(session)) return "redirect:/";

        List<Formation> formations = formationRepository.findAll();

        // Construire la map formationId -> liste de sessions
        Map<Long, List<com.formation.formation_app.model.Session>> sessionsByFormation = new HashMap<>();
        for (Formation f : formations) {
            sessionsByFormation.put(f.getId(), sessionRepository.findByFormationId(f.getId()));
        }

        model.addAttribute("employes", employeRepository.findAll());
        model.addAttribute("formations", formations);
        model.addAttribute("sessionsByFormation", sessionsByFormation);
        if (succes != null) model.addAttribute("succes", succes);
        if (erreur != null) model.addAttribute("erreur", erreur);
        return "admin";
    }

    // ─── AJOUTER UN EMPLOYÉ ───────────────────────────────────
    @PostMapping("/employes/ajouter")
    public String ajouterEmploye(
            @RequestParam String nom,
            @RequestParam String email,
            @RequestParam String motDePasse,
            @RequestParam String role,
            HttpSession session) {
        if (!estResponsable(session)) return "redirect:/";
        if (employeRepository.findByEmail(email).isPresent())
            return "redirect:/responsable/admin?erreur=Email+d%C3%A9j%C3%A0+utilis%C3%A9";

        Employe emp = new Employe();
        emp.setNom(nom); emp.setEmail(email);
        emp.setMotDePasse(motDePasse);
        emp.setRole(RoleEmploye.valueOf(role));
        employeRepository.save(emp);
        return "redirect:/responsable/admin?succes=Compte+cr%C3%A9%C3%A9";
    }

    // ─── SUPPRIMER UN EMPLOYÉ (cascade manuelle) ──────────────
    @PostMapping("/employes/supprimer/{id}")
    public String supprimerEmploye(@PathVariable Long id, HttpSession session) {
        if (!estResponsable(session)) return "redirect:/";
        try {
            List<Demande> demandes = demandeRepository.findByEmployeId(id);
            for (Demande demande : demandes) {
                inscriptionRepository.findByDemandeId(demande.getId())
                        .ifPresent(inscriptionRepository::delete);
                evaluationRepository.findAll().stream()
                        .filter(e -> e.getDemande().getId().equals(demande.getId()))
                        .forEach(evaluationRepository::delete);
            }
            demandeRepository.deleteAll(demandes);
            employeRepository.deleteById(id);
            return "redirect:/responsable/admin?succes=Compte+supprim%C3%A9";
        } catch (Exception e) {
            return "redirect:/responsable/admin?erreur=Impossible+de+supprimer+ce+compte";
        }
    }

    // ─── AJOUTER UNE FORMATION ────────────────────────────────
    @PostMapping("/formations/ajouter")
    public String ajouterFormation(
            @RequestParam String titre,
            @RequestParam(required = false) String description,
            @RequestParam int dureeJours,
            HttpSession session) {
        if (!estResponsable(session)) return "redirect:/";
        Formation f = new Formation();
        f.setTitre(titre); f.setDescription(description); f.setDureeJours(dureeJours);
        formationRepository.save(f);
        return "redirect:/responsable/admin?succes=Formation+ajout%C3%A9e";
    }

    // ─── SUPPRIMER UNE FORMATION ──────────────────────────────
    @PostMapping("/formations/supprimer/{id}")
    public String supprimerFormation(@PathVariable Long id, HttpSession session) {
        if (!estResponsable(session)) return "redirect:/";
        try {
            formationRepository.deleteById(id);
            return "redirect:/responsable/admin?succes=Formation+supprim%C3%A9e";
        } catch (Exception e) {
            return "redirect:/responsable/admin?erreur=Impossible+%28des+demandes+sont+li%C3%A9es+%C3%A0+cette+formation%29";
        }
    }

    // ─── AJOUTER UNE SESSION ──────────────────────────────────
    @PostMapping("/sessions/ajouter")
    public String ajouterSession(
            @RequestParam Long formationId,
            @RequestParam String dateDebut,
            @RequestParam String dateFin,
            HttpSession session) {
        if (!estResponsable(session)) return "redirect:/";
        try {
            Formation formation = formationRepository.findById(formationId)
                    .orElseThrow(() -> new RuntimeException("Formation introuvable"));

            com.formation.formation_app.model.Session s = new com.formation.formation_app.model.Session();
            s.setFormation(formation);
            s.setDateDebut(LocalDate.parse(dateDebut));
            s.setDateFin(LocalDate.parse(dateFin));
            sessionRepository.save(s);
            return "redirect:/responsable/admin?succes=Session+ajout%C3%A9e&tab=sessions";
        } catch (Exception e) {
            return "redirect:/responsable/admin?erreur=Erreur+lors+de+l%27ajout+de+la+session&tab=sessions";
        }
    }

    // ─── SUPPRIMER UNE SESSION ────────────────────────────────
    @PostMapping("/sessions/supprimer/{id}")
    public String supprimerSession(@PathVariable Long id, HttpSession session) {
        if (!estResponsable(session)) return "redirect:/";
        try {
            sessionRepository.deleteById(id);
            return "redirect:/responsable/admin?succes=Session+supprim%C3%A9e&tab=sessions";
        } catch (Exception e) {
            return "redirect:/responsable/admin?erreur=Impossible+%28des+inscriptions+sont+li%C3%A9es+%C3%A0+cette+session%29&tab=sessions";
        }
    }
}