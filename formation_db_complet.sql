/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.11.15-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: formation_db
-- ------------------------------------------------------
-- Server version	10.11.15-MariaDB-deb12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `demandes`
--

DROP TABLE IF EXISTS `demandes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `demandes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `employe_id` int(11) NOT NULL,
  `formation_id` int(11) NOT NULL,
  `statut` enum('EN_ATTENTE','ACCEPTEE','REFUSEE') DEFAULT 'EN_ATTENTE',
  `justification` varchar(255) DEFAULT NULL,
  `date_demande` date DEFAULT curdate(),
  PRIMARY KEY (`id`),
  KEY `employe_id` (`employe_id`),
  KEY `formation_id` (`formation_id`),
  CONSTRAINT `demandes_ibfk_1` FOREIGN KEY (`employe_id`) REFERENCES `employes` (`id`),
  CONSTRAINT `demandes_ibfk_2` FOREIGN KEY (`formation_id`) REFERENCES `formations` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `demandes`
--

LOCK TABLES `demandes` WRITE;
/*!40000 ALTER TABLE `demandes` DISABLE KEYS */;
INSERT INTO `demandes` VALUES
(1,1,1,'ACCEPTEE','Je souhaite approfondir mes compétences en Anglais Professionnel','2026-03-20'),
(2,1,2,'ACCEPTEE','Besoin pour mon projet en cours','2026-03-20'),
(4,1,2,'REFUSEE','                ','2026-03-29'),
(5,1,1,'REFUSEE','                ','2026-03-29'),
(6,1,2,'REFUSEE','                ','2026-03-29'),
(12,9,2,'ACCEPTEE','Je dois effectuer une mission qui nécessite des compétence en JAVA Web.','2026-03-27'),
(13,9,1,'ACCEPTEE','                ','2026-03-27'),
(14,9,1,'ACCEPTEE','                ','2026-03-27'),
(15,9,1,'REFUSEE','                ','2026-03-27'),
(16,9,2,'REFUSEE','                ','2026-03-27'),
(17,1,2,'ACCEPTEE','                ','2026-03-30'),
(18,1,4,'ACCEPTEE','                ','2026-03-30');
/*!40000 ALTER TABLE `demandes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employes`
--

DROP TABLE IF EXISTS `employes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `employes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mot_de_passe` varchar(255) NOT NULL,
  `role` enum('EMPLOYE','RESPONSABLE') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employes`
--

LOCK TABLES `employes` WRITE;
/*!40000 ALTER TABLE `employes` DISABLE KEYS */;
INSERT INTO `employes` VALUES
(1,'Erica Touré','erica@test.com','1234','EMPLOYE'),
(3,'Michael Koffi','michael@test.com','1234','RESPONSABLE'),
(6,'Soro Fabrice','soro@gmail.com','1234','EMPLOYE'),
(9,'Tuo Kévin','kevin@mail.com','123','EMPLOYE');
/*!40000 ALTER TABLE `employes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluations`
--

DROP TABLE IF EXISTS `evaluations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `demande_id` int(11) NOT NULL,
  `note` int(11) DEFAULT NULL CHECK (`note` between 1 and 5),
  `commentaire` varchar(255) DEFAULT NULL,
  `date_evaluation` date DEFAULT curdate(),
  `fichier` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `demande_id` (`demande_id`),
  CONSTRAINT `evaluations_ibfk_1` FOREIGN KEY (`demande_id`) REFERENCES `demandes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluations`
--

LOCK TABLES `evaluations` WRITE;
/*!40000 ALTER TABLE `evaluations` DISABLE KEYS */;
INSERT INTO `evaluations` VALUES
(1,14,4,'Teste de connaissance','2026-03-30',''),
(2,12,4,'Teste de connaissance','2026-03-30','12_Virtualistionexam2.pdf'),
(3,14,3,'tesy','2026-03-30','14_Virtualistionexam2.pdf'),
(4,17,4,'eeyyy','2026-03-30','17_VENTINES.docx'),
(5,18,4,'','2026-03-30','18_Virtualistionexam2.pdf');
/*!40000 ALTER TABLE `evaluations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `formations`
--

DROP TABLE IF EXISTS `formations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `formations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `titre` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `duree_jours` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formations`
--

LOCK TABLES `formations` WRITE;
/*!40000 ALTER TABLE `formations` DISABLE KEYS */;
INSERT INTO `formations` VALUES
(1,'Anglais','Formation en Anglais Profesionnel',30),
(2,'Java pour le Web','Programmation Java Web',30),
(3,'Python Data Science','Analyse de données',60),
(4,'Formation Art Oratoire','développer vos capacités à communiquer',28);
/*!40000 ALTER TABLE `formations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inscriptions`
--

DROP TABLE IF EXISTS `inscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `inscriptions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appreciation` varchar(255) DEFAULT NULL,
  `date_inscription` date DEFAULT NULL,
  `document_uploade` varchar(255) DEFAULT NULL,
  `statut` enum('ANNULEE','CONFIRMEE','EN_ATTENTE_CONFIRMATION') DEFAULT NULL,
  `demande_id` bigint(20) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK45ict5gy5rr2v5mp0od9r9n06` (`session_id`),
  CONSTRAINT `FK45ict5gy5rr2v5mp0od9r9n06` FOREIGN KEY (`session_id`) REFERENCES `sessions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inscriptions`
--

LOCK TABLES `inscriptions` WRITE;
/*!40000 ALTER TABLE `inscriptions` DISABLE KEYS */;
INSERT INTO `inscriptions` VALUES
(1,NULL,'2026-03-30',NULL,'EN_ATTENTE_CONFIRMATION',17,3),
(2,NULL,'2026-03-30',NULL,'EN_ATTENTE_CONFIRMATION',18,4);
/*!40000 ALTER TABLE `inscriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `sessions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `formation_id` int(11) NOT NULL,
  `date_debut` date NOT NULL,
  `date_fin` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `formation_id` (`formation_id`),
  CONSTRAINT `sessions_ibfk_1` FOREIGN KEY (`formation_id`) REFERENCES `formations` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
INSERT INTO `sessions` VALUES
(1,1,'2026-06-01','2026-07-31'),
(2,1,'2026-07-10','2025-08-07'),
(3,2,'2026-06-10','2025-08-10'),
(4,4,'2026-03-31','2026-04-25');
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-31  8:09:02
