package com.formation.formation_app.controller;

import com.formation.formation_app.model.Demande;
import com.formation.formation_app.model.Evaluation;
import com.formation.formation_app.repository.DemandeRepository;
import com.formation.formation_app.repository.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Controller
@RequestMapping("/responsable")
public class EvaluationController {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    // Dossier où les fichiers uploadés seront sauvegardés
    private static final String UPLOAD_DIR = "uploads/";

    // ─────────────────────────────────────────
    // GET /responsable/evaluation/{demandeId}
    // Affiche le formulaire d'évaluation
    // ─────────────────────────────────────────
    @GetMapping("/evaluation/{demandeId}")
    public String formulaireEvaluation(@PathVariable Long demandeId, Model model) {
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable : " + demandeId));

        model.addAttribute("demande", demande);
        model.addAttribute("evaluation", new Evaluation());
        return "evaluation";
    }

    // ─────────────────────────────────────────
    // POST /responsable/evaluation
    // Enregistre l'évaluation + fichier uploadé
    // ─────────────────────────────────────────
    @PostMapping("/evaluation")
    public String sauvegarderEvaluation(
            @RequestParam("demandeId") Long demandeId,
            @RequestParam("note") int note,
            @RequestParam("commentaire") String commentaire,
            @RequestParam("fichier") MultipartFile fichier) throws IOException {

        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable : " + demandeId));

        // Sauvegarde du fichier sur le disque
        String nomFichier = "";
        if (!fichier.isEmpty()) {
            nomFichier = demandeId + "_" + fichier.getOriginalFilename();
            Path chemin = Paths.get(UPLOAD_DIR + nomFichier);
            Files.createDirectories(chemin.getParent());
            Files.write(chemin, fichier.getBytes());
        }

        // Création et sauvegarde de l'évaluation
        Evaluation evaluation = new Evaluation();
        evaluation.setDemande(demande);
        evaluation.setNote(note);
        evaluation.setCommentaire(commentaire);
        evaluation.setFichier(nomFichier);

        evaluationRepository.save(evaluation);

        return "redirect:/responsable/demandes";
    }
}
