import express from 'express'

import db from '../database.js'
import { authenticateJWT } from '../middleware/auth.js'
import { parseDelaiMs, wait } from '../utils/delai.js'

const router = express.Router()

async function processDocumentsList(req, res) {
  const documents = await db.all(
    `
        SELECT id,
               title,
               content
        FROM documents
        WHERE owner_id = ?
        ORDER BY id
        `,
    [req.user.id],
  )

  res.json(documents)
}

async function processDocumentsListPage(req, res) {
  const pageParam = Number.parseInt(String(req.query.page ?? '1'), 10)
  const limitParam = Number.parseInt(String(req.query.limit ?? '10'), 10)

  const page = Number.isInteger(pageParam) && pageParam > 0 ? pageParam : 1
  const limit = Number.isInteger(limitParam) && limitParam > 0 ? limitParam : 10

  const total = await db.get(
    `
        SELECT COUNT(*) AS total
        FROM documents
        WHERE owner_id = ?
        `,
    [req.user.id],
  )

  const documents = await db.all(
    `
        SELECT id,
               title,
               content
        FROM documents
        WHERE owner_id = ?
        ORDER BY id
        LIMIT ? OFFSET ?
        `,
    [req.user.id, limit, (page - 1) * limit],
  )

  const totalDocuments = Number(total?.total ?? 0)

  res.json({
    documents,
    page,
    limit,
    total: totalDocuments,
    pages: Math.max(1, Math.ceil(totalDocuments / limit)),
  })
}

/**
 * Retourne uniquement les documents de l'utilisateur connecté
 */
router.get('/list', authenticateJWT, async (req, res) => {
  return processDocumentsList(req, res, 0)
})

router.get('/list-page', authenticateJWT, async (req, res) => {
  return processDocumentsListPage(req, res)
})

router.get('/list-page-delay', authenticateJWT, async (req, res) => {
  const delayMs = parseDelaiMs(req.query.delay)

  if (delayMs === null) {
    return res.status(400).json({
      message: 'Paramètre delay invalide: entier >= 0 attendu',
    })
  }

  await wait(delayMs)

  return processDocumentsListPage(req, res)
})

router.get('/list-delay', authenticateJWT, async (req, res) => {
  const delayMs = parseDelaiMs(req.query.delay)

  if (delayMs === null) {
    return res.status(400).json({
      message: 'Paramètre delay invalide: entier >= 0 attendu',
    })
  }

  await wait(delayMs)
  return processDocumentsList(req, res)
})

/**
 * Route en accès libre pour tests
 */
router.get('/list-all-free', async (req, res) => {
  const documents = await db.all(
    `
        SELECT id,
               title,
               content
        FROM documents
        ORDER BY id
        `,
  )

  res.json(documents)
})

/**
 * Route pour admin
 */
router.get('/admin/list', authenticateJWT, async (req, res) => {
  // vérif du rôle, pas
  if (req.user.role != 'ADMIN') {
    return res.status(403).json({
      message: 'Accès interdit',
    })
  }

  const documents = await db.all(
    `
        SELECT id,
               title,
               content
        FROM documents
        ORDER BY id
        `,
  )

  res.json(documents)
})

router.get('/admin/list-delay', authenticateJWT, async (req, res) => {
  // vérif du rôle, pas
  if (req.user.role != 'ADMIN') {
    return res.status(403).json({
      message: 'Accès interdit',
    })
  }

  const delayMs = parseDelaiMs(req.query.delay)

  if (delayMs === null) {
    return res.status(400).json({
      message: 'Paramètre delay invalide: entier >= 0 attendu',
    })
  }

  await wait(delayMs)
  const documents = await db.all(
    `
        SELECT id,
               title,
               content
        FROM documents
        ORDER BY id
        `,
  )

  res.json(documents)
})

/**
 * Retourne un document par son id, avec vérification a posteriori que c'est le bon utilisateur
 */
router.get('/:id', authenticateJWT, async (req, res) => {
  const document = await db.get(
    `
        SELECT *
        FROM documents
        WHERE id = ?
        `,
    [req.params.id],
  )

  if (!document) {
    return res.status(404).json({
      message: 'Document introuvable',
    })
  }

  if (document.owner_id !== req.user.id) {
    return res.status(403).json({
      message: 'Accès interdit',
    })
  }

  res.json(document)
})

export default router
