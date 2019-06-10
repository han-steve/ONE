<template>
    <div id="background">
        <div class="container">
            <div class="inner-container">
                <h1>Edit Transaction</h1>
                <div id="transactionType">
                    <label>Earned</label>
                    <input v-model="transactionValue" type="range" min="0" max="1">
                    <label>Spent</label>
                </div>
                <div id="grid">
                    <i class="fa fa-calendar" aria-hidden="true"></i>
                    <input
                            type="date"
                            class="form-control margin-bottom"
                            required="true"
                            autofocus="true"
                            name="date"
                            v-model="editModel.transaction_date"
                    >
                    <i class="fa fa-object-group" aria-hidden="true"></i>
                    <input
                            type="text"
                            class="form-control margin-bottom"
                            placeholder="Category"
                            required="true"
                            autofocus="true"
                            name="category"
                            v-model="editModel.category"
                    >
                    <i class="fa fa-user" aria-hidden="true"></i>
                    <input
                            type="text"
                            class="form-control margin-bottom"
                            :placeholder="payerPayee"
                            required="true"
                            autofocus="true"
                            name="payee"
                            v-model="editModel.payee"
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
                            v-model="editModel.amount"
                    >
                    <i class="fa fa-sticky-note" aria-hidden="true"></i>
                    <input
                            type="text"
                            class="form-control margin-bottom"
                            placeholder="Memo"
                            required="true"
                            autofocus="true"
                            name="memo"
                            v-model="editModel.memo"

                    >
                    <i class="fa fa-users" aria-hidden="true"></i>
                    <input
                            type="text"
                            class="form-control margin-bottom"
                            placeholder="Account"
                            required="true"
                            autofocus="true"
                            name="account"
                            v-model="editModel.account"
                    >
                </div>
                <button
                        id="loginButton"
                        class="btn btn-md btn-success float-center"
                        @click="submit()"
                >Edit Entry</button>
                <button
                        id="closeButton"
                        class="btn btn-md btn-danger float-center"
                        @click="close()"
                > X </button>
            </div>
        </div>
    </div>
</template>

<script>
    import { httpPutOptions } from "../lib/http"
    import Dashboard from "./Dashboard";

    export default {
        name: "edit",
        props: ["transaction"],
        data() {
            return {
                transactionValue: this.editModel.computedTransactionValue
            }
        },
        computed: {
            editModel() {
                return {
                    username: this.transaction.username,
                    computedTransactionValue: function() {
                        if(this.transaction.amount > 0)
                            return "0";
                        return "1";
                    },
                    transaction_date: this.transaction.transaction_date,
                    category: this.transaction.category,
                    payee: this.transaction.payee,
                    amount: Math.abs(this.transaction.amount),
                    memo: this.transaction.memo,
                    account: this.transaction.account
                }
            },
            payerPayee() {
                if(this.transactionValue === "0") return "Payer";
                return "Payee";
            }
        },
        methods: {
            submit() {
                var curr = this.editModel;
                if (curr.amount !== "" && curr.account !== "" && curr.transaction_date.toString().split("-").length === 3) {
                    var factor = 1;
                    if(this.transactionValue === "1") factor = -1;
                    var model = {
                        username: this.editModel.username,
                        transaction_date_before: this.transaction.transaction_date,
                        category_before: this.transaction.category,
                        payee_before: this.transaction.payee,
                        amount_before: this.transaction.amount,
                        memo_before: this.transaction.memo,
                        account_before: this.transaction.account,
                        transaction_date_after: curr.transaction_date,
                        category_after: curr.category,
                        payee_after: curr.payee,
                        amount_after: Number(Math.abs(curr.amount)) * factor,
                        memo_after: curr.memo,
                        account_after: curr.account,
                    };
                    console.log(model)
                    fetch("http://127.0.0.1:8080/transactions", httpPutOptions(model))
                        .then(res => res.json())
                        .then(response => {
                            this.$store.dispatch("editTransactionAction", model);
                            console.log('Success:', JSON.stringify(response))
                        })
                        .catch(error => console.error('Error:', error));
                    alert("Transaction updated!");
                    this.close();
                } else {
                    alert("Make sure you fill in amount, account, AND proper date!");
                }
            },
            close() {
                Dashboard.methods.closeEditEntry();
            }
        }
    };
</script>
<style scoped>
    #background {
        background-color: #f7f7fc;
    }
    .container {
        position: absolute;
        z-index: 200;
        width: 100%;
        background-color: rgba(0,0,0,.5);
        display: grid;
        margin-left: 9em;
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