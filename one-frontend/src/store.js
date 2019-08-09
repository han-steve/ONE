import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

import data from "./transactions_steve.js"; // will be replaced by Ajax
import date from "@/services/DateUtil.js";
import dataUtil from "@/services/DataUtil.js";

export default new Vuex.Store({
  state: {
    // positive is spent, negative is earned
    transactions: data,
    balance: 100, //hardcoded sample
    accounts: [
      {
        id: "bOwYxeA3rvTa1b7Q8YzqfRANOQ6MVqtqznxKV",
        balances: {
          available: 1175.47,
          current: 1184.35,
          iso_currency_code: "USD",
          limit: null,
          unofficial_currency_code: null
        },
        name: "Chase College",
        subtype: "checking",
        type: "depository"
      }
    ],
    categories: require("@/data/CategoryTreeForPieChart.json"),
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
    SET_FILTER_DATE_RANGE(state, dateRange) {
      state.filters.date = dateRange;
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
    getTransaction: state => id => {
      var transaction = state.transactions.find(
        transaction => transaction.transaction_id === id
      );
      return {
        id: transaction.transaction_id,
        date: date.parseDate(transaction.date),
        amount: transaction.amount.toFixed(2),
        account: transaction.account_id,
        category: transaction.category_id,
        payee: transaction.name,
        memo: "" //nothing yet
      };
    },
    getAccount: state => id => {
      return state.accounts.find(account => account.id === id);
    },
    filteredTransactions: state => {
      var subcategories = [];
      state.filters.categories.forEach(root => {
        dataUtil.findSubCategories(
          dataUtil.findCategory(root, state.categories),
          subcategories
        );
      });
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
          (subcategories.length
            ? subcategories.includes(transaction.category_id)
            : true) &&
          (state.filters.payees.length
            ? state.filters.payees.includes(transaction.name)
            : true) &&
          isRightType
        );
      });
    },
    //the same thing except doesn't filter by category
    filteredTransactionsNoCategory: state => {
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
    // I tried to make a new tree with the root being the category filter,
    // but it didn't work because to populate the tree, we need the find category method.
    // However, that function only works with a full tree (3 levels), which is why the
    // new, smaller tree cannot be searched and filled with values.
    // Currently, the tree is always a full tree, no matter filtered by which category.
    // Only the pie chart is doing the filtering based on the categories filter.
    pieChartData: (state, getters) => {
      var result = require("@/data/CategoryTreeForPieChart.json");
      dataUtil.populateTreeWithValues(
        getters.filteredTransactionsNoCategory,
        result
      );
      result.forEach(el => {
        dataUtil.updateSums(el);
      });
      return result;
    },
    tableData: (state, getters) => {
      var result = [];
      getters.filteredTransactions.forEach(transaction => {
        result.push({
          id: transaction.transaction_id,
          date: transaction.date,
          amount: transaction.amount.toFixed(2),
          account: state.accounts.find(
            account => account.id === transaction.account_id
          ).name,
          category: dataUtil.findCategory(
            transaction.category_id,
            state.categories
          ).name, //will be replaced by id
          payee: transaction.name,
          memo: "" //nothing yet
        });
      });
      return result;
    },
    getAccounts: state => {
      var result = [];
      state.accounts.forEach(account => {
        result.push({
          id: account.id,
          label: account.name
        });
      });
      return result;
    },
    getPayees: state => {
      // using set to prevent duplicates
      var set = new Set();
      state.transactions.forEach(transaction => {
        set.add(transaction.name);
      });
      // put it in the object form that treeselect requires
      var result = [];
      set.forEach(name => {
        result.push({
          id: name,
          label: name
        });
      });
      return result;
    }
  }
});
