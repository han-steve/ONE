<template>
  <div>
    <main>
      <h2>Dashboard</h2>
      <p class="subtitle">overview of your financial life</p>
      <label>Search by Category: </label>
      <input type="text" v-model="search">
      <div>
        <ul>
          <li v-for="transaction of list" v-bind:key="transaction['.key']">
            <p>{{transaction.amount}}</p>
          </li>
        </ul>
      </div>
    </main>
  </div>
</template>

<script>
  var user = "Timo";
  import {transactions} from '../firebase';
  import {users} from '../firebase';
  export default {
    name: 'dashboard',
    data(){
      return{
        search: ''
      }
    }, 
    firebase: {
      list: transactions.orderByChild("username").equalTo(user)
    },
    methods: {
      
      // remove(key) {
      //   namesRef.child(key).remove(); 
      // }
    },
    // computed: {
    //   filteredList: function() {
    //     return list.filter((transaction) => {
    //       return list.category.match(this.search)
    //     })
    //   }
    // } 
  };
</script>

<style>
  main {
    background-color: #F7F7FC;
    padding: 3em;
    padding-left: 4em;
  }
  h2 {
    font-size: 2em;
    margin-top: 0;
    margin-bottom: 0;
  }
  .subtitle {
    margin: 0;
    font-weight: 500;
    font-size: 1.2em;
    color: #878B9D;
  }
</style>
