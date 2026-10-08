<template>
  <article class="recette-card">

    <img :src="recipe.image" />

    <h3>{{ recipe.name }}</h3>

    <p>{{ recipe.difficulty }}</p>

  </article>
</template>

<script setup lang="ts">
import type { Recipe } from '~/types/api'

defineProps<{
  recipe: Recipe
}>()
</script>

<style scoped>

</style>