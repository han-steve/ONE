<template>
  <div class="container-1">
    <main>
      <div id="container-2">
        <h2>SignUp</h2>
        <input
          type="text"
          class="form-control"
          placeholder="Username"
          v-model="SignUpModel.username"
        >
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
import { httpPostOptions } from "../lib/http";

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
  methods: {
    signup() {
      const model = {
        username: this.SignUpModel.username,
        password: this.SignUpModel.password
      };
      fetch("http://127.0.0.1:8080/users", httpPostOptions(model))
      .then(res => res.json())
      .then(response => console.log('Success:', JSON.stringify(response)))
.catch(error => console.error('Error:', error));
      this.setCurrentUser(this.SignUpModel.username.trim())
      this.$router.push({ path: "/dashboard" });
    },
    setCurrentUser(username) {
      this.$store.dispatch('setCurrentUserAction', username);
    }
  }
};
</script>

<style scoped>
main {
  background-color: #F7F7FC;
        display: grid;
        height: 100vh;
        align-content: center;
        align-items: center;
        padding: 0 15%;
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
@media only screen and (max-width: 1100px) {
  main{
    height: 85vh;
    border-radius: 1em;
    margin: 0 5vw;
    background-color: transparent;
  }
}
</style>