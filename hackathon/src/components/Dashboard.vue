<template>
  <div>
    <main>
      <h2>Dashboard</h2>
      <p class="subtitle">overview of your financial life</p>
      <label>Search by Category: </label>
      <input type="text" v-model="search">
      <div id="table">
        <div class="rows">
              <div class="table-heading">
                Date
              </div>
              <div class="table-heading">
              </div>
              <div class="table-heading">
              </div>
              <div class="table-heading">
              </div>
              <div class="table-heading">
              </div>
              <div class="table-heading">
              </div>
            </div>
        <ul>
          <li v-for="transaction of list" v-bind:key="transaction['.key']">
            <div class="rows">
              <div class="date">
                {{transaction.date}}
              </div>
              <div class="amount">
                {{transaction.amount}}
              </div>
              <div class="account">
                {{transaction.account}}
              </div>
              <div class="category">
                {{transaction.category}}
              </div>
              <div class="payee">
                {{transaction.payee}}
              </div>
              <div class="memo">
                {{transaction.memo}}
              </div>
            </div>
            
          </li>
        </ul>
      </div>
    </main>
  </div>
</template>

<script>
  var user = "Q";
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
  .table-heading {
    text-align: center;

  }
  ul {
      list-style-type: none;
      margin: 0;
      padding: 0;
  }
  .rows {
    text-align: center;
    width: 100%;
    display: grid; 
    grid-template-columns: repeat(6, auto);
  }
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
