<template>
  <div class="container-1">
    <main>
      <div id="container-2">
        <h2>This Month</h2>
        <div class="chart">
          <line-chart :width="400" :height="350" id="myChart" :chart-data="datacollection" :options="chartOptions"></line-chart>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
    import LineChart from "../LineChart.js";
    import { transactions } from "../firebase";

    export default {
        name: "line",
        components: {
            LineChart
        },
        data() {
            return {
                user: null,
                chartOptions: {
                    scales: {
                        xAxes: [{
                            type: 'time',
                            distribution: 'linear',
                            time: {
                                unit: 'day',
                                min: this.getFirstDay(),
                                max: this.getLastDay()
                            }
                        }]
                    },
                    maintainAspectRatio:false
                }
            };
        },
        methods: {
            getFirstDay() {
                var date = new Date();
                var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
                return firstDay;
            },
            getLastDay() {
                var date = new Date();
                var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
                return lastDay;
            },
            reverseOrder(l) {
                var reversed = [];
                for(var i = l.length - 1; i >= 0; i--) {
                    reversed.push(l[i]);
                }
                return reversed;
            }
        },
        firebase: {
            list: transactions.orderByChild("date")
        },
        mounted() {
            console.log("I'm mounted!");
            this.user = Window.states.username;
        },
        computed: {
            datacollection: function() {
                if (!this.user) {
                    return null;
                } else {
                    return {
                        // labels: this.getDaysOfMonth,
                        datasets: [
                            {
                                label: "Earnings",
                                data: this.earnings,
                                fill: false,
                                borderColor: "#53f442"
                                // backgroundColor: "#53f442"
                            },
                            {
                                label: "Spendings",
                                data: this.spendings,
                                fill: false,
                                borderColor: "#e82929"
                                // backgroundColor: "#e82929"
                            }
                            ,
                            {
                                label: "Balance",
                                data: this.balance,
                                fill: false,
                                borderColor: "#000000"
                                // backgroundColor: "#e82929"
                            }
                        ]
                    };
                }
            },
            earnings: function() {
                var firstDay = this.getFirstDay();
                var lastDay = this.getLastDay();
                var sorted = this.list;
                var earnings = [];
                for(var i = 0; i < sorted.length; i++) {
                    console.log(sorted[i].date);
                    if(sorted[i].username === this.user
                        && new Date(sorted[i].date.toString()) >= firstDay && new Date(sorted[i].date.toString()) <= lastDay
                        && sorted[i].amount > 0) {
                        earnings.push({x: new Date(sorted[i].date), y: sorted[i].amount});
                    }
                }
                return earnings;
            },
            spendings: function() {
                var firstDay = this.getFirstDay();
                var lastDay = this.getLastDay();
                var sorted = this.list;
                var spendings = [];
                for(var i = 0; i < sorted.length; i++) {
                    if(sorted[i].username === this.user
                        && new Date(sorted[i].date) >= firstDay && new Date(sorted[i].date) <= lastDay
                        && sorted[i].amount < 0) {
                        spendings.push({x: new Date(sorted[i].date), y: Math.abs(sorted[i].amount)});
                    }
                }
                return spendings;
            },
            balance: function() {
                var firstDay = this.getFirstDay();
                var lastDay = this.getLastDay();
                var sorted = this.list;
                var balance = [];
                var sum = 0;
                for(var i = 0; i < sorted.length; i++) {
                    if(sorted[i].username === this.user
                        && new Date(sorted[i].date) >= firstDay && new Date(sorted[i].date) <= lastDay) {
                        sum += parseFloat(sorted[i].amount);
                        balance.push({x: new Date(sorted[i].date), y: sum});
                    }
                }
                console.log(balance);
                return balance;
            }
        }
    };
</script>

<style scoped>
  main {
    background-color: #f7f7fc;
    display: grid;
    height: 100vh;
    align-content: center;
    align-items: center;
    padding: 0 15%;
  }
  h2 {
    font-size: 2em;
    margin-top: 0;
    margin-bottom: 1em;
  }
  #signupButton {
    margin-left: 1%;
  }
  .container-1 {
    display: grid;
    align-items: center;
    align-content: center;
  }
  #container-2 {
    background-color: white;
    height: 85vh;
    padding: 4em;
    border-radius: 2em;
    box-shadow: -5px 29px 162px -54px grey;
  }
  @media only screen and (max-width: 1100px) {
    main {
      height: 85vh;
      border-radius: 1em;
      padding: 0 5%;
      margin: 0 5vw;
      background-color: transparent;
    }
  }
</style>