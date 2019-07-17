import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

import data from "./transactions_steve.js"; // will be replaced by Ajax
import date from "@/services/DateUtil.js";
import dataUtil from "@/services/DataUtil.js";

export default new Vuex.Store({
  state: {
    transactions: data,
    balance: 100, //hardcoded sample
    accounts: [
      {
        account_id: "bOwYxeA3rvTa1b7Q8YzqfRANOQ6MVqtqznxKV",
        balances: {
          available: 1175.47,
          current: 1184.35,
          iso_currency_code: "USD",
          limit: null,
          unofficial_currency_code: null
        },
        mask: "5373",
        name: "CHASE COLLEGE",
        official_name: null,
        subtype: "checking",
        type: "depository"
      }
    ],
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
      var startDate = state.filters.date.start;
      var endDate = state.filters.date.end;
      var data = [];
      if (state.filters.type !== "all") {
        let total = 0;
        let currentDay = new Date(startDate.getTime()); //clone the start date so we don't mutate the filter
        let index = transactions.length - 1;
        // doing it this way so that every day will have a datapoint
        while (+currentDay <= +endDate) {
          while (
            index >= 0 &&
            +date.parseDate(transactions[index].date) === +currentDay
          ) {
            total += Math.abs(transactions[index].amount);
            index--;
          }
          let currentDayString = date.stringifyDate(currentDay);
          data.push({
            name: currentDayString,
            value: [currentDayString, total]
          });
          currentDay.setDate(currentDay.getDate() + 1);
        }
      } else {
        //work backwards from balance
        let balance = state.balance;
        let currentDay = new Date(endDate.getTime());
        let index = 0;
        data.push({
          name: date.stringifyDate(currentDay),
          value: [date.stringifyDate(currentDay), balance]
        });
        //using > instead of >= to compensate for shifting all balances by one day
        while (+currentDay > +startDate) {
          while (
            index < transactions.length &&
            +date.parseDate(transactions[index].date) === +currentDay
          ) {
            balance += transactions[index].amount;
            index++;
          }
          currentDay.setDate(currentDay.getDate() - 1); //fix off-by-one error when working backwards
          let currentDayString = date.stringifyDate(currentDay);
          data.push({
            name: currentDayString,
            value: [currentDayString, balance]
          });
        }
      }
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
