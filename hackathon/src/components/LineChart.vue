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

    export default {
        name: "line",
        components: {
            LineChart
        },
        mounted() {
          if(this.$store.state.username === "")
            this.$router.push({ path: "/" });
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
                                format: "YYYY-MM-DD",
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
                var firstDay = new Date(date.getFullYear(), date.getMonth(), 1).getTime();
                return firstDay;
            },
            getLastDay() {
                var date = new Date();
                var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0).getTime();
                return lastDay;
            }
        },
        computed: {
          datacollection: function() {
            if (this.$store.username === "") {
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
                  },
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
            let earnings = [];
            let sorted = this.$store.state.transactions
            for (let i = 0; i < sorted.length; i++) {
              if (sorted[i].amount > 0) {
                earnings.push({
                  x: sorted[i].transaction_date,
                  y: sorted[i].amount
                });
              }
            }
            earnings.sort((a, b) => new Date(a.x).getTime() - new Date(b.x).getTime());
            return earnings;
          },
          spendings: function() {
            let spendings = [];
            let sorted = this.$store.state.transactions
            for (let i = 0; i < sorted.length; i++) {
              if (sorted[i].amount < 0) {
                spendings.push({
                  x: sorted[i].transaction_date,
                  y: Math.abs(sorted[i].amount)
                });
              }
            }
            spendings.sort((a, b) => new Date(a.x).getTime() - new Date(b.x).getTime());
            return spendings;
          },
          balance: function() {
            let balance = [];
            let sorted = this.$store.state.transactions
            let sum = 0;
            for (let i = 0; i < sorted.length; i++) {
              sum += parseFloat(sorted[i].amount);
              balance.push({
                x: sorted[i].transaction_date,
                y: sum
              });
            }
            balance.sort((a, b) => new Date(a.x).getTime() - new Date(b.x).getTime());
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