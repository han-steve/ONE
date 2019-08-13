<template>
  <main class="first-background">
    <h1>Dashboard</h1>
    <span class="subtitle">Overview of your financial life</span>
    <div class="second-background">
      <div class="section">
        <h2>Accounts</h2>
        <account-card id="bOwYxeA3rvTa1b7Q8YzqfRANOQ6MVqtqznxKV"></account-card>
      </div>
      <div class="section">
        <h2>Charts</h2>
        <div class="charts">
          <pie-chart startColor="rgb(241, 42, 42)" endColor="rgb(255, 200, 200)"></pie-chart>
          <line-chart startColor="rgb(62, 105, 221)" endColor="rgb(212, 223, 255)"></line-chart>
        </div>
      </div>
      <div class="section">
        <h2>Recent Transactions</h2>
        <transaction-table></transaction-table>
      </div>
    </div>
  </main>
</template>

<script>
import AccountCard from "@/components/AccountCard.vue";
import PieChart from "@/components/PieChart.vue";
import LineChart from "@/components/LineChart.vue";
import TransactionTable from "@/components/TransactionTable.vue";
import {defaultFilter} from "@/services/Variables.js"
import {httpGetOptions} from "../http";


export default {
  components: {
    AccountCard,
    PieChart,
    LineChart,
    TransactionTable
  },
  created() {
    this.$store.commit("SET_FILTERS", defaultFilter)
  },
  mounted() {
    fetch("http://127.0.0.1:8080/users/" + this.$store.state.profile.username, httpGetOptions())
      .then(res => res.json())
      .then(response => {
          let user = response;
          this.$store.dispatch("updateProfileAction", user);
          fetch(
              "http://127.0.0.1:8080/transactions/" + this.$store.state.profile.user_id,
              httpGetOptions()
          )
              .then(res => res.json())
              .then(response => {
                  this.$store.dispatch("clearCurrentStoredTransactionsAction");
                  let transactions = response.transactions;
                  for (let i = 0; i < transactions.length; i++) {
                      transactions[i].isPlaid = false;
                      this.$store.dispatch("addTransactionAction", transactions[i]);
                  }
              })
              .catch(error => console.error("Error:", error));
          fetch("http://127.0.0.1:8080/bank/" + this.$store.state.profile.user_id, httpGetOptions())
              .then(res => res.json())
              .then(response => {
                  let banks = response['banks'];
                  let numBanks = banks.length;
                  for(let i = 0; i < numBanks; i++) {
                      this.$store.dispatch("addBankConnectionAction", banks[i]);
                      this.getTransactions(banks[i].access_token)
                  }
              })
              .catch(error => console.error('Error:', error));
      })
      .catch(error => console.error("Error:", error));
  },
  methods: {
    getTransactions(access_token) {
                fetch("http://127.0.0.1:8080/bank/" + this.$store.state.profile.user_id + "/transactions/" + access_token, httpGetOptions())
                    .then(res => res.json())
                    .then(data => {
                        data.transactions.forEach(t => {
                            const model = {
                                isPlaid: true,
                                user_id: this.$store.state.profile.user_id,
                                transaction_id: t.transaction_id,
                                transaction_date: t.transaction_date,
                                category_id: t.category_id,
                                category: t.category,
                                account: t.account,
                                payee: t.payee,
                                amount: -Number(t.amount),
                                memo: t.memo
                            };
                            this.$store.dispatch("addTransactionAction", model);
                        });
                    });
            }
  }
};
</script>

<style scoped>
.charts {
  display: grid;
  /* Using calc here cause for some reason 100% causes horizontal scroll bar */
  width: calc(100% - 1px);
  /* Would like to use '420px auto' but there are issues with resizing */
  grid-template-columns: 420px calc(100% - 440px);
  grid-column-gap: 20px;
  margin-top: 20px;
  margin-bottom: 20px;
}
.section {
  margin-bottom: 20px;
}
</style>