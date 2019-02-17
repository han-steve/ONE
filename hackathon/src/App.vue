<template>
  <div id="app">
    <navigation/>
    <dashboard/>
  </div>
</template>

<script>
import Navigation from "./components/Navigation.vue";
import Dashboard from "./components/Dashboard.vue";

export default {
  name: "app",
  components: {
    Navigation,
    Dashboard
  }
};
</script>

<style>
#app {
  padding: 0;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  display: grid; 
  grid-template-columns: 28em auto;
  grid-template-areas: "navigation dashboard"
}
</style>
