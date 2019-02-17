import Vue from "vue";
import Router from "vue-router";
import Login from "@/components/Login";
import SignUp from "@/components/SignUp";
import Dashboard from "@/components/Dashboard";
import Accounts from "@/components/Accounts";
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
      path: "/signup",
      name: "create",
      component: SignUp
    },
    {
      path: "/dashboard",
      name: "dashboard",
      component: Dashboard
    },
    {
        path: "/accounts",
        name: "accounts",
        component: Accounts
    },
    {
      path: "/add",
      name: "addEntry",
      component: AddEntry
    }
  ]
});
