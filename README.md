# dsms_mysql_fixed 📦  
![Licence](https://img.shields.io/badge/licence-MIT-blue.svg)  

## Description du projet  
Ce projet **dsms_mysql_fixed** vise à apporter des corrections et améliorations à une base de données MySQL dédiée à un système de gestion (probablement « DSMS ») afin d’optimiser les requêtes, garantir l’intégrité des données et faciliter un usage plus stable en production. Il fournit une structure SQL corrigée, des scripts de migration ainsi qu’une documentation pour faciliter le déploiement.  

## Fonctionnalités clés  
- 🛠️ Correction des schémas MySQL pour résoudre des anomalies persistantes  
- 🧮 Optimisation des requêtes et indexation pour un meilleur rendement  
- 🔄 Scripts de migration pour mettre à jour les anciennes versions de la base  
- 📁 Structure claire des fichiers SQL & documentation pour une maintenance facilitée  

## Technologies utilisées  
- MySQL / MariaDB (SQL)  
- Fichiers SQL (.sql) pour les schémas, données et migrations  
- [Éventuellement] PHP ou autre langage pour les scripts (à confirmer)  
- Git pour le versioning  

## Installation  
Voici les étapes pour configurer ce projet en local (ou en environnement de test) :  
```bash
# 1. Cloner le dépôt
git clone https://github.com/Mandionelaye/dsms_mysql_fixed.git
cd dsms_mysql_fixed

# 2. Importer la base de données
mysql -u [UTILISATEUR] -p [NOM_DE_LA_BASE] < chemin/vers/schema.sql

# 3. Exécuter les scripts de migration (si disponibles)
mysql -u [UTILISATEUR] -p [NOM_DE_LA_BASE] < chemin/vers/migration-script.sql

# 4. Mettre à jour votre configuration (hôte, port, utilisateur, mot de passe)
# Éditez le fichier [config.sql | config.php | .env] selon vos besoins
