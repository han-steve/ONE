<template>
  <div class="background">
    <div class="user-background">
      <div class="profile-picture">
        <i class="fas fa-user"></i>
      </div>
      <span class="text">Steve Han</span>
      <i class="fas fa-caret-right arrow"></i>
    </div>
    <div class="user-menu">
      <div class="menu-option text">
        <a>Settings</a>
      </div>
      <div class="menu-option text">
        <a>Profile</a>
      </div>
      <div class="menu-option text" @click="reset()">
        <router-link to="login">Logout</router-link>
      </div>
      <div class="menu-option text">
        <a>Support</a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    reset() {
      this.$store.dispatch("resetAction");
    }
  }
};
</script>

<style scoped>
.user-background {
  background-color: #2d58cc;
  width: 15em;
  height: 5em;
  position: fixed;
  bottom: 0;
  left: 0;
  z-index: 100;
  display: grid;
  grid-template-columns: 5em auto 2em;
  align-items: center;
}
.text {
  font-weight: 700;
  font-size: 1.2em;
  color: white;
}
.profile-picture {
  justify-self: center;
  font-size: 2em;
  color: white;
}
.arrow {
  color: white;
  font-size: 1.1em;
  padding-top: 0.2em;
}
.user-menu {
  max-width: 0px;
  position: fixed;
  bottom: 0;
  left: 15em;
  background-color: #2d58cc;
  padding: 5px 0 15px 0;
  overflow: hidden;
  transition: max-width 0.3s ease-in-out;
  z-index: 100;
}
.menu-option {
  padding: 8px 25px;
}
.background:hover .user-menu {
  max-width: 200px;
}
a {
  color: white;
  text-decoration: none;
}
</style>