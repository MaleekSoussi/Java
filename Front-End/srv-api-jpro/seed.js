import bcrypt from 'bcrypt'
import fs from 'node:fs/promises'
import path from 'node:path'
import db from './database.js'

const DATA_FILE = path.resolve('./data/documents.json')

async function seed() {
  await db.run('DELETE FROM documents')
  await db.run('DELETE FROM users')

  const users = [
    { username: 'alice', password: 'alice123', role: 'USER' },
    { username: 'bob', password: 'bob123', role: 'USER' },
    { username: 'admin', password: 'admin123', role: 'ADMIN' },
  ]

  const userIds = new Map()

  for (const user of users) {
    const passwordHash = await bcrypt.hash(user.password, 10)
    const result = await db.run(
      `
        INSERT INTO users(username, password, role)
        VALUES(?, ?, ?)
      `,
      [user.username, passwordHash, user.role],
    )

    userIds.set(user.username, result.lastID)
  }

  const data = JSON.parse(await fs.readFile(DATA_FILE, 'utf8'))

  for (const item of data) {
    const ownerId = userIds.get(item.owner)

    if (!ownerId) {
      throw new Error(`Utilisateur inconnu pour le document: ${item.owner}`)
    }

    await db.run(
      `
        INSERT INTO documents(title, content, owner_id)
        VALUES(?, ?, ?)
      `,
      [item.title, item.content, ownerId],
    )
  }

  const count = await db.get('SELECT COUNT(*) AS total FROM documents')
  console.log(`Base initialisée avec ${count.total} documents.`)
}

await seed()
