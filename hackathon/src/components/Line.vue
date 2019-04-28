<template>
  <div class="container-1">
    <main>
      <div id="container-2">
        <h2>Line Graph</h2>
        <div class="chart">
          <canvas id="myChart"></canvas>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { transactions } from "../firebase";
import chart from "chart.js";

const chartData = {
  type: "pie",
  data: {
    labels: [],
    datasets: [
      {
        // one line graph
        label: "Number of Moons",
        data: [],
        backgroundColor: [
          "#CD5C5C",
          "#F08080",
          "#FA8072",
          "#E9967A",
          "#FFA07A",
          "#338EFF",
          "#0F55AD",
          "#083F83", 
          "#072A55",
          "#3B1A64",
          "#7536C3",
          "#EE51AB"
        ],
        borderColor: [
          
        ],
        borderWidth: 3
      }
    ]
  },
   options: {
         legend: {
            display: true,
            position: 'right'
         }
}
}

export default {
  name: "chart",
  data() {
    return {
      chartData: chartData,
      user: "",
      begin: new Date(0),
      end: new Date(Date.now())
    };
  },
  methods: {
    createChart(chartId, chartData) {
      const ctx = document.getElementById(chartId);
      const myChart = new Chart(ctx, {
        type: chartData.type,
        data: chartData.data,
        options: chartData.options
      });
    },
      updateChart() {
          this.chartData.data.labels = this.filteredNames;
          this.chartData.data.datasets[0].data = this.filteredAmount;
          console.log("updated data: " + this.chartData.data.datasets[0].data)
          this.createChart("myChart", this.chartData);
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
    setInterval(this.updateChart(), 200);
  },
  computed: {
      filteredNames: function() {
        var final = [];
        var sorted = this.reverseOrder(this.list);
          for (let i = 0; i < sorted.length; i++) {
            if (sorted[i].username === this.user && !final.includes(sorted[i].category)) {
                final.push(sorted[i].category);
            }
        }
        console.log("final: ", final)
        return final;
    },
    filteredAmount: function() {
      var arr = this.filteredNames;
        var sorted = this.reverseOrder(this.list);
        var final = [];
      for(let z = 0; z < arr.length; z ++) {
          final.push(0);
      }
      // console.log(final);
      for (let j = 0; j < arr.length; j++) {
        for (let i = 0; i < sorted.length; i++) {
          if (sorted[i].category === arr[j] && sorted[i].username === this.user
              && new Date(sorted[i].date.toString()) >= new Date(this.begin.toString())
              && new Date(sorted[i].date.toString()) <= new Date(this.end.toString())) {
              console.log(parseFloat(sorted[i].amount) + sorted[i].category);
            final[j] += parseFloat(sorted[i].amount);
          }
        }
      }
      return final;
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
  padding: 4em;
  border-radius: 2em;
  box-shadow: -5px 29px 162px -54px grey;
}
@media only screen and (max-width: 1100px) {
  main{
    height: 85vh;
    border-radius: 1em;
    padding: 0 5%;
    margin: 0 5vw;
    background-color: transparent;
  }
  #myChart{
  }
    .datepicker {
        width: 35%;
        display: inline-block;
        padding: 1%;
        margin: .5em;
    }
}
label {
    font-weight: 500;
    font-size: 1.1em;
}
</style>