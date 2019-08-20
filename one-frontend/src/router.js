import Vue from "vue";
import Router from "vue-router";
import Summary from "@/views/Summary.vue";
import NavBar from "@/components/NavBar.vue";
import Dashboard from "@/views/Dashboard.vue";
import Transactions from "@/views/Transactions.vue";
import store from "./store.js";
import { defaultFilter } from "@/services/Variables.js";

Vue.use(Router);

export default new Router({
  mode: "history",
  routes: [
    {
      path: "/",
      name: "login",
      components: { navbar: NavBar },
      props: { navbar: { login: true } }
    },
    {
      path: "/login",
      name: "login",
      components: { navbar: NavBar },
      props: { navbar: { login: true } }
    },
    {
      path: "/dashboard",
      name: "dashboard",
      components: { default: Dashboard, navbar: NavBar },
      beforeEnter: (to, from, next) => {
        store.commit("SET_FILTERS", defaultFilter);
        next();
      }
    },
    {
      path: "/summary",
      name: "summary",
      components: { default: Summary, navbar: NavBar }
    },
    {
      path: "/transactions",
      name: "transactions",
      components: { default: Transactions, navbar: NavBar }
    }
  ]
});
