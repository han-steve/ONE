<template>
  <div id="table">
    <div id="heading" class="rows">
      <div class="table-heading">Date</div>
      <div class="table-heading">Amount</div>
      <div class="table-heading">Account</div>
      <div class="table-heading">Category</div>
      <div class="table-heading">Payee</div>
      <div class="table-heading">Memo</div>
    </div>
    <hr />
    <ul>
      <div id="data">
        <li v-for="transaction of transactions" :key="transaction.transaction_id">
          <div class="rows">
            <div class="date">{{ transaction.date }}</div>
            <div class="amount">{{-transaction.amount}}</div>
            <div class="account">{{transaction.account_id}}</div>
            <div class="category">{{transaction.category}}</div>
            <div class="payee">{{transaction.name}}</div>
            <div class="memo">{{transaction.category_id}}</div>
          </div>
        </li>
      </div>
      <hr />
      <li>
        <div class="rows">
          <div id="sum">Sum:</div>
          <div class="amount">${{this.sum}}</div>
          <div class="account"></div>
          <div class="category"></div>
          <div class="payee"></div>
          <div class="memo"></div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  computed: {
    transactions() {
      return this.$store.getters.filteredTransactions;
    },
    sum() {
      return this.transactions
        .reduce((a, b) => {
          return a - b.amount;
        }, 0)
        .toFixed(2);
    }
  }
};
</script>

<style scoped>
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
  grid-template-columns: 1.5fr 1fr 1fr 1fr 1fr 2fr 0.5fr 0.5fr;
  margin-top: 0.3em;
}
</style>