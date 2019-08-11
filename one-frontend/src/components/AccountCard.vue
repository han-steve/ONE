<template>
  <div class="bubble background">
    <div class="left">
      <h3>{{account.name}}</h3>
      <span class="description">{{account.subtype}}</span>
    </div>
    <div class="right">
      <span
        class="big-number"
        :class="account.balances.current > 0 ? 'blue' : 'red'"
      >{{account.balances.current}}</span>
    </div>
  </div>
</template>

<script>
export default {
  props: ["id"],
  computed: {
    account() {
      return this.$store.getters.getAccount(this.id);
    }
  }
};
</script>

<style scoped>
.background {
  display: flex;
  flex-direction: row;
  width: 250px;
  padding: 20px;
  align-items: center;
  justify-content: space-between;
}
.big-number {
  font-size: 1.8em;
  font-weight: 800;
  font-style: italic;
}
.description {
  font-weight: 600;
  color: #686868;
}
.red {
  color: rgb(241, 42, 42);
}
.blue {
  color: rgb(62, 105, 221);
}
</style>