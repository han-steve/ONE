<template>
  <div class="container">
    <edit-entry ref = "edit" id="edit" style="visibility: hidden;" v-bind:transaction="TransactionEditModel" ></edit-entry>
    <main>
      <h1>Welcome {{ this.$store.state.username }}!</h1>
      <h2>Dashboard</h2>
      <p class="subtitle">overview of your financial life</p>
      <div id="filter">
        <div id="search-bar">
          <label>Search by Category:</label>
          <input type="text" class="search form-control" autofocus="true" v-model="search">
        </div>
        <div id="time-bar">
          <label>Search by Date Range:</label>
          <input
            type="date"
            class="datepicker form-control margin-bottom"
            autofocus="true"
            name="date"
            v-model="begin"
          >
          <label>to</label>
          <input
            type="date"
            class="datepicker form-control margin-bottom"
            autofocus="true"
            name="date"
            v-model="end"
          >
        </div>
      </div>
      <!--<div id="navPage">-->
      <!--<i class="fa fa-fast-backward" aria-hidden="true" @click="first()"></i>-->
      <!--<i class="fa fa-step-backward" aria-hidden="true" @click="prev()"></i>-->
      <!--<span id="page">Page {{this.page}} of {{this.max}}</span>-->
      <!--<i class="fa fa-step-forward" aria-hidden="true" @click="next()"></i>-->
      <!--<i class="fa fa-fast-forward" aria-hidden="true" @click="last()"></i>-->
      <!--</div>-->
      <div id="charts">
        <spending-chart :sorted="filteredList"></spending-chart>
        <spending-line-chart :sorted="filteredList"></spending-line-chart>
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
        <hr>
        <ul>
          <div id="data">
            <li
              v-for="transaction of filteredList"
              :sorted="filteredList"
              v-bind:key="transaction['.key']"
            >
              <div class="rows">
                <div class="date">{{ transaction.transaction_date }}</div>
                <div class="amount">{{transaction.amount}}</div>
                <div class="account">{{transaction.account}}</div>
                <div class="category">{{transaction.category}}</div>
                <div class="payee">{{transaction.payee}}</div>
                <div class="memo">{{transaction.memo}}</div>
                <button type="button" class="btn btn-success" @click="edit(transaction)">Edit</button>
                <button type="button" class="btn btn-danger" @click="rm(transaction)">Remove</button>
              </div>
            </li>
          </div>
          <hr>
          <li>
            <div class="rows">
              <div id="sum">Sum:</div>
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
import SpendingChart from "../components/SpendingChart";
import SpendingLineChart from "../components/SpendingLineChart";
import EditEntry from "../components/EditEntry";

import { httpDeleteOptions } from "../lib/http";

export default {
  name: "dashboard",
  components: {
    SpendingChart,
    EditEntry,
    SpendingLineChart
  },
  mounted() {
    if(this.$store.state.username === "")
      this.$router.push({ path: "/" });
  },
  data() {
    return {
      user: "",
      search: "",
      begin: new Date(0),
      end: new Date(Date.now()),
      sum: 0,
      page: 1,
      max: 1,
      TransactionEditModel: {
        username: this.$store.state.username,
        transaction_date: null,
        category: "",
        payee: "",
        amount: 0,
        memo: "",
        account: ""
      }
    };
  },
  computed: {
    filteredList: function() {
      this.sum = 0;
      let final = [];
      let list = this.$store.state.transactions;
      for (let i = 0; i < list.length; i++) {
          if (list[i].category.toLowerCase().match(this.search.toLowerCase())) {
            if (
                    new Date(list[i].transaction_date).getTime()  >=
                    new Date(this.begin).getTime() &&
                    new Date(list[i].transaction_date).getTime() <=
                    new Date(this.end).getTime()
            ) {
              final.push(list[i]);
              this.sum += new Number(list[i].amount);
            }
          }
      }
      return final;
    },
  },
  methods: {
    rm(transaction) {
      fetch("http://127.0.0.1:8080/transactions", httpDeleteOptions(transaction))
              .then(res => res.json())
              .then(response => {
                console.log("response" + response)
              })
              .catch(error => console.error('Error:', error));
      this.$store.dispatch("removeTransactionAction", transaction)
    },
    edit(transaction) {
      this.TransactionEditModel.transaction_date = transaction.transaction_date;
      this.TransactionEditModel.category = transaction.category;
      this.TransactionEditModel.payee = transaction.payee;
      this.TransactionEditModel.amount = transaction.amount;
      this.TransactionEditModel.memo = transaction.memo;
      this.TransactionEditModel.account = transaction.account;
      this.openEditEntry();
    },
    openEditEntry() {
      document.getElementById("edit").style.visibility = "visible";
    },
    closeEditEntry() {
      document.getElementById("edit").style.visibility = "hidden";
    }
  }
};
</script>

<style scoped>
.container {
  padding: 0;
  height: 100vh;
  overflow: scroll;
  max-width: none;
  width: 100%;
}
label {
  font-weight: 500;
  font-size: 1.1em;
}
/*#search-bar {*/
/*margin: 30px auto;*/
/*}*/
/*#search-bar input {*/
/*margin-left: 20px;*/
/*}*/
.checkbox {
  margin: 0.5em;
}
#filter {
  display: flex;
  flex-wrap: wrap;
}
.search {
  width: 9em;
  display: inline-block;
  padding: 1%;
  margin: 0.5em;
  margin-right: 1.5em;
}
.datepicker {
  width: 11em;
  display: inline-block;
  padding: 1%;
  margin: 0.5em;
}
#charts{
  display: grid; 
  height: 395px;
  overflow: hidden;
  grid-template-columns: 1fr 2fr;
}
#table {
  background-color: white;
  border-radius: 30px;
  /* box-shadow: -5px 29px 162px -54px grey; */
  padding-bottom: 15px;
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
#sum {
  margin: 0;
  padding: 0;
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
  grid-template-columns: 1.5fr 1fr 1fr 1fr 1fr 2fr .5fr .5fr;
  margin-top: 0.3em;
}
main {
  background-color: #f7f7fc;
  padding: 3em;
  padding-left: 4em;
  /* height: 100vh; */
}
h2 {
  font-size: 2em;
  margin-top: 0;
  margin-bottom: 0;
}
i {
  margin: 1.5%;
}
#navPage {
  margin-left: 75%;
}
.subtitle {
  margin: 0;
  font-weight: 500;
  font-size: 1.2em;
  color: #878b9d;
}
#data {
  height: 38vh;
  overflow: scroll;
}

@media only screen and (max-width: 1100px) {
  main {
    height: 85vh;
    border-radius: 1em;
    margin: 0 5vw;
    width: 90vw;
  }
  .container {
    margin: 0;
  }
  .datepicker {
    width: auto;
  }
  .search {
    width: auto;
  }
  #data {
    height: 35vh;
  }
}
</style>
