import { eleves } from '../groupe1.js'

export default async function (fastify, opts) {
  fastify.get('/moyennes', async function (request, reply) {
    return eleves
  })
}