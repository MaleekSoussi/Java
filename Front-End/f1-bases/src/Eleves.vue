<template>
  <div>
    <h1>Gestion des élèves</h1>

    <form>
      <input type="text" placeholder="Nouvel élève" v-model="nouveauNom" />

      <button @click.prevent="ajouterEleve">Ajouter</button>
    </form>

    <ul>
      <li v-for="eleve in eleves" :key="eleve">
        {{ eleve }}
        <button @click="supprimerEleve(eleve)">Supprimer</button>

      </li>
    </ul>
    <button @click="trierEleves()">trier</button>
  </div>
</template>

<script setup lang="ts">
  import { ref } from 'vue'

  const eleves = ref(['Cathy', 'Brice', 'Aline'])

  const nouveauNom = ref('')

  function supprimerEleve(eleve: string) {
    eleves.value = eleves.value.filter((item) => item !== eleve)
  }

  function ajouterEleve() {
    eleves.value.push(nouveauNom.value)
    nouveauNom.value = ''
  }
  function trierEleves() {
    eleves.value.sort((a, b) => (a < b ? -1 : 1))
  }
</script>

<style scoped>
  li {
    margin: 10px;
  }

  button {
    margin-left: 10px;
  }

  input {
    padding: 5px;
  }
</style>
