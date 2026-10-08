# API JWT Demo
**Documentation générée par IA, relue, peu modifiée.**

Cette API permet de tester une authentification JWT avec des routes de démonstration pour l’authentification, les utilisateurs et les documents.

## Développement avec IA : démarche

La **programmation a été faite avec assistance IA**. Le développement général est itératif, chaque itération où l'IA est intervenue suit globalement ce principe : 
- **expression d'un objectif** de prog avec demande à l'assistant de proposer une solution d'implémentation
- **"discussion" sur la solution**
- lorsque la solution est OK, **demande d'une planification** (étapes du dev)
- implémentation par l'IA, par **étapes planifiées précédemment**
- **relecture à chaque étape**, correction si besoin
  
Les besoins exprimés laissent, pour ce développement, peu de marge de "choix" à l'assistant IA : architecture totalement imposée, solutions techniques précisées dans le prompt, etc.
Plusieurs assistants différents ont été utilisés (à travers Copilot) : ce petit projet m'a permis de tester un peu les performances d'agents que je n'avais pas (ou peu) utilisés.

## Prérequis

- Node.js 18 ou plus
- npm ou pnpm
- Git

## Cloner le projet

```bash
git clone <url-du-repo>
cd srv-api-jpro
```
(Si vous préférez vous pouvez télécharger le package (cliquer sur le bouton code dans la page d'accueil du dépôt). )

## Installer les dépendances

Avec npm :

```bash
npm install
```

Avec pnpm :

```bash
pnpm install
```

## Configurer l’environnement

Le fichier `.env` est versionné dans ce TP et doit donc être présent dans le clone.

Vérifiez la présence d’un fichier `.env` à la racine du projet avec le contenu suivant :

```env
PORT=3123
JWT_SECRET=change_me_dev_secret
JWT_REFRESH_SECRET=change_me_refresh_secret
ACCESS_TOKEN_EXPIRATION=25min
REFRESH_TOKEN_EXPIRATION=7d
```

## Initialiser les données de démonstration

```bash
npm run seed
```

ou :

```bash
pnpm seed
```

## Démarrer l’API

```bash
npm start
```

ou :

```bash
pnpm start
```

Par défaut, le serveur écoute sur le port `3123` si `PORT` n’est pas défini.

## Vérifier que l’API est bien démarrée

```bash
curl http://localhost:3123/
```

Réponse attendue :

```json
{
  "message": "API JWT Demo opérationnelle !"
}
```

## Documentation Swagger

La documentation Swagger est disponible sur :

```text
http://localhost:3123/api-docs
```

La spécification OpenAPI est dans le fichier :

- `openapi.json`

## Mode développement

Pour démarrer avec surveillance automatique des modifications :

```bash
npm run dev
```

ou :

```bash
pnpm dev
```

## Structure principale

- `app.js` : point d’entrée du serveur
- `routes/` : routes API
- `middleware/` : middleware JWT
- `database.js` : accès SQLite
- **`seed.js` : initialisation des données (à partir du fichier data/documents.json) : on y trouve les logins/pass des utilisateurs**
- `openapi.json` : documentation OpenAPI
- `DOC_TECHNIQUE.md` : documentation technique interne

## Points importants

- Le projet utilise Express, JWT, SQLite et Swagger.
- L’authentification se fait via un token JWT envoyé dans le header `Authorization` au format `Bearer <token>`.
- Les routes protégées sont vérifiées par le middleware JWT dans `middleware/auth.js`.
- Les délais artificiels de démonstration sont gérés via des routes de test (`delay`).

## Exemple de login

```bash
curl -X POST http://localhost:3123/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "secret123"
  }'
```

## Exemple d’utilisation du token

```bash
curl http://localhost:3123/users/me \
  -H "Authorization: Bearer <votre_token_jwt>"
```
