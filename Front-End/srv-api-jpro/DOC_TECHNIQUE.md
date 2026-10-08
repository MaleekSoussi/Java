# Documentation technique interne - api-jwt-demo
(Générée par IA, relue et laissée inchangée)

## 1. Objet du document
Ce document decrit le fonctionnement interne du code de l'API.
Il ne sert pas de reference fonctionnelle pour decrire les routes exposees aux clients.

## 2. Vue d'ensemble de l'architecture
L'application suit une architecture Express simple, modulaire et separee en couches:

- Bootstrap serveur: initialisation Express, middleware globaux, montage des routeurs
- Routeurs metier: authentification, utilisateur, documents
- Middleware transverse: verification JWT
- Couche donnees: acces SQLite via le package sqlite
- Utilitaires transverses: gestion des delais artificiels pour les scenarios pedagogiques
- Initialisation des donnees: script de seed

Organisation des fichiers principaux:

- Point d'entree serveur: app.js
- Acces base de donnees: database.js
- Middleware auth: middleware/auth.js
- Routeur auth: routes/auth.js
- Routeur users: routes/users.js
- Routeur documents: routes/documents.js
- Utilitaire delai: utils/delai.js
- Seed donnees: seed.js

## 3. Cycle de demarrage
Au lancement du processus Node:

1. dotenv est charge tres tot pour injecter les variables d'environnement
2. Express est instancie
3. Les middlewares globaux sont poses dans cet ordre:
- cors
- express.json
4. Les routeurs sont montes par domaine
5. Le serveur ecoute sur PORT, ou 3123 par defaut

Caracteristique importante:
- database.js utilise top-level await. La connexion SQLite et la creation des tables sont terminees avant l'utilisation de la couche donnees.

## 4. Couche donnees SQLite
La couche donnees repose sur sqlite3 (driver) et sqlite (API async/await).

Initialisation:

- Ouverture de database.sqlite
- Execution d'un script SQL de creation conditionnelle des tables

Schema cree automatiquement:

- users
  - id (PK autoincrement)
  - username (unique, non null)
  - password (hash bcrypt)
  - role (texte, USER par defaut)

- documents
  - id (PK autoincrement)
  - title (non null)
  - content
  - owner_id (FK logique vers users.id)

Notes techniques:

- Le schema est initialise a chaque demarrage via CREATE TABLE IF NOT EXISTS
- Le code n'active pas explicitement les contraintes PRAGMA foreign_keys dans database.js
- Les acces se font via db.get, db.all, db.run selon les besoins

## 5. Middleware d'authentification JWT
Le middleware authenticateJWT centralise le controle d'authentification.

Etapes internes:

1. Lecture de l'en-tete Authorization
2. Verification presence
3. Verification format Bearer <token>
4. Verification cryptographique du JWT avec JWT_SECRET
5. Injection du payload decode dans req.user
6. Appel de next() pour transmettre au handler suivant

En cas d'erreur (absence, format, signature invalide, expiration), la chaine est coupee et une reponse 401 est renvoyee.

Responsabilites et limites:

- Le middleware valide l'identite, pas l'autorisation metier fine
- Les controles de role/propriete restent dans les handlers concernes

## 6. Flux interne d'authentification
Le module auth encapsule la creation du token.

Pipeline de traitement:

1. Validation minimale des champs entrants
2. Recherche utilisateur en base
3. Verification du mot de passe via bcrypt.compare
4. Generation du JWT via jwt.sign
5. Retour du token signe

Payload du token genere:

- id
- username
- role

Parametrage du token:

- Secret: JWT_SECRET
- Duree: 25min

Impact conception:

- Les infos utilisateur principales sont transportees dans le token
- Les handlers proteges peuvent exploiter req.user sans re-requete systematique sur users

## 7. Flux interne des modules users et documents
Les modules users et documents reposent sur req.user injecte par authenticateJWT.

Pattern commun:

1. Authentification via middleware
2. Eventuel delai artificiel (routes delay)
3. Traitement metier
4. Reponse JSON

Specificites du module documents:

- Filtrage des documents par owner_id = req.user.id pour l'acces utilisateur
- Controle de role ADMIN dans une branche dediee
- Verification de propriete sur recuperation d'un document cible

## 8. Mecanisme de delai artificiel
Le module utils/delai.js permet de ralentir certaines branches pour les demonstrations front.

Fonctions:

- parseDelaiMs(rawDelay)
  - Retourne 0 si le parametre est absent
  - Retourne null si la valeur n'est pas un entier >= 0
  - Borne maximale a 30000 ms

- wait(ms)
  - Promise resolue apres setTimeout

Consequences pratiques:

- Le front pilote le delai par requete
- Le serveur ne lit pas de variables d'environnement pour cette fonctionnalite
- Un delai 0 permet de conserver le meme comportement logique sans ralentissement

## 9. Script de seed
Le script seed.js reinitialise les donnees d'exercice.

Comportement:

1. Suppression des lignes documents puis users
2. Generation de hash bcrypt pour 3 comptes
3. Insertion des utilisateurs
4. Insertion des documents relies aux utilisateurs

Interet:

- Reproductibilite des tests
- Etat de depart stable pour les demonstrations pedagogiques

