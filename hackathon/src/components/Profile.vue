<template>
  <div class="container-1">
    <main>
      <div id="container-2">
        <h2>My Profile</h2>
        <div id="grid">
        <i class="fa fa-user" aria-hidden="true"></i>
        <input
          type="text"
          class="form-control"
          placeholder="Username"
          required="true"
          v-model="ProfileModel.username"
        >
        <i class="fa fa-envelope" aria-hidden="true"></i>
        <input
                type="email"
                class="form-control"
                placeholder="Email Address"
                v-model="ProfileModel.email"
        >
        <i class="fa fa-lock" aria-hidden="true"></i>
        <input
          type="password"
          placeholder="Password"
          class="form-control"
          required="true"
          v-model="ProfileModel.password"
        >
        <i class="fa fa-phone" aria-hidden="true"></i>
        <input
                type="number"
                placeholder="Phone Number"
                class="form-control"
                v-model="ProfileModel.phoneNumber"
        >
        </div>
        <button class="btn btn-md btn-success float-center" @click="update()">Update Profile</button>
      </div>
    </main>
  </div>
</template>

<script>
import { httpPutOptions } from "../lib/http";
import MD5 from "crypto-js/md5";

export default {
  name: "profile",
  mounted() {
    if(this.$store.state.username === "")
      this.$router.push({ path: "/" });
  },
  data() {
    return {
      ProfileModel: {
        username: this.$store.state.username,
        email: this.$store.state.email,
        password: this.$store.state.password,
        phoneNumber: this.$store.state.phoneNumber
      }
    };
  },
  methods: {
    update() {
      if(this.ProfileModel.username !== "" && this.ProfileModel.email.match("@") && this.ProfileModel.password.trim().length >= 6) {
        if(this.ProfileModel.phoneNumber !== "" && this.ProfileModel.phoneNumber.trim().length !== 10) {
          this.ProfileModel.phoneNumber = "";
        }
        const model = {
          username_before: this.$store.state.username,
          email_before: this.$store.state.email,
          password_before: this.$store.state.password,
          phoneNumber_before: this.$store.state.phoneNumber,
          username_after: this.ProfileModel.username,
          email_after: this.ProfileModel.email,
          password_after: MD5(this.ProfileModel.password).toString(),
          phoneNumber_after: this.ProfileModel.phoneNumber
        };
        fetch("http://127.0.0.1:8080/users", httpPutOptions(model))
            .then(res => res.json())
            .then(response => {
              this.updateProfile(this.ProfileModel)
              alert("Successfully updated profile!")
              console.log('Success:', JSON.stringify(response))
            }).catch(error => {
              alert("The username or email has been taken.")
              console.error('Error:', error)
            });

      }
    },
    updateProfile(model) {
      this.$store.dispatch('updateProfileAction', model);
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