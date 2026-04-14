# 🎓 FormaPro Système de Gestion des Formations Professionnelles

[![Java](https://img.shields.io/badge/Java-21-orange)](https://adoptium.net)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green)](https://spring.io/projects/spring-boot)
[![MariaDB](https://img.shields.io/badge/MariaDB-10.11-blue)](https://mariadb.org)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

FormaPro est une application web de gestion des formations professionnelles en entreprise. Elle permet aux employés de consulter le catalogue des formations, de soumettre des demandes d'inscription et de suivre leur parcours de formation. Le responsable dispose d'un espace dédié pour instruire les demandes, gérer les inscriptions et administrer l'ensemble du système.

---

## ✨ Fonctionnalités

### Côté Employé
- 🔍 Consulter le catalogue des formations avec recherche
- 📋 Soumettre une demande de formation avec choix de session
- 📅 Suivre ses inscriptions (statut en temps réel)
- ✖️ Annuler une inscription
- ⭐ Transmettre une appréciation + attestation de présence

### Côté Responsable
- ✔️ Accepter ou refuser les demandes
- 📅 Confirmer ou annuler les inscriptions
- 📝 Évaluer les formations suivies
- ⚙️ Administrer les comptes employés (ajout/suppression)
- 📚 Gérer le catalogue de formations
- 🗓️ Gérer les sessions (ajout/suppression par formation)

---

## 🏗️ Architecture

```
formation-app/
├── src/main/java/com/formation/formation_app/
│   ├── controller/          # Logique métier et gestion des requêtes HTTP
│   │   ├── LoginController.java
│   │   ├── DashboardController.java
│   │   ├── DemandeController.java
│   │   ├── ResponsableController.java
│   │   ├── CatalogueController.java
│   │   ├── EvaluationController.java
│   │   ├── InscriptionController.java
│   │   └── AdminController.java
│   ├── model/               # Entités JPA (tables BDD)
│   │   ├── Employe.java
│   │   ├── Formation.java
│   │   ├── Session.java
│   │   ├── Demande.java
│   │   ├── Inscription.java
│   │   ├── Evaluation.java
│   │   ├── RoleEmploye.java (enum)
│   │   ├── StatutDemande.java (enum)
│   │   └── StatutInscription.java (enum)
│   └── repository/          # Accès base de données (Spring Data JPA)
│       ├── EmployeRepository.java
│       ├── FormationRepository.java
│       ├── SessionRepository.java
│       ├── DemandeRepository.java
│       ├── InscriptionRepository.java
│       └── EvaluationRepository.java
├── src/main/resources/
│   ├── templates/           # Pages HTML (Thymeleaf)
│   │   ├── login.html
│   │   ├── inscription.html
│   │   ├── dashboard.html
│   │   ├── catalogue.html
│   │   ├── demande.html
│   │   ├── demande_detail.html
│   │   ├── session.html
│   │   ├── dashboard_responsable.html
│   │   ├── gestion_demandes.html
│   │   ├── evaluation.html
│   │   └── admin.html
│   ├── static/css/
│   │   └── style.css
│   └── application.properties
├── schema.sql               # Script de création de la base de données
├── pom.xml                  # Dépendances Maven
└── README.md
```

---

## 🚀 Installation rapide

### Prérequis
- Java 21+
- MariaDB 10.6+
- Maven (inclus via `./mvnw`)

### Lancer en local

```bash
# 1. Créer la base de données
mysql -u root -p < schema.sql

# 2. Configurer application.properties avec vos identifiants BDD

# 3. Lancer l'application
./mvnw spring-boot:run

# 4. Ouvrir dans le navigateur
# http://localhost:8081
```

### Comptes de test
| Rôle | Email | Mot de passe |
|------|-------|--------------|
| Employé | erica.toure@entreprise.com | password123 |
| Responsable | rh@entreprise.com | admin123 |

---

## ☁️ Déploiement (Railway)

1. Pousser le projet sur GitHub
2. Créer un compte sur [railway.app](https://railway.app)
3. Nouveau projet → Deploy from GitHub
4. Ajouter un service MySQL
5. Configurer les variables d'environnement :
   - `DATABASE_URL`
   - `DATABASE_USERNAME`
   - `DATABASE_PASSWORD`
6. Railway génère une URL publique automatiquement

---

## 🗄️ Schéma de base de données

```
employes ──< demandes >── formations ──< sessions
                │
                └──< inscriptions
evaluations >── demandes
```

---

## 📁 Flux de travail complet

```
Employé consulte catalogue
        ↓
Employé soumet une demande → Inscription créée (EN_ATTENTE_CONFIRMATION)
        ↓
Responsable instruit la demande (ACCEPTEE / REFUSEE)
        ↓
Responsable confirme l'inscription (CONFIRMEE / ANNULEE)
        ↓
Employé suit la formation
        ↓
Employé transmet appréciation + attestation
        ↓
Responsable évalue la formation
```

---

## 🛠️ Technologies

| Technologie | Version | Usage |
|-------------|---------|-------|
| Java | 21 | Langage principal |
| Spring Boot | 3.5 | Framework web |
| Thymeleaf | 3.1 | Moteur de templates HTML |
| Spring Data JPA | — | Accès base de données |
| MariaDB | 10.11 | Base de données |
| Maven | 3.9 | Gestion des dépendances |

---

## 📄 Licence

Ce projet est développé dans un cadre éducatif. Voir [LICENSE](LICENSE) pour plus de détails.
