<template>
  <div class="container-1">
    <main>
      <div id="container-2">
        <h2>SignUp</h2>
        <input type="text" class="form-control" placeholder="Username" v-model="SignUpModel.username">
        <input
          type="password"
          placeholder="Password"
          class="form-control"
          @keyup.enter="signup()"
          v-model="SignUpModel.password"
        >
        <button class="btn btn-md btn-success float-center" @click="signup()">Sign Up</button>
      </div>
    </main>
  </div>
</template>

<script>
import { users } from "../firebase";
export default {
  name: "signup",
  data() {
    return {
      SignUpModel: {
        username: "",
        password: ""
      }
    };
  },
  firebase: {
    names: users
  },
  methods: {
    signup() {
      Window.states.username = this.SignUpModel.username.trim();
      users.push(this.SignUpModel);
      this.$router.push({ path: "/dashboard" });
    }
  }
};
</script>

<style scoped>
main {
  background-color: #f7f7fc;
  height: 100vh;
  padding: 200px 6em 3em 6em;
}
h2 {
  font-size: 2em;
  margin-top: 0;
  margin-bottom: 1em;
}
#signupButton {
  margin-left: 1%;
}
.container-1 {
  display: grid;
  align-items: center;
  align-content: center;
}
#container-2 {
  background-color: white;
  padding: 4em;
  border-radius: 2em;
  box-shadow: -5px 29px 162px -54px grey;
}
input {
    margin-bottom: 1em;
}
</style>