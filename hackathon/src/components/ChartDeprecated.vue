<template>
  <div class="container-1">
    <main>
      <div id="container-2">
        <h2>Chart</h2>
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
      user: ""
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
    }
  },
  firebase: {
    list: transactions
  },
  mounted() {
    console.log("I'm mounted!");
    this.user = this.$store.username;
    this.chartData.data.labels = this.filteredNames2;
    this.chartData.data.datasets[0].data = this.filteredAmount2;
    this.createChart("myChart", this.chartData);
  },
  computed: {
    filteredNames1: function() {
      var final = [];
      for (let i = 0; i < this.list.length; i++) {
        if (this.list[i].username === this.user && !final.includes(this.list[i].category)) {
          final.push(this.list[i].category);
        }
      }
      return final;
    },
    filteredAmount1: function() {
        console.log("filteredAmount1");
        var arr = this.filteredNames1;
      var final = [];
      for(let z = 0; z < arr.length; z ++) {
          final.push(0);
      }
      for (let j = 0; j < arr.length; j++) {
        for (let i = 0; i < this.list.length; i++) {
          if (this.list[i].category === arr[j] && this.list[i].username === this.user) {
            final[j] += parseFloat(this.list[i].amount);
          }
        }
      }
      return final;
    },
      filteredNames2: function() {
        console.log("filteredNames2");
          var cat = this.filteredNames1;
          console.log("cat: " + cat);
          var amounts = this.filteredAmount1;
          console.log("amounts: " + amounts.length);
          var final = [];
          for (let i = 0; i < amounts.length; i++) {
              if (amounts[i] < 0) {
                final.push(cat[i]);
              }
          }
          console.log("final cats: ", final);
          return final;
      },
      filteredAmount2: function() {
          console.log("filteredNames2");
          var arr = this.filteredNames2;
          var final = [];
          for(let z = 0; z < arr.length; z ++) {
              final.push(0);
          }
          for (let j = 0; j < arr.length; j++) {
              for (let i = 0; i < this.list.length; i++) {
                  if (this.list[i].category === arr[j] && this.list[i].username === this.user) {
                      final[j] += parseFloat(Math.abs(this.list[i].amount));
                  }
              }
          }
          console.log("filteredAmounts2: " + final);
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
}
</style>