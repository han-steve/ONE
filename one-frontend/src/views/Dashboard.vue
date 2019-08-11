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

export default {
  components: {
    AccountCard,
    PieChart,
    LineChart,
    TransactionTable
  },
  created() {
    this.$store.commit("SET_FILTERS", defaultFilter)
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