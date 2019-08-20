<template>
  <main class="first-background">
    <div class="heading">
      <div class="titles">
        <h1>Summary</h1>
        <span class="subtitle">Analysis of your transactions</span>
      </div>
      <div class="buttons">
        <button class="expense" :class="{red: isExpense}" @click="setExpense">Expense</button>
        <button class="income" :class="{blue: isIncome}" @click="setIncome">Income</button>
        <button class="all" :class="{blue: isAll}" @click="setAll">All</button>
      </div>
    </div>
    <div class="second-background">
      <filter-bar :color="datePickerColor"></filter-bar>
      <div class="charts" :class="{'charts-no-pie': !displayPie}">
        <pie-chart v-if="displayPie" :startColor="startColor" :endColor="endColor"></pie-chart>
        <line-chart :startColor="startColor" :endColor="endColor"></line-chart>
      </div>
      <div class="table">
        <transaction-table></transaction-table>
      </div>
    </div>
  </main>
</template>

<script>
import FilterBar from "@/components/FilterBar.vue";
import PieChart from "@/components/PieChart.vue";
import LineChart from "@/components/LineChart.vue";
import TransactionTable from "@/components/TransactionTable.vue";

export default {
  components: {
    FilterBar,
    PieChart,
    LineChart,
    TransactionTable
  },
  data() {
    return {
      type: "expense"
    };
  },
  mounted() {
    if(this.$store.state.profile.username === "")
      this.$router.push("login")
  },
  methods: {
    setExpense() {
      this.type = "expense";
      this.$store.commit("SET_FILTER_TYPE", "expense");
    },
    setIncome() {
      this.type = "income";
      this.$store.commit("SET_FILTER_TYPE", "income");
    },
    setAll() {
      this.type = "all";
      this.$store.commit("SET_FILTER_TYPE", "all");
    }
  },
  computed: {
    startColor() {
      if (this.isExpense) {
        return "rgb(241, 42, 42)"; //red
      } else {
        return "rgb(62, 105, 221)"; //blue
      }
    },
    endColor() {
      if (this.isExpense) {
        return "rgb(255, 200, 200)"; //light red
      } else {
        return "rgb(212, 223, 255)"; //light blue
      }
    },
    datePickerColor() {
      return this.isExpense ? "red" : "blue";
    },
    isExpense() {
      return this.type === "expense";
    },
    isIncome() {
      return this.type === "income";
    },
    isAll() {
      return this.type === "all";
    },
    displayPie() {
      return this.type !== "all";
    },
    tableColumns() {
      return this.$store.getters.tableColumns;
    },
    tableData() {
      return this.$store.getters.tableData;
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
.charts-no-pie {
  grid-template-columns: auto;
}
.heading {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.buttons {
  display: grid;
  grid-template-columns: auto auto auto;
  grid-column-gap: 20px;
}
.blue {
  background-color: rgb(62, 105, 221);
  color: white;
}
.red {
  background-color: rgb(241, 42, 42);
  color: white;
}
</style>