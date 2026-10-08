<template>
  <div>
    <header class="page-header">
      <a to ="/" class="logo">Miam</a>
      <nav>
        <NuxtLink href="/recettes">Recettes</NuxtLink>
        <NuxtLink href="/contact">Contact</NuxtLink>
        <NuxtLink href="/apropos">A propos</NuxtLink>
      </nav>
    </header>
    <slot  class="container" />
  </div>
</template>
