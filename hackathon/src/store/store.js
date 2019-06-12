import Vue from "vue";
import Vuex from "vuex";
import { isEquivalent } from "../lib/lib";

Vue.use(Vuex);

const state = {
    username: '',
    email: '',
    password: '',
    phoneNumber: '',
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
    updateProfileMutation: function(state, model) {
        state.username = model.username;
        state.email = model.email;
        state.password = model.password;
        state.phoneNumber = model.phoneNumber;
    },
    clearCurrentStoredTransactionsMutation: function(state) {
        state.transactions = []
    },
    addTransactionMutation: function(state, transaction) {
        state.transactions.push(transaction);
        state.transactions.sort((a, b) => new Date(b.transaction_date).getTime() - new Date(a.transaction_date).getTime());
    },
    editTransactionMutation: function(state, transaction) {
        for(let i = 0; i < state.transactions.length; i++) {
            let current = state.transactions[i];
            if(current.id === transaction.id) {
                state.transactions[i].transaction_date = transaction.transaction_date;
                state.transactions[i].category = transaction.category;
                state.transactions[i].payee = transaction.payee;
                state.transactions[i].amount = transaction.amount;
                state.transactions[i].memo = transaction.memo;
                state.transactions[i].account = transaction.account;
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
    updateProfileAction: function(context, model) {
        context.commit('updateProfileMutation', model);
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
