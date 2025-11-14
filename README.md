Projet DSMS (corrigé pour MySQL)

Pré-requis :
- Java 21
- Maven
- MySQL (bases: ventes_dakar, ventes_thies, ventes_stlouis)
- L'utilisateur MySQL 'dsms_user' avec mot de passe 'dsms_pass' et droits sur les 3 bases.

Commandes :
mvn clean package
mvn spring-boot:run

Endpoints :
GET  /api/ventes
POST /api/ventes
GET  /api/ventes/region/{region}
GET  /api/ventes/total
