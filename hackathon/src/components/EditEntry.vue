<template>
    <div id="background">
        <div class="container">
            <div class="inner-container">
                <h1>Edit Transaction</h1>
                <div id="grid">
                    <i class="fa fa-calendar" aria-hidden="true"></i>
                    <input
                            type="date"
                            class="form-control margin-bottom"
                            required="true"
                            autofocus="true"
                            name="date"
                            v-model="date"
                    >
                    <i class="fa fa-object-group" aria-hidden="true"></i>
                    <input
                            type="text"
                            class="form-control margin-bottom"
                            placeholder="Category"
                            required="true"
                            autofocus="true"
                            name="category"
                            v-model="category"
                    >
                    <i class="fa fa-user" aria-hidden="true"></i>
                    <input
                            type="text"
                            class="form-control margin-bottom"
                            placeholder="Payee"
                            required="true"
                            autofocus="true"
                            name="payee"
                            v-model="payee"
                    >
                    <i class="fa fa-usd" aria-hidden="true"></i>
                    <input
                            type="number"
                            step=".01"
                            class="form-control margin-bottom"
                            placeholder="Amount"
                            required="true"
                            autofocus="true"
                            name="amount"
                            v-model="amount"
                    >
                    <i class="fa fa-sticky-note" aria-hidden="true"></i>
                    <input
                            type="text"
                            class="form-control margin-bottom"
                            placeholder="Memo"
                            required="true"
                            autofocus="true"
                            name="memo"
                            v-model="memo"
                    >
                    <i class="fa fa-users" aria-hidden="true"></i>
                    <input
                            type="text"
                            class="form-control margin-bottom"
                            placeholder="Account"
                            required="true"
                            autofocus="true"
                            name="account"
                            v-model="account"
                    >
                </div>
                <button
                        id="loginButton"
                        class="btn btn-md btn-success float-center"
                        @click="submit()"
                >Edit Entry</button>
            </div>
        </div>
    </div>
</template>

<script>
    import navTpl from "@/components/NavTpl";
    import { users } from "../firebase";
    import { transactions } from "../firebase";
    export default {
        name: "edit",
        components: { navTpl },
        firebase: {
            names: users
        },
        props: ['id', 'date', 'amount', 'account', 'category', 'payee', 'memo'],
        data() {
            return {
                EditModel: {
                    username: Window.states.username,
                    edit: {
                        id: this.id,
                        date: this.date,
                        category: this.category,
                        payee: this.payee,
                        amount: this.amount,
                        memo: this.memo,
                        account: this.account
                    }
                }
            };
        },
        methods: {
            submit() {
                var curr = this.EditModel;
                console.log(curr.edit);
                if (curr.username !== "" && curr.edit.amount !== "" && curr.edit.account !== "" && curr.edit.date.toString().split("-").length === 3) {
                    var updates = {};
                    updates['transactions/' + this.id] = curr.edit;
                    firebase.database().ref().update(updates);
                    //db.ref("transactions/" + )
                    transactions.push(curr);
                    this.EditModel = {
                        username: Window.states.username,
                        edit: {
                            id: this.id,
                            date: this.date,
                            category: this.category,
                            payee: this.payee,
                            amount: this.amount,
                            memo: this.memo,
                            account: this.account
                        }
                    };
                    alert("Transaction updated!");
                } else {
                    alert("Make sure you fill in amount, account, AND proper date!");
                }
            }
        }
    };
</script>

<style scoped>
    #background {
        background-color: #f7f7fc;
    }
    .container {
        display: grid;
        align-content: center;
        align-items: center;
        height: 100vh;
    }
    .inner-container {
        margin: 15%;
        background-color: white;
        padding: 3em;
        border-radius: 20px;
        box-shadow: -5px 29px 162px -54px grey;
    }
    #grid {
        display: grid;
        grid-template-columns: 3em auto;
        grid-template-rows: repeat(6, 3em);
        grid-column-gap: 1em;
    }
    i {
        font-size: 2em;
        text-align: center;
    }
    h1 {
        margin-bottom: 1em;
    }
    button {
        margin: 1em auto;
    }
    @media only screen and (max-width: 1100px) {
        #background{
            height: 85vh;
            border-radius: 1em;
            margin: 0 5vw;
            background-color: transparent;
        }
        .container {
            height: 85vh;
        }
    }


</style>