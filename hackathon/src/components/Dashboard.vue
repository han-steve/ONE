<template>
  <div class="container">
    <main>
      <h2>Dashboard</h2>
      <p class="subtitle">overview of your financial life</p>
      <div id="search-bar">
        <label>Search by Category:</label>
        <input type="text" v-model="search">
      </div>
      <div id="navPage">
        <i class="fa fa-fast-backward" aria-hidden="true" @click="first()"></i>
        <i class="fa fa-step-backward" aria-hidden="true" @click="prev()"></i>
        <span id="page">Page {{this.page}} of {{this.max}}</span>
        <i class="fa fa-step-forward" aria-hidden="true" @click="next()"></i>
        <i class="fa fa-fast-forward" aria-hidden="true" @click="last()"></i>
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
        <hr/>
          <ul>
            <div style="height: 38vh;overflow:scroll;background-color: ghostwhite">
              <li v-for="transaction of filteredList" v-bind:key="transaction['.key']">
                <div class="rows">
                  <div class="date">{{transaction.date}}</div>
                  <div class="amount">{{transaction.amount}}</div>
                  <div class="account">{{transaction.account}}</div>
                  <div class="category">{{transaction.category}}</div>
                  <div class="payee">{{transaction.payee}}</div>
                  <div class="memo">{{transaction.memo}}</div>
                  <button type="button" class="btn btn-danger" @click="rm(transaction)">Remove</button>
                </div>
              </li>
            </div>
            <hr/>
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
var user = "";
import { transactions } from "../firebase";
import { users } from "../firebase";
export default {
  name: "dashboard",
  data() {
    return {
      user: "",
      search: "",
      sum: 0,
      page: 1,
      max: 1
    };
  },
  mounted() {
    console.log("áctivated!");
    this.user = Window.states.username;
  },
  firebase: {
    list: transactions.orderByChild("date")
  },
  computed: {
    filteredList: function() {
      this.sum = 0;
      this.max = Math.ceil(this.list.length/10);
      var final = [];
      for (let i = (this.page - 1) * 10; i < (this.page - 1) * 10 + Math.min((this.list.length - (this.page - 1) * 10), 10); i++) {
        if (
          this.list[i].username === this.user &&
          this.list[i].category.toLowerCase().match(this.search.toLowerCase())
        ) {
          final.push(this.list[i]);
          this.sum += new Number(this.list[i].amount);
        }
      }
      return final;
    }
  },
  methods: {
      first() {
        this.page = 1;
      },
      prev() {
          if(this.page > 1)
            this.page--;
      },
      next() {
          if(this.page < this.max)
              this.page++;
      },
      last() {
        this.page = this.max;
      },
      rm(elm) {
        transactions.child(elm['.key']).remove();
      }
  }
};
</script>

<style scoped>
.container {
  padding: 0;
}
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
  grid-template-columns: 1.5fr 1fr 1fr 1fr 1fr 2fr 1fr;
  margin-top: .3em;
}
main {
  background-color: #f7f7fc;
  padding: 3em;
  padding-left: 4em;
  height: 100vh;
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
</style>
