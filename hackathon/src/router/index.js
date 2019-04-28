import Vue from "vue";
import Router from "vue-router";
import Login from "@/components/Login";
import SignUp from "@/components/SignUp";
import Dashboard from "@/components/Dashboard";
import Accounts from "@/components/Accounts";
import AddEntry from "@/components/AddEntry";
import spendingchart from "@/components/SpendingChart";
import LineChart from "@/components/LineChart";

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
      path: "/spendingchart",
      name: "chart",
      component: spendingchart
    },
    {
        path: "/linechart",
        name: "line",
        component: LineChart
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
