package com.formation.formation_app.controller;

import com.formation.formation_app.model.Employe;
import com.formation.formation_app.model.RoleEmploye;
import com.formation.formation_app.repository.EmployeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private EmployeRepository employeRepository;

    @GetMapping("/")
    public String afficherLogin() {
        return "login";
    }

    @GetMapping("/login")
    public String afficherLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String traiterLogin(
            @RequestParam String email,
            @RequestParam String motDePasse,
            HttpSession session,
            Model model) {

        Optional<Employe> employe = employeRepository
                .findByEmailAndMotDePasse(email, motDePasse);

        if (employe.isPresent()) {
            session.setAttribute("employe", employe.get());

            if (employe.get().getRole() == RoleEmploye.RESPONSABLE) {
                return "redirect:/responsable/dashboard";
            } else {
                return "redirect:/dashboard";
            }
        } else {
            model.addAttribute("erreur", "Email ou mot de passe incorrect");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}