import "./firebase";
import Vue from "vue";
import App from "./App.vue";
import VueFire from "vuefire";
import router from "./router";


require("bootstrap/dist/css/bootstrap.css");
require("font-awesome/css/font-awesome.css");

Vue.use(VueFire);

Window.states = {
    username: "",
};

Vue.config.productionTip = false;

new Vue({
  el: "#app",
  render: h => h(App),
  router,
  components: {App},
  template: "<App/>"
});
