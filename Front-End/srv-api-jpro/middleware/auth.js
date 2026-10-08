import jwt from 'jsonwebtoken'

// Middleware d'authentification JWT
// => https://expressjs.com/fr/5x/guide/using-middleware/
// préconisé pour sécuriser les routes d'une API 
// en centralisant la vérification du token dans un middleware 
// (pas la vérification des droits d'accès, qui est spécifique à chaque route)

// req : requête entrante
// res : réponse sortante
// next : fonction qui passe au prochain middleware ou route (founie par express.js lors de l'appel)
export function authenticateJWT(req, res, next) {
  // on récupère le "bearer token"
  const headerAuth = req.headers.authorization

  // pas de token : pb d'authentification
  if (!headerAuth) {
    return res.status(401).json({
      message: 'Token non fourni',
    })
  }

  // découpe du token, pour récupérer les infos et la signature
  const parts = headerAuth.split(' ')

  // vérif (minimale) de la structure du bearer token
  if (parts.length !== 2 || parts[0] !== 'Bearer') {
    return res.status(401).json({
      message: 'Format du token incorrect',
    })
  }

  // récupération du jwt
  const token = parts[1]

  try {
    // décryptage du jwt pour récupérer les infos de l'utilisateur
    // => le JWT_SECRET est dans le .env
    const decoded = jwt.verify(token, process.env.JWT_SECRET)

    req.user = decoded

    // next() : on passe au prochain middleware ou route
    // (cf paramètre next de la fonction authenticateJWT)
    // => si le token est valide, on laisse passer la requête jusqu'à la route demandée
    next()
  } catch (error) {
    return res.status(401).json({
      message: 'Token invalide ou expiré',
    })
  }
}
