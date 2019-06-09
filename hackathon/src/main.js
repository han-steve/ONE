import "./firebase";
import Vue from "vue";
import Vuex from "vuex";
import App from "./App.vue";
import VueFire from "vuefire";
import router from "./router";
import store from "./store/store";

require("bootstrap/dist/css/bootstrap.css");
require("font-awesome/css/font-awesome.css");

Vue.use(VueFire);
Vue.use(Vuex);

Vue.config.productionTip = false;

new Vue({
  store,
  el: "#app",
  render: h => h(App),
  router,
  components: { App },
  template: "<App/>"
});