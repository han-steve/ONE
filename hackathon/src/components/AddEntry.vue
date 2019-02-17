<template>
  <div>
    <div class="container jumbo margin-left30">
      <div class="inner-addon left-addon col-lg-6 col-lg-offset-3 margin-top6">
        <h1>Add Transaction</h1>
        <i class="fa fa-calendar" aria-hidden="true"></i>
        <input type="date" class="form-control margin-bottom" required="true" autofocus="true"
               name="date" v-model=EntryModel.date>
        <i class="fa fa-object-group" aria-hidden="true"></i>
        <input type="text" class="form-control margin-bottom" placeholder="Category" required="true" autofocus="true"
               name="category" v-model=EntryModel.category>
        <i class="fa fa-user" aria-hidden="true"></i>
        <input type="text" class="form-control margin-bottom" placeholder="Payee" required="true" autofocus="true"
               name="payee" v-model=EntryModel.payee>
        <i class="fa fa-usd" aria-hidden="true"></i>
        <input type="number" step=".01" class="form-control margin-bottom" placeholder="Amount" required="true" autofocus="true"
               name="amount" v-model=EntryModel.amount>
        <i class="fa fa-sticky-note" aria-hidden="true"></i>
        <input type="text" class="form-control margin-bottom" placeholder="Memo" required="true" autofocus="true"
               name="memo" v-model=EntryModel.memo>
        <i class="fa fa-users" aria-hidden="true"></i>
        <input type="text" class="form-control margin-bottom" placeholder="Account" required="true" autofocus="true"
               name="account" v-model=EntryModel.account>
        <button id="loginButton" class="btn btn-md btn-success float-center" @click="submit()">
          Add Entry
        </button>
      </div>
    </div>

  </div>
</template>

<script>
  import navTpl from "@/components/NavTpl"
  import {users} from '../firebase';
  import {transactions} from '../firebase';

  export default {
    name: 'entry',
    components: {navTpl},
    firebase: {
      names: users
    },
    data() {
      return {
        EntryModel: {
          username: Window.states.username,
          date: new Date().getDate(),
          category: '',
          payee: '',
          amount: '',
          memo: '',
          account: ''
        }
      }
    },
    methods: {
      submit() {
        var curr = this.EntryModel;
        if(curr.username !== '' && curr.amount !== '' && curr.account !== '') {
          transactions.push(curr);
          this.EntryModel = {
              username: Window.states.username,
              date: new Date().getDate(),
              category: '',
              payee: '',
              amount: '',
              memo: '',
              account: ''
          };
          alert("Transaction recorded!");
        }
        else {
          alert("Make sure you fill in amount AND account!");
        }
      }
    }
  }
</script>
