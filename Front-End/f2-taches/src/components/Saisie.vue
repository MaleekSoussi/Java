<template>
  <div>
    <form @submit="onSubmit">
      <input type="text" v-model="titre" />
      <button type="reset">X</button>
      <button type="submit">OK</button>
    </form>
  </div>
</template>

<script setup lang="ts">
const titre = defineModel<string>()
const emit = defineEmits(['ok'])

function onSubmit(event: Event) {
  event.preventDefault()
  emit('ok')
}
</script>

<style scoped>
</style>