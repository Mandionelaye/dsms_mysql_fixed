# 🚀 Système de Gestion et Synchronisation des Ventes Régionales (Multi-Ventes)

[![Licence](https://img.shields.io/badge/Licence-MIT-blue.svg)](LICENSE)
[![Filière](https://img.shields.io/badge/Master-Ingénierie%20Logicielle-green.svg)](https://www.un-chk.edu.sn/)

## Table des Matières
* [Introduction](#-introduction)
* [Concept Multi-Ventes](#-concept-multi-ventes)
* [Technologies Utilisées](#-technologies-utilisées)
* [Architecture du Projet](#-architecture-du-projet)
* [Fonctionnalités Clés](#-fonctionnalités-clés)
* [Installation et Démarrage](#-installation-et-démarrage)
* [Tests de Validation](#-tests-de-validation)
* [Difficultés Rencontrées](#-difficultés-rencontrées)
* [Perspectives Futures](#-perspectives-futures)
* [Auteurs](#-auteurs)
* [Encadrant](#-encadrant)

---

## 🌟 Introduction

Ce projet a pour objectif la mise en place d'une **architecture de bases de données distribuées** pour la gestion cohérente des ventes réparties sur trois régions : **Dakar, Thiès et Saint-Louis**. Chaque région possède sa propre base de données, mais le système garantit une **synchronisation automatique** et une vue unifiée des données.

L'enjeu principal est d'assurer l'**intégrité des données** en cas de modification simultanée ou de panne temporaire d'un nœud.

### 🎯 Objectif Clé
Assurer une **synchronisation automatique** entre les bases en utilisant un mécanisme de résolution de conflits basé sur le principe du **Last-Write-Wins (LWW)** : la dernière mise à jour enregistrée est considérée comme la plus valide.

---

## 💡 Concept "Multi-Ventes"

Le concept "Multi-Ventes" permet de répartir les opérations de vente sur trois bases régionales (Dakar, Thiès, Saint-Louis), chacune capable d'enregistrer, de modifier et de consulter ses ventes localement.

### Enjeux de la Synchronisation Multi-Bases
* **Cohérence des données** : Garantir que toutes les régions disposent des mêmes informations après chaque opération.
* **Résilience aux pannes** : Permettre aux bases actives de continuer à fonctionner même si un autre nœud est temporairement inaccessible.
* **Gestion des conflits** : Utiliser la stratégie **Last-Write-Wins (LWW)** pour déterminer quelle mise à jour est conservée en cas de modification simultanée.

---

## 🛠️ Technologies Utilisées

Le projet est implémenté en utilisant l'écosystème Java/Spring pour le backend et des technologies web pour l'interface utilisateur.

| Technologie | Rôle |
| :--- | :--- |
| **Spring Boot** | Framework principal de l'application. |
| **Spring Data JPA** | Gestion des entités et de la persistance. |
| **MySQL** | Système de gestion des bases de données. |
| **Java 17** | Langage de programmation principal. |
| **Thymeleaf/HTML/TailwindCSS** | Interface web de l'application. |

---

## 🏗️ Architecture du Projet

Le système repose sur une architecture distribuée simplifiée reliant les trois bases de données régionales (Dakar, Thiès, Saint-Louis) via des services de synchronisation Spring Boot.

### Composants Clés
* `MultiVenteService` : Service central pour les opérations de lecture, ajout et mise à jour des ventes dans les différentes bases. Il applique le mécanisme **LWW**.
* `SyncService` : Responsable de la synchronisation automatique en comparant les champs `updatedAt` pour propager la version la plus récente d'une vente.
* `SyncScheduler` : Composant planificateur qui exécute périodiquement la synchronisation entre les bases pour assurer la cohérence.
* `WebController` : Contrôleur Spring MVC gérant les requêtes HTTP, servant l'interface utilisateur Thymeleaf et gérant la saisie de nouvelles ventes.

### Structure de l'entité `Vente`
Chaque base régionale contient la même entité `Vente` avec les champs suivants:
* `id` (UUID unique)
* `dateVente`
* `montant`
* `produit`
* `region`
* `updatedAt`

---

## ✅ Fonctionnalités Clés

* **Ajout et Consultation** : Les utilisateurs peuvent saisir de nouvelles ventes et consulter l'ensemble des ventes consolidées via l'interface web.
* **Synchronisation Automatique** : Les modifications (nouvelles ventes, mises à jour) sont propagées automatiquement vers toutes les autres bases.
* **Résolution de Conflits LWW** : En cas de modification simultanée, la vente avec la date de mise à jour (`updatedAt`) la plus récente prévaut.
* **Tolérance aux Pannes** : Si une base est indisponible, les ventes ajoutées dans les autres bases sont automatiquement synchronisées dès son redémarrage (synchronisation différée).

---

## ⚙️ Installation et Démarrage

Ce projet nécessite **Java 17+** et un environnement **MySQL** configuré.

### Prérequis
1.  Avoir **Java 17** (ou supérieur) installé.
2.  Avoir **MySQL** installé et en cours d'exécution.
3.  Créer les trois schémas de bases de données régionaux : `ventes_dakar`, `ventes_thies`, et `ventes_stlouis`.

### Étapes de Démarrage
1.  **Cloner le dépôt :**
    ```bash
    git clone https://github.com/Mandionelaye/dsms_mysql_fixed.git
    cd dsms_mysql_fixed
    ```
2.  **Configuration des Datasources :**
    Configurer les URLs, utilisateurs et mots de passe de vos trois bases MySQL dans les fichiers de configuration Spring (gestion multi-datasource).
3.  **Démarrer l'application (Backend Spring Boot) :**
    ```bash
    ./mvnw spring-boot:run
    ```
4.  **Accéder à l'interface web :**
    L'application sera accessible via votre navigateur à l'adresse par défaut : `http://localhost:8080` (sauf configuration différente).

---

## 🧪 Tests de Validation

Le système a été validé à travers plusieurs scénarios de test pour assurer la robustesse du mécanisme de synchronisation.

| Test | Objectif | Résultat | Statut |
| :--- | :--- | :--- | :--- |
| **Test A** | Synchronisation de base à base (fonctionnement normal)  | Réplication entre les 3 bases  | **Réussi**  |
| **Test B** | Conflit et stratégie Last-Write-Wins (LWW) | Application du LWW (la version la plus récente est conservée)  | **Réussi**  |
| **Test C** | Panne et reprise d'une base de données  | La base redémarrée récupère les données manquantes  | **Réussi**  |

---

## ⛔ Difficultés Rencontrées

La mise en place de ce système distribué a soulevé des défis spécifiques:
* **Gestion de la configuration multi-datasource** : Résolu par l'utilisation de fichiers de configuration distincts et des annotations Spring (`@Qualifier`, `@Primary`, etc.).
* **Erreurs de connexion** : Gérées par l'interception automatique des exceptions JDBC par Spring Boot.
* **Synchronisation différée après redémarrage** : Résolu par l'utilisation du `SyncScheduler` qui relance le processus de synchronisation périodiquement dès que la base redevient disponible.

---

## 🗺️ Perspectives Futures

Pour rendre le système plus robuste et évolutif, les améliorations suivantes sont envisagées:
* **Mise en place d'un Bus de Messages** : Intégration de technologies comme **Kafka** ou **RabbitMQ** pour la gestion asynchrone des messages de synchronisation.
* **Monitoring en temps réel** : Ajout d'un système de surveillance pour l'état de santé des nœuds régionaux (Dakar, Thiès, Saint-Louis).
* **Gestion des erreurs personnalisée** : Ajout de blocs `try/catch` personnalisés dans le service de synchronisation pour une gestion plus fine des erreurs de connexion.

---

## 👥 Auteurs

* **Moustapha Faye** 
* **Seydina Madione Mbaye** 
* **Université numérique Cheikh Hamidou KANE** (UN-CHK)
* **Année universitaire :** 2024-2025

## 👨‍🏫 Encadrant
* **Dr Mahamadou TOURE** 
