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
      <filter-bar></filter-bar>
      <div class="charts">
        <pie-chart :startColor="startColor" :endColor="endColor"></pie-chart>
        <line-chart :startColor="startColor" :endColor="endColor"></line-chart>
      </div>
    </div>
  </main>
</template>

<script>
import FilterBar from "@/components/FilterBar.vue";
import PieChart from "@/components/PieChart.vue";
import LineChart from "@/components/LineChart.vue";

export default {
  components: {
    FilterBar,
    PieChart,
    LineChart
  },
  data() {
    return {
      type: "expense"
    };
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
    isExpense() {
      return this.type === "expense";
    },
    isIncome() {
      return this.type === "income";
    },
    isAll() {
      return this.type === "all";
    }
  }
};
</script>

<style scoped>
.charts {
  display: grid;
  width: calc(100% - 1px);
  grid-template-columns: 420px auto;
  grid-column-gap: 20px;
  margin-top: 20px;
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