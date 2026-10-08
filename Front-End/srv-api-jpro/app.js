import 'dotenv/config'

import fs from 'node:fs'
import express from 'express'
import cors from 'cors'
import swaggerUi from 'swagger-ui-express'

import authRoutes from './routes/auth.js'
import userRoutes from './routes/users.js'
import documentRoutes from './routes/documents.js'

const app = express()
const openApiSpec = JSON.parse(
  fs.readFileSync(new URL('./openapi.json', import.meta.url), 'utf8'),
)

app.use(cors())
app.use(express.json())

app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(openApiSpec))

app.use('/auth', authRoutes)
app.use('/users', userRoutes)
app.use('/documents', documentRoutes)

app.get('/', (req, res) => {
  res.json({
    message: 'API JWT Demo opérationnelle !',
  })
})

// port stocké dans le .env, ou 3123 par défaut sinon
app.listen(process.env.PORT || 3123, () => {
  console.log('Serveur lancé')
  console.log('Swagger disponible sur http://localhost:3123/api-docs')
})
