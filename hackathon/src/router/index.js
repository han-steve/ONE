import Vue from "vue";
import Router from "vue-router";
import Login from "@/components/Login";
import SignUp from "@/components/SignUp";
import Dashboard from "@/components/Dashboard";
import Accounts from "@/components/Accounts";
import AddEntry from "@/components/AddEntry";
import LineChart from "@/components/LineChart";
import HorzBarChart from "@/components/Budget";


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
        path: "/linechart",
        name: "line",
        component: LineChart
    },
    {
        path: "/budget",
        name: "budget",
        component: HorzBarChart
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
