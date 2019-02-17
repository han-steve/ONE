<template>
    <div>
        <h2>Select an account below: </h2>
        <select id="selection" @change="chosen()">
            <option>Select an account</option>
            <option v-for="item of new Set(account)">
                {{ item }}
            </option>
            <option>Total</option>
        </select>
        <div id="display" style="visibility: hidden">
        <h2 id="title"></h2>
        <h4 id="balance"></h4>
        </div>
    </div>
</template>

<script>
    var list = [];
    import navTpl from "@/components/NavTpl"
    import {transactions} from '../firebase';
    export default {
        name: 'accounts',
        components: {navTpl},
        data() {
            return {
                account: [],
                chosenAccount: ''
            }
        },
        firebase: {
            transactionList: transactions
        },
        mounted() {
            list = this.transactionList;
            for(var i = 0; i < list.length; i++) {
                if(list[i].username === Window.states.username) {
                    this.account.push(list[i].account);
                }
            }
        },
        methods: {
            chosen() {
                var sum = 0;
                this.chosenAccount = document.getElementById("selection").value;
                for(var i = 0; i < list.length; i++) {
                    if(list[i].username === Window.states.username) {
                        console.log()
                        if(this.chosenAccount === "Total") {
                            sum += new Number(list[i].amount);
                        }
                        else if(list[i].account === this.chosenAccount) {
                            sum += new Number(list[i].amount);
                        }
                    }
                }
                document.getElementById("title").innerText = "Your total balance spent on " + this.chosenAccount + " is: ";
                document.getElementById("balance").innerText = "\t$" + sum.toFixed(2);
                document.getElementById("display").style.visibility = "visible";
            }
        }
    }
</script>
