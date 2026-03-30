package com.formation.formation_app.controller;

import com.formation.formation_app.model.*;
import com.formation.formation_app.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class DemandeController {

    @Autowired private DemandeRepository demandeRepository;
    @Autowired private FormationRepository formationRepository;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private InscriptionRepository inscriptionRepository;

    // -------------------------------------------------------
    // Formulaire de demande
    // -------------------------------------------------------
    @GetMapping("/demande/new")
    public String afficherFormulairedemande(
            @RequestParam Long formationId,
            HttpSession session,
            Model model) {

        Employe employe = (Employe) session.getAttribute("employe");
        if (employe == null) return "redirect:/";

        Optional<Formation> formation = formationRepository.findById(formationId);
        if (formation.isEmpty()) return "redirect:/catalogue";

        List<Session> sessions = sessionRepository.findByFormationId(formationId);
        model.addAttribute("employe", employe);
        model.addAttribute("formation", formation.get());
        model.addAttribute("sessions", sessions);
        return "demande";
    }

    // -------------------------------------------------------
    // Soumettre une demande → crée Demande + Inscription
    // -------------------------------------------------------
    @PostMapping("/demande/soumettre")
    public String soumettredemande(
            @RequestParam Long formationId,
            @RequestParam Long sessionId,
            @RequestParam(required = false) String commentaire,
            HttpSession session,
            Model model) {

        Employe employe = (Employe) session.getAttribute("employe");
        if (employe == null) return "redirect:/";

        Optional<Formation> formation = formationRepository.findById(formationId);
        Optional<Session> sessionFormation = sessionRepository.findById(sessionId);

        if (formation.isEmpty() || sessionFormation.isEmpty()) {
            model.addAttribute("erreur", "Formation ou session introuvable.");
            return "demande";
        }

        // Créer la Demande
        Demande demande = new Demande();
        demande.setEmploye(employe);
        demande.setFormation(formation.get());
        demande.setStatut(StatutDemande.EN_ATTENTE);
        demande.setJustification(commentaire);
        demande.setDateDemande(LocalDate.now());
        demandeRepository.save(demande);

        // Créer l'Inscription liée
        Inscription inscription = new Inscription();
        inscription.setDemande(demande);
        inscription.setSession(sessionFormation.get());
        inscription.setDateInscription(LocalDate.now());
        inscription.setStatut(StatutInscription.EN_ATTENTE_CONFIRMATION);
        inscriptionRepository.save(inscription);

        return "redirect:/mes-demandes?succes=true";
    }

    // -------------------------------------------------------
    // Mes demandes
    // -------------------------------------------------------
    @GetMapping("/mes-demandes")
    public String afficherMesDemandes(
            @RequestParam(required = false) String succes,
            HttpSession session,
            Model model) {

        Employe employe = (Employe) session.getAttribute("employe");
        if (employe == null) return "redirect:/";

        List<Demande> demandes = demandeRepository.findByEmployeId(employe.getId());
        List<Inscription> inscriptions = inscriptionRepository.findByDemandeEmployeId(employe.getId());

        long nbTotal    = demandes.size();
        long nbAttente  = demandes.stream().filter(d -> d.getStatut() == StatutDemande.EN_ATTENTE).count();
        long nbAcceptee = demandes.stream().filter(d -> d.getStatut() == StatutDemande.ACCEPTEE).count();
        long nbRefusee  = demandes.stream().filter(d -> d.getStatut() == StatutDemande.REFUSEE).count();

        model.addAttribute("employe", employe);
        model.addAttribute("demandes", demandes);
        model.addAttribute("inscriptions", inscriptions);
        model.addAttribute("nbTotal", nbTotal);
        model.addAttribute("nbAttente", nbAttente);
        model.addAttribute("nbAcceptee", nbAcceptee);
        model.addAttribute("nbRefusee", nbRefusee);

        if ("true".equals(succes))
            model.addAttribute("succes", "Votre demande a bien été envoyée !");

        return "session";
    }

    // -------------------------------------------------------
    // Détail d'une demande
    // -------------------------------------------------------
    @GetMapping("/demande/detail/{id}")
    public String afficherDetailDemande(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        Employe employe = (Employe) session.getAttribute("employe");
        if (employe == null) return "redirect:/";

        Optional<Demande> demande = demandeRepository.findById(id);
        if (demande.isEmpty()) return "redirect:/mes-demandes";

        // Chercher l'inscription associée si elle existe
        Optional<Inscription> inscription = inscriptionRepository.findByDemandeId(id);

        model.addAttribute("employe", employe);
        model.addAttribute("demande", demande.get());
        inscription.ifPresent(i -> model.addAttribute("inscription", i));
        return "demande_detail";
    }

    // -------------------------------------------------------
    // Annuler une inscription (par l'employé)
    // -------------------------------------------------------
    @PostMapping("/inscription/annuler/{id}")
    public String annulerInscription(
            @PathVariable Long id,
            HttpSession session) {

        Employe employe = (Employe) session.getAttribute("employe");
        if (employe == null) return "redirect:/";

        Optional<Inscription> inscription = inscriptionRepository.findById(id);
        if (inscription.isPresent()) {
            inscription.get().setStatut(StatutInscription.ANNULEE);
            inscriptionRepository.save(inscription.get());
        }
        return "redirect:/mes-demandes";
    }

    // -------------------------------------------------------
    // Soumettre appréciation + document (par l'employé)
    // -------------------------------------------------------
    @PostMapping("/inscription/appreciation/{id}")
    public String soumettreAppreciation(
            @PathVariable Long id,
            @RequestParam String appreciation,
            @RequestParam(required = false) org.springframework.web.multipart.MultipartFile document,
            HttpSession session) throws java.io.IOException {

        Employe employe = (Employe) session.getAttribute("employe");
        if (employe == null) return "redirect:/";

        Optional<Inscription> opt = inscriptionRepository.findById(id);
        if (opt.isPresent()) {
            Inscription inscription = opt.get();
            inscription.setAppreciation(appreciation);

            if (document != null && !document.isEmpty()) {
                String nomFichier = id + "_" + document.getOriginalFilename();
                java.nio.file.Path chemin = java.nio.file.Paths.get("uploads/" + nomFichier);
                java.nio.file.Files.createDirectories(chemin.getParent());
                java.nio.file.Files.write(chemin, document.getBytes());
                inscription.setDocumentUploade(nomFichier);
            }
            inscriptionRepository.save(inscription);
        }
        return "redirect:/mes-demandes";
    }
}