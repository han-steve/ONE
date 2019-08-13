<template>
  <div class="background">
    <div class="login">
      <form>
        <h1>Log in</h1>
        <div class="input-fields">
          <div class="input-field">
            <input type="text" placeholder="username" autofocus />
          </div>
          <div class="input-field">
            <input type="text" placeholder="password" autofocus />
          </div>
        </div>
        <button type="submit" @click.prevent="submit">Submit</button>
      </form>
      <div class="social-media">
        <!-- add social media logins -->
      </div>
      <div class="alternate-action">
        <p>Don't have a account?</p>
        <img id="signup-button" src="../assets/signup.svg" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      login: true
    };
  },
  methods: {
    submit() {
      this.$router.push("dashboard");
    }
  }
};
</script>

<style scoped>
.background {
  width: 100vw;
  height: 100vh;
  display: grid;
  justify-items: center;
  align-items: center;
}
.login {
  width: 400px;
}
h1 {
  color: white;
  font-size: 3.5em;
}
input {
  background-color: transparent;
  color: white;
  font-size: 1.2em;
  width: initial;
  padding-left: 0px;
  border-bottom: 3px solid white;
  border-radius: 0;
  transition: 0.2s ease;
}
input:focus {
  border-bottom: 7px solid white;
}
.input-fields {
  padding-left: 50px;
  padding-top: 30px;
}
.input-field {
  margin-bottom: 10px;
}
::placeholder {
  /* Chrome, Firefox, Opera, Safari 10.1+ */
  color: white;
  opacity: 1; /* Firefox */
}

:-ms-input-placeholder {
  /* Internet Explorer 10-11 */
  color: white;
}

::-ms-input-placeholder {
  /* Microsoft Edge */
  color: white;
}
button {
  background-color: transparent;
  color: white;
  margin-left: 215px;
  margin-top: 30px;
  font-size: 1.6em;
  font-weight: 800;
  border: 0px white solid;
  border-radius: 5px;
  padding: 5px 10px;
}
button:focus,
button:hover {
  border: 2px white solid;
}
.alternate-action {
  margin-top: 50px;
  padding-left: 50px;
}
p {
  color: white;
  margin: 0;
  margin-bottom: 10px;
}
#signup-button {
  width: 100px;
}
</style>