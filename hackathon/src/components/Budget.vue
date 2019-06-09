<template>
    <div class="container-1">
        <main>
            <div id="container-2">
                <h2>Budget</h2>
                <div class="chart">
                    <horz-bar-chart :width="380" :height="280" id="myChart" :chart-data="datacollection" :options="chartOptions"></horz-bar-chart>
                </div>
            </div>
        </main>
    </div>
</template>

<script>
    import HorzBarChart from "../HorzBarChart.js";
    import { transactions } from "../firebase";

    export default {
        name: "budget",
        components: {
            HorzBarChart
        },
        data() {
            return {
                user: null,
                chartOptions: {
                    scales: {
                        xAxes: [{
                            ticks: {
                                beginAtZero:true,
                                fontFamily: "'Open Sans Bold', sans-serif",
                                fontSize:11,
                                suggestedMin: 0,
                                suggestedMax: 800
                            },
                            scaleLabel:{
                                display:false
                            },
                            gridLines: {
                            },
                            stacked: true
                        }],
                        yAxes: [{
                            gridLines: {
                                display:false,
                                color: "#fff",
                                zeroLineColor: "#fff",
                                zeroLineWidth: 0
                            },
                            ticks: {
                                fontFamily: "'Open Sans Bold', sans-serif",
                                fontSize:11
                            },
                            stacked: true
                        }]
                    },
                    legend:{
                        display:true
                    }
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
            },
            randoColor() {
                return "rgba( " + Math.floor(Math.random()*256) + "," + Math.floor(Math.random()*256) + "," + Math.floor(Math.random()*256) + ",1)";
            }
        },
        firebase: {
            list: transactions.orderByChild("date")
        },
        mounted() {
            this.user = this.$store.state.username;
        },
        computed: {
            datacollection: function() {
                if (!this.user) {
                    return null;
                } else {
                    return {
                        type: "horizontalBar",
                        labels: ["Spendings"],
                        datasets: this.spendings
                    };
                }
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
                        spendings.push({label: sorted[i].memo, data: [Math.abs(sorted[i].amount)], backgroundColor: this.randoColor()});
                    }
                }
                return spendings;
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