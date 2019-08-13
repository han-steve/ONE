<template>
  <main class="first-background">
    <div class="heading">
      <div class="titles">
        <h1>Transactions</h1>
        <span class="subtitle">List of your transactions</span>
      </div>
      <div class="buttons">
        <div class="input-container">
          <input type="text" />
          <div id="search-icon">
            <i class="fas fa-search"></i>
            <span class="placeholder">Search Memo</span>
          </div>
        </div>
        <button class="expense" :class="{red: isExpense}" @click="setExpense">Expense</button>
        <button class="income" :class="{blue: isIncome}" @click="setIncome">Income</button>
        <button class="all" :class="{blue: isAll}" @click="setAll">All</button>
      </div>
    </div>
    <div class="second-background">
      <filter-bar :color="datePickerColor"></filter-bar>
      <div class="table">
        <transaction-table></transaction-table>
      </div>
    </div>
  </main>
</template>

<script>
import FilterBar from "@/components/FilterBar.vue";
import TransactionTable from "@/components/TransactionTable.vue";

export default {
  components: {
    FilterBar,
    TransactionTable
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
.heading {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.buttons {
  display: grid;
  grid-template-columns: auto auto auto auto;
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
.input-container {
  position: relative;
}
#search-icon {
  position: absolute;
  display: block;
  left: 15px;
  top: 9px;
  transition: 0.3s ease;
  color: #909090;
}
input:focus ~ #search-icon {
  opacity: 0;
  transform: translateX(-10px);
}
.placeholder {
  font-weight: 700;
  margin-left: 7px;
}
</style>