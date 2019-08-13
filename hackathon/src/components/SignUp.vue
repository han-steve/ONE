<template>
  <div class="container-1">
    <main>
      <div id="container-2">
        <h2>SignUp</h2>
        <div id="grid">
          <i class="fa fa-user" aria-hidden="true"></i>
          <input
                  type="text"
                  class="form-control"
                  placeholder="Username"
                  @keyup.enter="signup()"
                  required="true"
                  v-model="SignUpModel.username"
          >
          <i class="fa fa-envelope" aria-hidden="true"></i>
          <input
                  type="email"
                  class="form-control"
                  placeholder="Email Address"
                  @keyup.enter="signup()"
                  v-model="SignUpModel.email"
          >
          <i class="fa fa-lock" aria-hidden="true"></i>
          <input
                  type="password"
                  placeholder="Password"
                  class="form-control"
                  @keyup.enter="signup()"
                  required="true"
                  v-model="SignUpModel.password"
          >
          <i class="fa fa-phone" aria-hidden="true"></i>
          <input
                  type="number"
                  step="1"
                  placeholder="Phone Number"
                  class="form-control"
                  @keyup.enter="signup()"
                  v-model="SignUpModel.phoneNumber"
          >
        </div>
        <button class="btn btn-md btn-success float-center" @click="signup()">Sign Up</button>
      </div>
    </main>
  </div>
</template>

<script>
import { httpPostOptions } from "../lib/http";
import MD5 from "crypto-js/md5";

export default {
  name: "signup",
  data() {
    return {
      SignUpModel: {
        username: "",
        email: "",
        password: "",
        phoneNumber: ""
      }
    };
  },
  methods: {
    signup() {
      if(this.SignUpModel.username !== "" && this.SignUpModel.email.match("@") && this.SignUpModel.password.trim().length >= 6) {
        if(this.SignUpModel.phoneNumber !== "" && this.SignUpModel.phoneNumber.trim().length !== 10) {
          this.SignUpModel.phoneNumber = "";
        }
        const model = {
          user_id: -1,
          username: this.SignUpModel.username,
          email: this.SignUpModel.email,
          password: MD5(this.SignUpModel.password).toString(),
          phoneNumber: this.SignUpModel.phoneNumber
        };
        fetch("http://127.0.0.1:8080/users/signup", httpPostOptions(model))
            .then(res => res.json())
            .then(response => {
              if(!response) {
                alert("\"This username or email has been taken.\"")
              }
              else {
                this.resetCurrentUser(this.SignUpModel.username.trim())
                this.$router.push({path: "/dashboard"});
              }
            });
        // .catch(error => {
        //   console.error('Error:', error)
        //   alert("This username or email has been taken.")
        //   });
      }
      else
        alert("Make sure the inputted email is properly formatted and includes '@'")
    },
    resetCurrentUser(username) {
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
#grid {
  display: grid;
  grid-template-columns: 3em auto;
  grid-template-rows: repeat(4, 3em);
  grid-column-gap: 1em;
}
i {
  font-size: 2em;
  text-align: center;
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
