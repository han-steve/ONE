import Vue from "vue";
import Router from "vue-router";
import Login from "@/components/Login";
import Dashboard from "@/components/Dashboard";
import AddEntry from "@/components/AddEntry";

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: "/",
      name: "signin",
      component: Login
    },
    {
      path: "/dashboard",
      name: "dashboard",
      component: Dashboard
    },
    {
      path: "/add",
      name: "addEntry",
      component: AddEntry
    }
  ]
});
