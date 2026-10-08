import express from 'express'
import bcrypt from 'bcrypt'
import jwt from 'jsonwebtoken'

import db from '../database.js'
import { parseDelaiMs, wait } from '../utils/delai.js'

const router = express.Router()

async function processLogin(req, res) {
   const { username, password } = req.body

  if (!username || !password) {
    return res.status(400).json({
      message: 'username et password obligatoires',
    })
  }

  // Recherche de l'utilisateur dans la base
  const user = await db.get(
    `
        SELECT *
        FROM users
        WHERE username = ?
        `,
    [username],
  )

  // login inconnu : on le signale 
  // (pas le mieux en prod, on devrait plutôt renvoyer 
  // un message générique "login ou mot de passe incorrect" )
  if (!user) {
    return res.status(401).json({
      message: 'Utilisateur inconnu',
    })
  }

  // Vérification du mot de passe
  const validPassword = await bcrypt.compare(password, user.password)

  // Mêeme remarque que pour l'utilisateur inconnu, 
  // on ne devrait pas renvoyer un message spécifique en prod
  if (!validPassword) {
    return res.status(401).json({
      message: 'Mot de passe incorrect',
    })
  }

  const token = jwt.sign(
    {
      id: user.id,
      username: user.username,
      role: user.role,
    },
    process.env.JWT_SECRET,
    {
      expiresIn: '25min',
    },
  )
  
  // retour du token au client, en json
  res.json({
    token,
    'role':user.role
  })
}

// auth/login : vérifie le login/pass, retourne un jwt si OK
// params : username, password
// retour : token (=accesToken)
router.post('/login', async (req, res) => {
  // on traite, sans délai artificiel
  return processLogin(req, res)
})

router.post('/login-delay', async (req, res) => {
  const delayMs = parseDelaiMs(req.query.delay)

  if (delayMs === null) {
    return res.status(400).json({
      message: 'Paramètre delay invalide: entier >= 0 attendu',
    })
  }

  await wait(delayMs)
  return processLogin(req, res)
})


// auth/login : vérifie le login/pass, retourne un jwt si OK
// params : username, password
// retour : accessToken + refreshToken
// Rq : pas de version avec délai
router.post('/login-avec-refresh', async (req, res) => {
   const { username, password } = req.body

  if (!username || !password) {
    return res.status(400).json({
      message: 'username et password obligatoires',
    })
  }

  // Recherche de l'utilisateur dans la base
  const user = await db.get(
    `
        SELECT *
        FROM users
        WHERE username = ?
        `,
    [username],
  )

  // login inconnu : on le signale 
  // (pas le mieux en prod, on devrait plutôt renvoyer 
  // un message générique "login ou mot de passe incorrect" )
  if (!user) {
    return res.status(401).json({
      message: 'Utilisateur inconnu',
    })
  }

  // Vérification du mot de passe
  const validPassword = await bcrypt.compare(password, user.password)

  // Mêeme remarque que pour l'utilisateur inconnu, 
  // on ne devrait pas renvoyer un message spécifique en prod
  if (!validPassword) {
    return res.status(401).json({
      message: 'Mot de passe incorrect',
    })
  }

  const accessToken = jwt.sign(
    {
      id: user.id,
      username: user.username,
      role: user.role,
    },
    process.env.JWT_SECRET,
    {
      expiresIn: process.env.ACCESS_TOKEN_EXPIRATION,
    },
  )
  
  const refreshToken = jwt.sign(
    {
      id: user.id,
      username: user.username,
    },
    process.env.JWT_REFRESH_SECRET,
    {
      expiresIn: process.env.REFRESH_TOKEN_EXPIRATION,
    },
  )
  
  // retour du token au client, en json
  res.json({
    token: accessToken,
    refreshToken: refreshToken,
  })
})


// Demande de refresh du token d'accès, à partir du refreshToken
router.post('/refresh', (req, res) => {
  const { refreshToken } = req.body;

  if (!refreshToken) {
    return res.status(403).json({ error: 'Refresh token non fourni' });
  }

  try {
    const user = jwt.verify(refreshToken, process.env.JWT_REFRESH_SECRET);
    const token = jwt.sign(
      {
        id: user.id,
        username: user.username,
        role: user.role,
      },
      process.env.JWT_SECRET,
      {
        expiresIn: process.env.ACCESS_TOKEN_EXPIRATION,
      },
    )
    res.json({ token });
  } catch (error) {
    res.status(403).json({ error: 'Refresh token invalide' });
  }
});

export default router
