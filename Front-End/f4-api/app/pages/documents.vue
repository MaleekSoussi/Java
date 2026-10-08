<script setup lang="ts">
import type { Document } from '~/types/api'

const jeton = useState('accessToken', () => '')

const documents = ref<Document[]>([])
const errorMessage = ref('')
const isLoading = ref(true)

onMounted(() => {
  $fetch<Document[]>('http://localhost:3123/documents', {   // adapte l'URL d'après le Swagger
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${jeton.value}`
    },
  })
    .then((response) => {
      console.log('Réponse succès du serveur (documents) : ')
      console.log(response)
      documents.value = response
    })
    .catch((error) => {
      console.log('Erreur (documents) : ')
      console.log(error)
      if (error?.statusCode === 401) {
        errorMessage.value = 'Accès refusé : veuillez vous connecter.'
      } else {
        errorMessage.value = 'Impossible de récupérer vos documents.'
      }
    })
    .finally(() => {
      isLoading.value = false
    })
})
</script>

<template>
  <main>
    <h1>Mes documents</h1>

    <p v-if="isLoading">Chargement...</p>
    <p v-else-if="errorMessage">{{ errorMessage }}</p>
    <p v-else-if="documents.length === 0">Aucun document.</p>

    <ul v-else>
      <li v-for="doc in documents" :key="doc.id">
        <strong>{{ doc.title }}</strong> — {{ doc.content }}
      </li>
    </ul>
  </main>
</template>