<template>
  <div>
    <h1>Recettes</h1>

    <div class="recettes-grid">
      <Carte
        v-for="recipe in data?.recipes"
        :key="recipe.id"
        :recipe="recipe"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
  import type { RecipePage } from '~/types/api'

  const pageNum = 1

  const { data } = useFetch<RecipePage>(
    `https://dummyjson.com/recipes?limit=6&skip=${(pageNum - 1) * 6}`,
  )
</script>

<style scoped></style>
