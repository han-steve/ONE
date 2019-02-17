<template>
  <div id="background">
    <div class="container">
      <div class="inner-container">
        <h1>Add Transaction</h1>
        <div id="grid">
          <i class="fa fa-calendar" aria-hidden="true"></i>
          <input
            type="date"
            class="form-control margin-bottom"
            required="true"
            autofocus="true"
            name="date"
            v-model="EntryModel.date"
          >
          <i class="fa fa-object-group" aria-hidden="true"></i>
          <input
            type="text"
            class="form-control margin-bottom"
            placeholder="Category"
            required="true"
            autofocus="true"
            name="category"
            v-model="EntryModel.category"
          >
          <i class="fa fa-user" aria-hidden="true"></i>
          <input
            type="text"
            class="form-control margin-bottom"
            placeholder="Payee"
            required="true"
            autofocus="true"
            name="payee"
            v-model="EntryModel.payee"
          >
          <i class="fa fa-usd" aria-hidden="true"></i>
          <input
            type="number"
            step=".01"
            class="form-control margin-bottom"
            placeholder="Amount"
            required="true"
            autofocus="true"
            name="amount"
            v-model="EntryModel.amount"
          >
          <i class="fa fa-sticky-note" aria-hidden="true"></i>
          <input
            type="text"
            class="form-control margin-bottom"
            placeholder="Memo"
            required="true"
            autofocus="true"
            name="memo"
            v-model="EntryModel.memo"
          >
          <i class="fa fa-users" aria-hidden="true"></i>
          <input
            type="text"
            class="form-control margin-bottom"
            placeholder="Account"
            required="true"
            autofocus="true"
            name="account"
            v-model="EntryModel.account"
          >
        </div>
        <button
          id="loginButton"
          class="btn btn-md btn-success float-center"
          @click="submit()"
        >Add Entry</button>
      </div>
    </div>
  </div>
</template>

<script>
import navTpl from "@/components/NavTpl";
import { users } from "../firebase";
import { transactions } from "../firebase";

export default {
  name: "entry",
  components: { navTpl },
  firebase: {
    names: users
  },
  data() {
    return {
      EntryModel: {
        username: Window.states.username,
        date: new Date().getDate(),
        category: "",
        payee: "",
        amount: "",
        memo: "",
        account: ""
      }
    };
  },
  methods: {
    submit() {
      var curr = this.EntryModel;
      if (curr.username !== "" && curr.amount !== "" && curr.account !== "") {
        transactions.push(curr);
        this.EntryModel = {
          username: Window.states.username,
          date: new Date().getDate(),
          category: "",
          payee: "",
          amount: "",
          memo: "",
          account: ""
        };
        alert("Transaction recorded!");
      } else {
        alert("Make sure you fill in amount AND account!");
      }
    }
  }
};
</script>

<style scoped>
#background {
  background-color: #f7f7fc;
}
.container {
  display: grid;
  align-content: center;
  align-items: center;
  height: 100vh;
}
.inner-container {
  margin: 15%;
  background-color: white;
  padding: 3em;
  border-radius: 20px;
  box-shadow: -5px 29px 162px -54px grey;
}
#grid {
  display: grid; 
  grid-template-columns: 3em auto; 
  grid-template-rows: repeat(6, 3em);
  grid-column-gap: 1em;
}
i {
  font-size: 2em;
  text-align: center;
}
h1 {
  margin-bottom: 1em;
}
button {
  margin: 1em auto;
}
</style>