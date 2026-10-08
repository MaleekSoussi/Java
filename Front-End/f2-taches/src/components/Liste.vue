<template>
  <div>
    <div>
      <input type="checkbox" role="switch" v-model="masquerFaites" />
      Masquer les tâches terminées
    </div>
    <ul>
      <li v-for="tache in tachesAAfficher" :key="tache.id" class="tache">
        <div>
          <input type="checkbox" v-model="tache.faite" />
        </div>
        <div>
          {{ tache.titre }}
        </div>
        <div>
          <img src="@/assets/modifier.png" @click="emit('modif', tache)" />
          <img src="@/assets/supprimer.png" @click="emit('suppr', tache.id)" />
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import type { Tache } from '../types'

const props = defineProps<{ taches: Tache[] }>()
const emit = defineEmits(['suppr', 'modif'])

const masquerFaites = ref(false)

const tachesAAfficher = computed(() => {
  const resultat = props.taches.slice().sort((a, b) =>
    a.faite === b.faite ? a.id - b.id : b.faite ? -1 : 1,
  )
  return masquerFaites.value
    ? resultat.filter((tache) => !tache.faite)
    : resultat
})
</script>

<style scoped>
</style>