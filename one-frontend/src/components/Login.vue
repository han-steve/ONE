<template>
  <div class="background">
    <div class="login">
      <form>
        <transition name="title" mode="out-in">
          <!-- adding a key makes the transition work -->
          <h1 :key="text">{{ text }}</h1>
        </transition>
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
        <div id="or">or</div>
        <img id="facebook" class="button" src="../assets/facebook.svg" />
      </div>
      <div class="alternate-action">
        <p>{{firstWord}} have a account?</p>
        <img
          id="signup-button"
          v-if="login"
          class="button"
          src="../assets/signup.svg"
          @click="login = false"
        />
        <img
          id="signup-button"
          v-else
          class="button"
          src="../assets/login.svg"
          @click="login = true"
        />
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
  },
  computed: {
    firstWord() {
      return this.login ? "Don't" : "Already";
    },
    text() {
      return this.login ? "Log in" : "Sign up";
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
  width: 350px;
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
.button {
  cursor: pointer;
  transition: transform 0.3s ease;
}
#signup-button {
  width: 100px;
}
.button:hover {
  transform: scale(1.05);
}

.title-enter-active,
.title-leave-active {
  transition: all 0.5s ease;
}
.title-enter,
.title-leave-to {
  opacity: 0;
  transform: translateX(-200px);
}

#or {
  color: white;
  font-weight: bold;
  font-size: 1.1em;
  /* using margin instead of center align cause not everything's centered */
  margin: 25px 0 20px 170px;
}
#facebook {
  margin-left: 50px;
  width: 280px;
}
</style>