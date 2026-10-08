import express from 'express'

import { authenticateJWT } from '../middleware/auth.js'
import { parseDelaiMs, wait } from '../utils/delai.js'

const router = express.Router()


async function processMe(req, res) {
  
  res.json({
    message: 'Utilisateur authentifié',
    user: req.user,
  })
}

router.get('/me', authenticateJWT, async (req, res) => {
  return processMe(req, res, 0)
})

router.get('/me-delay', authenticateJWT, async (req,res) =>{
  const delayMs = parseDelaiMs(req.query.delay)

  if (delayMs === null) {
    return res.status(400).json({
      message: 'Paramètre delay invalide: entier >= 0 attendu',
    })
  }

  await wait(delayMs)

  return processMe(req, res, delayMs)
})

export default router
