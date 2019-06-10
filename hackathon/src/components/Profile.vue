<template>
  <div class="container-1">
    <main>
      <div id="container-2">
        <h2>My Profile</h2>
        <input
          type="text"
          class="form-control"
          placeholder="Username"
          required="true"
          v-model="ProfileModel.username"
        >
        <input
                type="email"
                class="form-control"
                placeholder="Email Address"
                v-model="ProfileModel.email"
        >
        <input
          type="password"
          placeholder="Password"
          class="form-control"
          required="true"
          v-model="ProfileModel.password"
        >
        <input
                type="number"
                placeholder="Phone Number"
                class="form-control"
                v-model="ProfileModel.phoneNumber"
        >
        <button class="btn btn-md btn-success float-center" @click="update()">Update Profile</button>
      </div>
    </main>
  </div>
</template>

<script>
import { httpPutOptions } from "../lib/http";

export default {
  name: "profile",
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
          password_after: this.ProfileModel.password,
          phoneNumber_after: this.ProfileModel.phoneNumber
        };
        fetch("http://127.0.0.1:8080/users", httpPutOptions(model))
            .then(res => res.json())
            .then(response => console.log('Success:', JSON.stringify(response)))
            .catch(error => console.error('Error:', error));
        this.updateProfile(this.ProfileModel)
        alert("Successfully updated profile!")
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