<template>
  <div class="container">
    <main>
      <h2>Dashboard</h2>
      <p class="subtitle">overview of your financial life</p>
      <div id="search-bar">
        <label>Search by Category:</label>
        <input type="text" v-model="search">        
      </div>

      <div id="table">
        <div id="heading" class="rows">
          <div class="table-heading">Date</div>
          <div class="table-heading">Amount</div>
          <div class="table-heading">Account</div>
          <div class="table-heading">Category</div>
          <div class="table-heading">Payee</div>
          <div class="table-heading">Memo</div>
        </div>
        <ul>
          <li v-for="transaction of filteredList" v-bind:key="transaction['.key']">
            <div class="rows">
              <div class="date">{{transaction.date}}</div>
              <div class="amount">{{transaction.amount}}</div>
              <div class="account">{{transaction.account}}</div>
              <div class="category">{{transaction.category}}</div>
              <div class="payee">{{transaction.payee}}</div>
              <div class="memo">{{transaction.memo}}</div>
            </div>
          </li>
          <hr/>
          <li>
            <div class="rows">
              <div class="date"></div>
              <div class="amount">${{this.sum.toFixed(2)}}</div>
              <div class="account"></div>
              <div class="category"></div>
              <div class="payee"></div>
              <div class="memo"></div>
            </div>
          </li>
        </ul>
      </div>
    </main>
  </div>
</template>

<script>
var user = "";
import { transactions } from "../firebase";
import { users } from "../firebase";
export default {
  name: "dashboard",
  data() {
    return {
      user: "",
      search: "",
      sum: 0
    };
  },
  mounted() {
    console.log("áctivated!");
    this.user = Window.states.username;
  },
  firebase: {
    list: transactions
  },
  computed: {
    filteredList: function() {
      var final = [];
      for (let i = 0; i < Math.min(this.list.length, 10); i++) {
        if (
          this.list[i].username === this.user &&
          this.list[i].category.toLowerCase().match(this.search.toLowerCase())
        ) {
          final.push(this.list[i]);
          console.log(this.list[i].amount);
          this.sum += new Number(this.list[i].amount);
        }
      }
      return final;
    },
  }
};
</script>

<style>
label {
  font-weight: 500;
  font-size: 1.1em;
}
#search-bar {
  margin: 30px auto;
}
#search-bar input {
  margin-left: 20px;
}
#table {
  background-color: white;
  border-radius: 10px;
  box-shadow: -5px 29px 162px -54px grey;
  padding-bottom: 4px;
}
.table-heading {
  text-align: center;
  padding-top: 12px;
  font-size: 1.2em;
  font-weight: 700;
}
#heading {
  margin-bottom: 10px;
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
  grid-template-columns: 1.5fr 1fr 1fr 1fr 1fr 2fr;
}
main {
  background-color: #f7f7fc;
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
  color: #878b9d;
}
</style>
