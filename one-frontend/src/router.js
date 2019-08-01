import Vue from 'vue';
import Router from 'vue-router';
import Summary from '@/views/Summary.vue';
import Dashboard from '@/views/Dashboard.vue';
import Transactions from '@/views/Transactions.vue';

Vue.use(Router);

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/summary',
      name: 'summary',
      component: Summary
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: Dashboard
    },
    {
      path: '/transactions',
      name: 'transactions',
      component: Transactions
    }
  ]
});
