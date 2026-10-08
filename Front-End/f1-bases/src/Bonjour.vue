<template>
  <div>

<h1>Bonjour {{ nom }}</h1>

  </div>
</template>

<script setup lang="ts">
const nom = 'John'
</script>

<style scoped>
h1 {
 color: red;
}

</style>