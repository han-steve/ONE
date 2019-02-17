<template>
    <div class="container-1">
        <main>
            <div id="container-2">
        <h2>Select an account below: </h2>
        <select id="selection" @change="chosen()">
            <option>Select an account</option>
            <option v-for="item of new Set(account)">
                {{ item }}
            </option>
            <option>Total</option>
        </select>
        <div id="display" style="visibility: hidden">
        <h3 id="title"></h3>
        <h4 id="balance"></h4>
        </div>
    </div>
        </main>
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

<style scoped>
    main {
        background-color: #F7F7FC;
        display: grid;
        height: 100vh;
        align-content: center;
        align-items: center;
        padding: 0 15%;
    }
    #display {
        display: grid;
        grid-template-columns: auto 7em;
        align-content: center;
        align-items: center;
        margin-top: 2em;
    }
    h3 {
        font-weight:400;
    }
    #balance {
        color: rgba(62,105,221,1);
        font-size: 2em;
        font-weight: 800
    }
    h2 {
        font-size: 2em;
        margin-top: 0;
        margin-bottom: 1em;
    }
    .container-1 {
        display: grid; 
        align-items: center;
        align-content: center;
    }
    #container-2 {
        background-color: white;
        padding: 4em;
        border-radius: 2em;
          box-shadow: -5px 29px 162px -54px grey;

    }
    @media only screen and (max-width: 1100px) {
  main{
    height: 85vh;
    border-radius: 1em;
    margin: 0 5vw;
    background-color: transparent;
  }
}
</style>
