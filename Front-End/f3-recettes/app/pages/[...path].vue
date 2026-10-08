<template>
  <div>
    <h1>Cette page n'existe pas</h1>
    <p>{{ $route.fullPath }}</p>
    definePageMeta({ layout: 'minimal', })
  </div>
</template>

<script setup lang="ts"></script>

<style scoped></style>
