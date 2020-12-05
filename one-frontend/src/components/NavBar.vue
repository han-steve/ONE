<template>
  <div id="background">
    <div class="nav-bar" :class="{fullscreen: login}">
      <div id="logo" v-show="!login">
        <h1><u>Taco 'Bout It</u></h1>
<!--        <div id="rectangle"></div>-->
      </div>
      <transition name="nav">
        <div id="nav" v-show="!login">
          <div class="nav-options">
            <router-link to="/dashboard">
              <i class="fas fa-tachometer-alt"></i> Dashboard
            </router-link>
          </div>
          <div class="nav-options">
            <router-link to="/summary">
              <i class="fas fa-chart-pie"></i> Summary
            </router-link>
          </div>
          <div class="nav-options">
            <router-link to="/transactions">
              <i class="fas fa-list"></i> Transactions
            </router-link>
          </div>
          <div class="nav-options">
            <router-link to="/budget">
              <i class="fas fa-calculator"></i> Budget
            </router-link>
          </div>
          <div class="nav-options">
            <router-link to="/accounts">
              <i class="fas fa-users"></i> Accounts
            </router-link>
          </div>
        </div>
      </transition>
      <transition name="login">
        <login v-if="login"></login>
      </transition>
    </div>
    <i class="fas fa-plus-circle plus-button" @click="openModal" v-show="!login"></i>
    <transaction-modal ref="modal" v-show="!login">Add&nbsp</transaction-modal>
    <user v-show="!login"></user>
  </div>
</template>

<script>
import User from "@/components/User.vue";
import TransactionModal from "@/components/TransactionModal.vue";
import Login from "@/components/Login.vue";

export default {
  props: ["login"],
  components: {
    User,
    TransactionModal,
    Login
  },
  data() {
    return {
      showLogin: false
    };
  },
  methods: {
    openModal() {
      this.$refs.modal.open();
    }
  }
};
</script>

<style scoped>
.nav-bar {
  background: linear-gradient(
    45deg,
    rgba(62, 105, 221, 1) 20%,
    rgba(241, 42, 42, 1) 80%
  );
  height: 100vh;
  width: 15em;
  position: fixed;
  z-index: 10;
  transition: width 0.7s ease-out;
}
.fullscreen {
  width: 100vw;
}
#logo {
  padding-top: 2em;
}
#logo h1 {
  text-align: center;
  font-weight: 650;
  font-style: italic;
  font-size: 2em;
  margin: 0;
  color: white;
}
#rectangle {
  width: 8em;
  height: 0.4em;
  background-color: white;
  margin: 0 auto;
  position: relative;
  top: -0.5em;
}
#nav {
  margin-top: 2em;
  display: grid;
  grid-template-rows: repeat(auto);
  height: 5em;
}
.nav-options {
  display: grid;
  align-content: center;
  padding: 0.4em 0 0.4em 1.5em;
  font-size: 1.5em;
  text-align: left;
  font-weight: 700;
  color: white;
  transition: 0.3s ease;
}
a {
  color: inherit;
  text-decoration: none;
}
#nav div:hover {
  background-color: #f7f7fc;
  color: black;
  font-weight: 700;
}
.nav-options i {
  width: 30px;
}
/* button */
.plus-button {
  color: white;
  font-size: 50px;
  position: fixed;
  bottom: 100px;
  left: 5.9rem;
  transition: 0.2s ease-out;
  cursor: pointer;
  z-index: 900;
}
.plus-button:hover {
  transform: scale(1.1);
}

.login-enter-active,
.login-leave-active {
  transition: all 1s ease-out;
}
.login-enter,
.login-leave-to {
  transform: translateY(500px);
  opacity: 0;
}
/* for some reason any longer time will make other animations laggy */
.nav-enter-active,
.nav-leave-active {
  transition: all 0.2s ease;
}
.nav-enter,
.nav-leave-to {
  opacity: 0;
  transform: translateX(-200px);
}
</style>
