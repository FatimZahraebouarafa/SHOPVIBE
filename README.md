# ShopVibe — Application e-commerce

ShopVibe est une application web e-commerce développée avec Java et Spring Boot. Elle permet de consulter un catalogue de produits, de créer un compte, de se connecter, de gérer un panier et de passer des commandes avec différents moyens de paiement.

Le projet est une application monolithique : le backend Spring Boot sert également le frontend Thymeleaf et les ressources statiques.

## Fonctionnalités

- Page d'accueil avec carousel promotionnel.
- Catalogue des articles disponibles.
- Recherche par mot-clé.
- Filtrage des articles par marque.
- Page de détail d'un article.
- Inscription et authentification des utilisateurs.
- Gestion du panier.
- Création et consultation des commandes.
- Paiement par PayPal ou carte bancaire.
- Espace d'administration pour gérer les articles.
- Gestion du stock lors des achats et des annulations.
- Protection des formulaires avec CSRF.
- Autorisation des pages d'administration avec le rôle `ROLE_ADMIN`.

## Technologies utilisées

### Backend

- Java 17.
- Spring Boot 4.0.6.
- Spring MVC pour les contrôleurs web.
- Spring Data JPA et Hibernate pour la persistance.
- Spring Security pour l'authentification, les rôles et la protection CSRF.
- Maven pour la gestion du projet et des dépendances.
- MySQL 8 pour la base de données en environnement normal.
- H2 pour les tests automatisés.

### Frontend

- HTML5.
- Thymeleaf pour le rendu serveur des pages dynamiques.
- CSS3 avec le fichier principal `style.css`.
- Bootstrap 5 pour la grille responsive et certains composants.
- JavaScript Bootstrap pour le carousel et les interactions.
- Images locales dans `src/main/resources/static/images`.

### Qualité et automatisation

- JUnit 5.
- Mockito pour les tests unitaires.
- MockMvc pour les tests HTTP et API.
- Spring Security Test pour les tests de sécurité.
- Checkstyle pour le linting Java.
- Docker et Docker Compose.
- GitHub Actions pour la CI.

## Architecture

```text
Navigateur
    |
    v
Spring Boot
    |-- Controllers
    |-- Services
    |-- Repositories
    |-- Models / JPA Entities
    |-- Spring Security
    |-- Thymeleaf templates
    |
    v
MySQL
```

Les pages Thymeleaf sont rendues côté serveur. Il n'y a pas de frontend React, Angular ou Vue séparé.

## Structure du projet

```text
src/
├── main/
│   ├── java/com/ecommerce/
│   │   ├── controller/       Contrôleurs web
│   │   ├── model/            Entités JPA
│   │   ├── repository/       Accès aux données
│   │   ├── security/         Configuration Spring Security
│   │   └── service/          Logique métier
│   └── resources/
│       ├── templates/        Pages Thymeleaf
│       ├── static/css/       Feuilles de style
│       ├── static/images/    Images des produits et du carousel
│       ├── application.properties
│       ├── schema.sql
│       └── data.sql
└── test/
    ├── java/                 Tests unitaires et d'intégration
    └── resources/            Configuration H2 des tests
```

## Prérequis

Pour un lancement local :

- JDK 17 ou supérieur.
- MySQL 8.
- PowerShell, Bash ou un terminal compatible.

Pour Docker :

- Docker Desktop.
- Docker Compose v2.

## Configuration MySQL locale

Créez la base de données avec MySQL :

```sql
CREATE DATABASE IF NOT EXISTS dbVentes
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

Configurez ensuite les identifiants dans `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dbVentes?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=VOTRE_MOT_DE_PASSE
```

Pour un environnement réel, utilisez de préférence un utilisateur MySQL dédié plutôt que `root`.

## Lancement en local

Depuis la racine du projet :

```powershell
.\mvnw.cmd spring-boot:run
```

L'application est disponible à l'adresse :

<http://localhost:8080>

Le catalogue est accessible à :

<http://localhost:8080/articles>

## Lancement avec Docker Compose

Copiez le fichier d'exemple de variables :

```powershell
Copy-Item .env.example .env
```

Modifiez ensuite `.env` :

```env
MYSQL_ROOT_PASSWORD=un-mot-de-passe-fort
APP_PORT=8080
MYSQL_PORT=3306
```

Construisez et démarrez les services :

```powershell
docker compose up -d --build
```

Docker Compose démarre :

- `ecommerce-app` : application Spring Boot, frontend Thymeleaf et backend ;
- `ecommerce-mysql` : base de données MySQL 8.

L'application attend que le healthcheck MySQL soit valide avant de démarrer.

Commandes utiles :

```powershell
docker compose ps
docker compose logs -f app
docker compose logs -f mysql
docker compose down
```

Les données MySQL sont conservées dans le volume `mysql_data`. Pour supprimer les conteneurs et les données :

```powershell
docker compose down -v
```

Le script `docker/mysql/init/01-seed-articles.sql` insère les articles de démonstration lors de la création initiale du volume MySQL.

## Tests

La suite de tests utilise H2 en mémoire et ne nécessite pas de serveur MySQL.

Exécuter tous les tests :

```powershell
.\mvnw.cmd -B test
```

La couverture actuelle comprend :

- tests unitaires de `ArticleService` avec Mockito ;
- tests d'intégration du catalogue et du détail article ;
- tests d'authentification ;
- test de protection d'une route privée ;
- test de rejet d'une requête sans token CSRF ;
- test de chargement du contexte Spring.

## Linting Checkstyle

Exécuter Checkstyle :

```powershell
.\mvnw.cmd -B checkstyle:check
```

Le plugin est configuré pour signaler les violations sans bloquer la compilation. Les règles existantes peuvent encore signaler des problèmes de formatage ou de Javadoc dans le code historique.

## CI GitHub Actions

Le workflow [.github/workflows/deploy.yml](.github/workflows/deploy.yml) s'exécute sur les branches `main`, `master` et `develop`, ainsi que sur les pull requests.

Il :

1. installe Java 17 ;
2. exécute les tests Maven ;
3. exécute Checkstyle ;
4. construit l'image Docker si les tests réussissent.

L'image Docker est construite pour la CI mais n'est pas publiée dans un registre.

## Sécurité

- Les mots de passe applicatifs sont encodés avec BCrypt.
- Les requêtes POST sont protégées par CSRF, sauf les routes explicitement configurées.
- Les pages `/admin/**` nécessitent le rôle `ADMIN`.
- Les ressources publiques sont limitées aux routes et fichiers nécessaires.
- Ne commitez jamais un vrai mot de passe dans `application.properties` ou `.env`.
- Utilisez des secrets GitHub Actions ou des variables d'environnement pour la CI et la production.

## Dockerfile

Le [Dockerfile](Dockerfile) utilise une construction multi-stage :

1. une image Maven avec JDK 17 pour compiler l'application ;
2. une image JRE Eclipse Temurin plus légère pour exécuter le fichier JAR ;
3. un utilisateur non privilégié `app` dans l'image finale.

## Développement

Après une modification :

```powershell
.\mvnw.cmd -B test
.\mvnw.cmd -B checkstyle:check
```

Puis vérifiez l'application avec :

<http://localhost:8080>

