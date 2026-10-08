<script setup lang="ts">
import type { InfosUtilisateur } from '~/types/api'

const jeton = useState('accessToken', () => '')

const utilisateur = ref<InfosUtilisateur>()
const errorMessage = ref('')

onMounted(() => {
  $fetch<InfosUtilisateur>('http://localhost:3123/users/me', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${jeton.value}`
    },
  })
    .then((response) => {
      console.log('Réponse succès du serveur (users/me) : ')
      console.log(response)
      utilisateur.value = response
    })
    .catch((error) => {
      // .catch reçoit aussi les erreurs levées dans le .then
      console.log('Erreur (users/me) : ')
      console.log(error)
      if (error?.statusCode === 401) {
        errorMessage.value = 'Accès refusé : veuillez vous connecter.'
      } else {
        errorMessage.value = 'Impossible de récupérer vos informations.'
      }
    })
})
</script>

<template>
  <main>
    <h1>Profil</h1>

    <p v-if="errorMessage">{{ errorMessage }}</p>

    <article v-else-if="utilisateur">
      <p><strong>Identifiant :</strong> {{ utilisateur?.id }}</p>
      <p><strong>Nom d'utilisateur :</strong> {{ utilisateur?.username }}</p>
      <p><strong>Rôle :</strong> {{ utilisateur?.role }}</p>
    </article>

    <p v-else>Chargement...</p>
  </main>
</template>