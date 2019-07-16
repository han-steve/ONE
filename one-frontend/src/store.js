import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

import data from "./transactions_steve.js"; // will be replaced by Ajax
import date from "@/services/DateUtil.js";
import dataUtil from "@/services/DataUtil.js";

export default new Vuex.Store({
  state: {
    transactions: data,
    categories: require("@/data/CategoryTree.json"),
    filters: {
      date: {
        start: date.getThisMonth(),
        end: date.getToday()
      },
      accounts: [],
      categories: [],
      payees: [],
      type: "expense"
    }
  },
  mutations: {
    SET_FILTER_DATE_START(state, startDate) {
      state.filters.date.start = startDate;
    },
    SET_FILTER_DATE_END(state, endDate) {
      state.filters.date.end = endDate;
    },
    SET_FILTER_ACCOUNTS(state, accounts) {
      state.filters.accounts = accounts;
    },
    SET_FILTER_CATEGORIES(state, categories) {
      state.filters.categories = categories;
    },
    SET_FILTER_PAYEES(state, payees) {
      state.filters.payees = payees;
    },
    SET_FILTER_TYPE(state, type) {
      state.filters.type = type;
    }
  },
  actions: {},
  getters: {
    filteredTransactions: state => {
      return state.transactions.filter(transaction => {
        var transactionDate = date.parseDate(transaction.date);
        var isRightType = true;
        if (state.filters.type === "income") {
          isRightType = transaction.amount < 0;
        } else if (state.filters.type === "expense") {
          isRightType = transaction.amount > 0;
        }
        return (
          +transactionDate >= +state.filters.date.start &&
          +transactionDate <= +state.filters.date.end &&
          (state.filters.accounts.length
            ? state.filters.accounts.includes(transaction.account_id)
            : true) &&
          (state.filters.categories.length
            ? state.filters.categories.includes(transaction.category_id)
            : true) &&
          (state.filters.payees.length
            ? state.filters.payees.includes(transaction.name)
            : true) &&
          isRightType
        );
      });
    },
    //only works when the data is sorted by time
    lineChartData: (state, getters) => {
      var transactions = getters.filteredTransactions;
      var data = [];
      if (state.filters.type !== "all") {
        var total = 0;
        for (let i = transactions.length - 1; i >= 0; i--) {
          var transaction = transactions[i];
          total += transaction.amount;
          if (i > 0 && transactions[i - 1].date !== transaction.date) {
            data.push({
              name: transaction.date,
              value: [transaction.date, total]
            });
          }
        }
      }
      //if all
      return data;
    },
    pieChartData: (state, getters) => {
      delete require.cache[
        require.resolve("@/data/CategoryTreeForPieChart.json")
      ];
      var result = require("@/data/CategoryTreeForPieChart.json");
      dataUtil.populateTreeWithValues(getters.filteredTransactions, result);
      result.forEach(el => {
        dataUtil.updateSums(el);
      });
      return result;
    }
  }
});
