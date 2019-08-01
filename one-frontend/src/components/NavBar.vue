<template>
  <div id="background">
    <div id="nav-bar">
      <div id="logo">
        <h1>ONE</h1>
        <div id="rectangle"></div>
      </div>
      <div id="nav">
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
    </div>
    <i class="fas fa-plus-circle plus-button" @click="openModal"></i>
    <transaction-modal ref="modal">Add&nbsp</transaction-modal>
    <user></user>
  </div>
</template>

<script>
import User from "@/components/User.vue";
import TransactionModal from "@/components/TransactionModal.vue";

export default {
  components: {
    User,
    TransactionModal
  },
  methods: {
    openModal() {
      this.$refs.modal.open();
    }
  }
};
</script>

<style scoped>
#nav-bar {
  background: linear-gradient(
    45deg,
    rgba(62, 105, 221, 1) 20%,
    rgba(241, 42, 42, 1) 80%
  );
  height: 100vh;
  width: 15em;
  position: fixed;
  z-index: 10;
}
#logo {
  padding-top: 2em;
}
#logo h1 {
  text-align: center;
  font-weight: 900;
  font-style: italic;
  font-size: 4em;
  margin: 0;
  color: white;
}
#rectangle {
  width: 8em;
  height: 0.8em;
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
</style>
