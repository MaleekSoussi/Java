<script setup lang="ts">
  import { ref } from 'vue'

  const form = ref({
    username: '',
    password: '',
  })

  const isLoading = ref(false)
  const errorMessage = ref('')

  // Jeton stocké globalement (accessible depuis toutes les pages)
  const jeton = useState('accessToken', () => '')

  function onSubmit() {
    isLoading.value = true
    errorMessage.value = ''

    $fetch('http://localhost:3123/auth/login', {
      method: 'POST',
      body: {
        username: form.value.username,
        password: form.value.password,
      },
    })
      .then((response: any) => {
        console.log('Réponse succès du serveur : ')
        console.log(response)
        jeton.value = response.token
      })
      .catch((response) => {
        console.log('Réponse échec du serveur : ')
        console.log(response)
        jeton.value = ''
        errorMessage.value = 'Identifiants incorrects'
      })
      .finally(() => {
        isLoading.value = false
      })
  }
</script>

<template>
  <main>
    <h1>Connexion</h1>

    <form class="login-form" @submit.prevent="onSubmit">
      <label for="username">Nom d'utilisateur</label>

      <input id="username" v-model="form.username" type="text" required />

      <label for="password">Mot de passe</label>

      <input id="password" v-model="form.password" type="password" required />

      <button type="submit" :disabled="isLoading">
        {{ isLoading ? 'Connexion...' : 'Se connecter' }}
      </button>
    </form>

    <p v-if="errorMessage">{{ errorMessage }}</p>
    <p v-if="jeton">Connecté</p>
  </main>
</template>