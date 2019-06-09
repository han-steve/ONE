import Vue from "vue";
import Vuex from "vuex";
import { isEquivalent } from "../lib/lib";

Vue.use(Vuex);

const state = {
    username: '',
    transactions: []
}

const getters = {
    getCurrentUser: function(state) {
        return state.username;
    }
}

const mutations = {
    setCurrentUserMutations: function(state, nextUsername) {
        state.username = nextUsername;
    },
    clearCurrentStoredTransactionsMutation: function(state) {
        state.transactions = []
    },
    addTransactionMutation: function(state, transaction) {
        state.transactions.push(transaction);
    },
    editTransactionMutation: function(state, transaction) {
        for(let i = 0; i < state.transactions.length; i++) {
            let current = state.transactions[i];
            if(current.transaction_date === transaction.transaction_date_before && current.category === transaction.category_before &&
                current.payee === transaction.payee_before && current.amount === transaction.amount_before && current.memo === transaction.memo_before &&
                current.account === transaction.account_before) {
                state.transactions[i].transaction_date = transaction.transaction_date_after;
                state.transactions[i].category = transaction.category_after;
                state.transactions[i].payee = transaction.payee_after;
                state.transactions[i].amount = transaction.amount_after;
                state.transactions[i].memo = transaction.memo_after;
                state.transactions[i].account = transaction.account_after;
            }
        }
        state.transactions.sort((a, b) => new Date(b.transaction_date).getTime() - new Date(a.transaction_date).getTime());
    },
    removeTransactionMutation: function(state, transaction) {
        var index = 0;
        while(!isEquivalent(state.transactions[index], transaction)) index++;
        state.transactions.splice(index, 1);
    }
}

const actions = {
    setCurrentUserAction: function(context, username) {
        context.commit('setCurrentUserMutations', username);
    },
    clearCurrentStoredTransactionsAction: function(context) {
        context.commit("clearCurrentStoredTransactionsMutation");
    },
    addTransactionAction: function(context, transaction) {
        context.commit('addTransactionMutation', transaction);
    },
    editTransactionAction: function(context, transaction) {
        context.commit('editTransactionMutation', transaction);
    },
    removeTransactionAction: function(context, transaction) {
        context.commit('removeTransactionMutation', transaction);
    }
}

export default new Vuex.Store({
  strict: true,
    state,
    getters,
    mutations,
    actions
});
