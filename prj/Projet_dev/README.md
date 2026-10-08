# Projet_dev — Gestion des cours (Spring Boot + React + PostgreSQL)

```
Projet_dev
├── backend    → API REST Spring Boot 4 (Java 17, Gradle, JWT)
├── frontend   → Interface React (Vite)
└── database   → schema.sql (installation neuve de la base)
```

## 1. Base de données

**Vous avez déjà la base `Projet_dev` (avec les ALTER TABLE faits ensemble)** : rien à faire, le backend est aligné dessus.

**Installation neuve** : dans pgAdmin, clic droit sur la base `Projet_dev` → Query Tool → exécuter `database/schema.sql`.

## 2. Backend

1. Ouvrir `backend/src/main/resources/application.properties` et mettre **votre** mot de passe PostgreSQL :
   `spring.datasource.password=...`
2. Ouvrir le dossier `backend` dans IntelliJ (il détecte Gradle), puis lancer `DemoApplication`.
3. Au premier démarrage, un compte admin est créé automatiquement :
   **admin@ecole.fr / admin1234** (modifiable dans `application.properties`).

L'API tourne sur http://localhost:8080.

## 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

Ouvrir http://localhost:5173.

## Fonctionnalités

| Espace admin | Espace parent |
|---|---|
| Tableau de bord (chiffres, soldes, derniers paiements) | Accueil : solde + planning de la semaine |
| Utilisateurs (CRUD, rôles) | Mes enfants (ajout, modification, suppression) |
| Enfants (CRUD, inscriptions aux cours) | Inscrire / désinscrire un enfant à un cours |
| Cours (CRUD, liste des inscrits) | Mes paiements (payer, historique) |
| Niveaux, Salles (CRUD) | |
| Paiements (enregistrer, supprimer) | |

### Règles métier
- **Solde restant** d'un parent = somme des prix (niveau) des cours suivis par ses enfants − total payé.
  Recalculé automatiquement à chaque inscription, désinscription ou paiement.
- Inscription : impossible si le cours est complet, n'est pas du niveau de l'enfant, ou chevauche un autre cours de l'enfant.
  Chaque inscription retire une place, chaque désinscription la rend.
- Cours : l'heure de fin doit être après l'heure de début, une salle ne peut pas avoir deux cours en même temps,
  et le nombre de places ne peut pas dépasser la capacité de la salle.
- Un parent ne voit et ne modifie que ses propres enfants et paiements.

## Endpoints

| Méthode | URL | Accès |
|---|---|---|
| POST | `/api/auth/signup`, `/api/auth/signin` | public |
| GET | `/api/auth/me` | connecté |
| CRUD | `/api/utilisateurs` | ADMIN |
| GET | `/api/niveaux`, `/api/salles`, `/api/cours` | connecté |
| POST/PUT/DELETE | `/api/niveaux`, `/api/salles`, `/api/cours` | ADMIN |
| GET | `/api/cours/{id}/enfants` | ADMIN |
| CRUD | `/api/enfants` | parent (les siens) / ADMIN (tous) |
| POST/DELETE | `/api/enfants/{id}/cours/{coursId}` | parent (les siens) / ADMIN |
| GET/POST | `/api/mouvements` | parent (les siens) / ADMIN (tous) |
| DELETE | `/api/mouvements/{id}` | ADMIN |
