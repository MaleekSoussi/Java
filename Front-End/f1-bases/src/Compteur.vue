<template>
  <div>
    <h1 :style="{ color: compteur >= 3 ? 'red' : 'blue' }">
      Compteur : {{ compteur }}
    </h1>
  </div>

  <button v-on:click="decrementer">-</button>
  <button v-on:click="incrementer">+</button>

  <p v-if="compteur >= 3">Premier palier atteint</p>
  <p v-else>Continuez de progresser !</p>

  <p v-show="compteur >= 5">Deuxième palier atteint</p>

  <p :class="{ impact: compteur >= 5 }">
    Premier palier atteint
  </p>

  <input
    type="text"
    :placeholder="'Valeur mini : ' + compteur"
    :title="compteur.toString()"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue'

const compteur = ref(0)

function decrementer() {
  compteur.value--
}

function incrementer() {
  compteur.value++
}

function valeurMini() {
  return `Valeur mini : ${compteur.value}`
}
</script>

<style scoped>
.impact {
  font-family: Impact;
}
</style>