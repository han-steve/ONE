<template>
  <table class="bubble">
    <thead>
      <tr id="heading">
        <th class="date">Date</th>
        <th class="account">Account</th>
        <th class="category">Category</th>
        <th class="payee">Payee</th>
        <th class="memo">Memo</th>
        <th class="amount">Amount</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="transaction of transactions" :key="transaction.id" @click="edit(transaction.id)">
        <td>{{transaction.date}}</td>
        <td>{{transaction.account}}</td>
        <td>{{transaction.category}}</td>
        <td>{{transaction.payee}}</td>
        <td>{{transaction.memo}}</td>
        <td>{{-transaction.amount}}</td>
      </tr>
    </tbody>
    <tfoot>
      <tr>Total: {{sum}}</tr>
    </tfoot>
  </table>
</template>

<script>
export default {
  computed: {
    transactions() {
      return this.$store.getters.tableData;
    },
    sum() {
      return this.transactions
        .reduce((a, b) => {
          return a - b.amount;
        }, 0)
        .toFixed(2);
    }
  },
  methods: {
    edit(id) {
      console.log(id);
    }
  }
};
</script>

<style scoped>
table {
  border-spacing: 0;
  padding: 15px;
  width: 100%;
  position: relative;
}
th {
  text-align: left;
  font-weight: 700;
  font-size: 1.1em;
}
th,
td {
  padding: 8px 10px;
  border-bottom: 2px solid #eee;
}
tbody tr:hover {
  background-color: #eee;
}
tbody tr {
  cursor: pointer;
}
.date {
  width: 5em;
}
/* it's kinda weird that this is hard-coded since we don't know the account name */
.account {
  width: 6em;
}
tfoot tr {
  text-align: right;
}
</style>