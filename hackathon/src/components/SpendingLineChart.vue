<template>
  <div class="container-1">
    <div id="main">
      <div id="container-2">
        <h2>This Month</h2>
        <div class="chart">
          <line-chart
            :width="400"
            :height="300"
            id="myChart"
            :chart-data="datacollection"
            :options="chartOptions"
          ></line-chart>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LineChart from "../LineChart.js";
import { transactions } from "../firebase";

export default {
  name: "line",
  props: ["sorted"],
  components: {
    LineChart
  },
  data() {
    return {
      user: null,
      chartOptions: {
        scales: {
          xAxes: [
            {
              type: "time",
              distribution: "linear",
            }
          ]
        },
        maintainAspectRatio: false
      }
    };
  },
  firebase: {
    list: transactions
  },
  mounted() {
    console.log("LineChart is Mounted!");
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
      var earnings = [];
      for (var i = 0; i < this.sorted.length; i++) {
        console.log(this.sorted[i].date);
        if (this.sorted[i].amount > 0) {
          earnings.push({
            x: new Date(this.sorted[i].date),
            y: this.sorted[i].amount
          });
        }
      }
      return earnings;
    },
    spendings: function() {
      var spendings = [];
      for (var i = 0; i < this.sorted.length; i++) {
        if (this.sorted[i].amount < 0) {
          spendings.push({
            x: new Date(this.sorted[i].date),
            y: Math.abs(this.sorted[i].amount)
          });
        }
      }
      return spendings;
    },
    balance: function() {
      var balance = [];
      var sum = 0;
      for (var i = 0; i < this.sorted.length; i++) {
        sum += parseFloat(this.sorted[i].amount);
        balance.push({ x: new Date(this.sorted[i].date), y: sum });
      }
      console.log(balance);
      return balance;
    }
  }
};
</script>

<style scoped>
#main {
  align-content: center;
  align-items: center;
  padding: 0;
  background-color: none;
}
h2 {
  font-size: 1.4em;
  margin-top: 0;
  margin-bottom: .5em;
}
#signupButton {
  margin-left: 1%;
}
.container-1 {
  margin-left: 1%;
}
#container-2 {
  background-color: white;
  padding: 2em;
  border-radius: 2em;
  padding-top: 1.3em;
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