## 10. Gestion des erreurs et conventions de reponse
Le code suit une gestion des erreurs explicite dans les handlers.

Approche actuelle:

- Erreurs metier connues: reponse immediate avec code HTTP adapte
- Erreurs techniques inattendues: pas de middleware global d'erreur dedie

Implication:

- Les erreurs non capturees dans un handler pourraient remonter au comportement par defaut Express
- Pour industrialiser, un middleware central de gestion d'erreur serait utile

## 11. Configuration et variables d'environnement
Variables observees dans le code:

- JWT_SECRET
- PORT
Notes:

- .env contient actuellement JWT_SECRET et PORT
- Le delai artificiel n'est pas configure cote serveur

## 12. Securite: points solides et points de vigilance
Points solides:

- Mot de passe stocke sous forme de hash bcrypt
- JWT verifie cote serveur a chaque acces protege
- Separation claire authentification (middleware) vs autorisation (handlers)

Vigilances:

- Secret JWT en clair dans .env local (normal en dev, a securiser en prod)
- Absence de rotation/blacklist de token
- Pas de middleware global anti-bruteforce sur login
- Pas de validation schema stricte des payloads entrants

## 13. Performance et comportement runtime
Caracteristiques:

- Application monolithique legere, I/O majoritairement asynchrone
- SQLite sur fichier local, adapte au contexte demo/atelier
- Delais artificiels optionnels pouvant augmenter la latence de facon volontaire

Limites attendues:

- SQLite peut devenir un goulot si charge concurrente elevee
- Pas de pool horizontal ni orchestration avancee

## 14. Recommandations d'evolution (sans changer le role pedagogique)
1. Factoriser la logique d'authentification du module auth dans une fonction commune pour reduire la duplication entre branche standard et branche delay
2. Ajouter un middleware global d'erreur pour uniformiser les reponses inattendues
3. Ajouter une couche de validation d'entree (schema) sur les payloads
4. Ajouter une journalisation structurée minimale (requete, latence, code retour)
5. Externaliser la configuration de duree JWT et des bornes de delai

## 15. Cartographie rapide des dependances
- express: routage HTTP et pipeline middleware
- cors: ouverture CORS pour le front
- dotenv: chargement configuration
- bcrypt: hash et verification de mots de passe
- jsonwebtoken: signature et verification JWT
- sqlite + sqlite3: persistance locale
- nodemon (dev): relance automatique en developpement
- swagger-ui-express: exposition de la documentation Swagger dans le navigateur
- swagger-jsdoc: generation ou lecture de specification OpenAPI

## 16. Récapitulatif du travail Swagger réalisé
Une documentation Swagger a ete ajoutee a l'API afin de permettre une consultation rapide des endpoints et des schemas depuis le serveur lui-meme.

### 16.1 Objectif
Exposer un point d'entree de documentation de type OpenAPI/Swagger, accessible via le navigateur, sans modifier les routes metier du projet.

### 16.2 Fichiers impliqués
- app.js: montage de la route /api-docs et chargement de la specification OpenAPI
- openapi.json: specification OpenAPI source de la documentation
- package.json: ajout des dependances Swagger via pnpm

### 16.3 Mode d'usage
La documentation est disponible a l'URL suivante:
- http://localhost:3123/api-docs

Cette route affiche l'interface Swagger UI, ou l'on peut visualiser les endpoints, les schemas, les requetes et les autorisations JWT.

### 16.4 Verification fonctionnelle
Le lancement du serveur a confirme que l'API demarre correctement avec Swagger actif.

Sortie observee lors du demarrage:
- "Serveur lance"
- "Swagger disponible sur http://localhost:3123/api-docs"

Cela confirme que le montage de Swagger ne casse pas le boot de l'application et que la route est bien accessible.

## 17. Executions lancees
Les commandes suivantes ont ete executees pendant le setup Swagger:

1. Installation des dependances Swagger
   ```bash
   cd /d "f:\Jerome\Cours\JavaEA\devVS\AteliersFront\API\api-jwt-demo"
   pnpm add swagger-ui-express swagger-jsdoc
   ```

   Resultat attendu: ajout des packages dans package.json et lockfile.
   Observations: installation reussie, avec avertissement pnpm concernant des build scripts ignores, puis validation avec `pnpm approve-builds`.

2. Verification du demarrage du serveur avec Swagger actif
   ```bash
   cd /d "f:\Jerome\Cours\JavaEA\devVS\AteliersFront\API\api-jwt-demo"
   node app.js
   ```

   Resultat observe: le serveur a bien demarre et affiche la route Swagger sur /api-docs.

## 18. Resume execution interne
En synthese:

- app.js initialise et connecte les briques
- database.js fournit un acces SQLite deja prepare
- auth.js produit des tokens JWT a partir des identifiants verifies
- middleware/auth.js valide les tokens et alimente req.user
- users.js et documents.js exploitent req.user pour appliquer les regles d'acces
- utils/delai.js injecte une latence parametrable pour les scenarios d'apprentissage front
- seed.js permet de repartir d'un etat fonctionnel stable
- Swagger UI expose maintenant la spec OpenAPI sous /api-docs pour une documentation exploitable dans le navigateur
