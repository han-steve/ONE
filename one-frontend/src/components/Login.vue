<template>
  <div class="background">
    <div>
      <button class="startBusiness">Start a Business</button>
    </div>
    <div class="login">
      <form>
        <transition name="title" mode="out-in">
          <h1>Don't Taco 'Bout It<br>Just Find It</h1>
        </transition>
        <div class="input-fields filters">
          <div class="input-field">
            <input type="text" placeholder="City" v-model="filter.city">
          </div>
          <div class="input-field">
            <input type="text" placeholder="Cuisine Type" v-model="filter.type">
          </div>
          <div class="input-field">
            <input type="text" placeholder="Dietary Restriction" v-model="filter.restrictions">
          </div>
          <div>
            <button @click="submit()">Search</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      filter: {
        city: "",
        type: "",
        restrictions: ""
      }
    };
  },
  methods: {
    submit() {
      this.$store.dispatch("SET_FILTERS_ACTION", this.filter);
      this.$router.push("dashboard");
    }
  },
  computed: {
    getTypes: function() {
      return this.filter.type.split("\\s*,\\s*");
    },
    getRestrictions: function() {
      return this.filter.restrictions.split("\\s*,\\s*");
    }
  }
};
</script>

<style scoped>
.background {
  width: 100vw;
  /*height: 100vh;*/
  display: grid;
  justify-items: center;
  align-items: center;
}
.login {
  width: 32%;
  margin-top: 12%;
}
h1 {
  color: white;
  font-size: 3.5em;
  text-align: center;
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
  /*margin-left: 215px;*/
  /*margin-top: 30px;*/
  font-size: 1.6em;
  font-weight: 800;
  border: 2px solid transparent;
  border-radius: 5px;
  padding: 5px 10px;
}
.startBusiness {
  margin-top: 1em;
  margin-left: 56em;
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
