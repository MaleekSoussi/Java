<template>
  <div class="container">
    <Titre />
    <Saisie v-model="titreSaisi" @ok="enregistrerSaisie" />
    <Liste :taches="taches" @suppr="supprimerTache" @modif="preparerModif" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Titre from './components/Titre.vue'
import Saisie from './components/Saisie.vue'
import Liste from './components/Liste.vue'
import json from './assets/taches.json'
import type { Tache } from './types'

const taches = ref(json as Tache[])
const tacheAModifier = ref(null as null | Tache)
const titreSaisi = ref('')

function supprimerTache(id: number) {
  const index = taches.value.findIndex((tache) => tache.id === id)
  if (index !== -1) {
    taches.value.splice(index, 1)
  }
}

function preparerModif(tache: Tache) {
  tacheAModifier.value = tache
  titreSaisi.value = tache.titre
}

function enregistrerSaisie() {
  if (tacheAModifier.value !== null) {
    tacheAModifier.value.titre = titreSaisi.value
  } else {
    taches.value.push({
      titre: titreSaisi.value,
      faite: false,
      id: Date.now()
    })
  }
  tacheAModifier.value = null
  titreSaisi.value = ''
}
</script>

<style scoped></style>