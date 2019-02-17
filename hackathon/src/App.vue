<template>
  <div id="app">
    <nav-tpl></nav-tpl>
    <router-view/>
  </div>
</template>

<script>
  import navTpl from "./components/NavTpl.vue";
  export default {
    name: 'App',
    components: {
      navTpl
    }
  }
</script>

<style>
  #app {
    padding: 0;
    font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
    display: grid;
    grid-template-columns: 28em auto;
    grid-template-areas: "navTpl dashboard"; 
  }
</style>
