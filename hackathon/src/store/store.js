import Vue from "vue";
import Vuex from "vuex";
import { isEquivalent } from "../lib/lib";

Vue.use(Vuex);

const state = {
    profile: {
        user_id: -1,
        username: '',
        email: '',
        password: '',
        phoneNumber: '',
    },
    transactions: [],
    connections: []
}

const getters = {
    getCurrentUser: function(state) {
        return state.profile.username;
    }
}

const mutations = {
    setCurrentUserMutations: function(state, nextUsername) {
        state.profile.username = nextUsername;
    },
    updateProfileMutation: function(state, model) {
        state.profile = model;
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
            if(state.transactions[i].transaction_id === transaction.transaction_id) {
                state.transactions[i].transaction_date = transaction.transaction_date;
                state.transactions[i].category = transaction.category;
                state.transactions[i].account = transaction.account;
                state.transactions[i].payee = transaction.payee;
                state.transactions[i].amount = transaction.amount;
                state.transactions[i].memo = transaction.memo;
                break;
            }
        }
        state.transactions.sort((a, b) => new Date(b.transaction_date).getTime() - new Date(a.transaction_date).getTime());
    },
    removeTransactionMutation: function(state, transaction) {
        var index = 0;
        while(!isEquivalent(state.transactions[index], transaction)) index++;
        state.transactions.splice(index, 1);
    },
    addBankConnectionMutation: function(state, transaction) {
        state.connections.push(transaction);
    },
    removeBankConnectionMutation: function(state, connection) {
        var index = 0;
        while (!isEquivalent(state.connections[index], connection)) index++;
        state.connections.splice(index, 1);
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
    },
    addBankConnectionAction: function(context, connection) {
        context.commit('addBankConnectionMutation', connection);
    },
    removeBankConnectionAction: function(context, connection) {
        context.commit('removeBankConnectionMutation', connection);
    }

}

export default new Vuex.Store({
    strict: true,
    state,
    getters,
    mutations,
    actions
});
