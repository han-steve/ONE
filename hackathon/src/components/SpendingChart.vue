<template>
  <div class="container-1">
    <main>
      <div id="container-2">
        <h2>Spendings</h2>
        <div class="chart">
          <pie-chart id="myChart" :chart-data="datacollection" :options="chartOptions"></pie-chart>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import PieChart from "../PieChart.js";
import { transactions } from "../firebase";

export default {
  name: "chart",
  components: {
      PieChart
  },
  props: ['sorted'],
  data() {
    return {
      user: null,
      chartOptions: {
        legend: {
          display: true,
          position: "right"
        }
      }
    };
  },
  methods: {},
  firebase: {
    list: transactions
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
          labels: this.filteredNames2,
          datasets: [
            {
              data: this.filteredAmount2,
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
              borderColor: [],
              borderWidth: 3
            }
          ]
        };
      }
    },
    filteredNames1: function() {
      var final = [];
      for (let i = 0; i < this.sorted.length; i++) {
        if (
            this.sorted[i].username === this.user &&
          !final.includes(this.sorted[i].category)
        ) {
          final.push(this.sorted[i].category);
        }
      }
      return final;
    },
    filteredAmount1: function() {
      console.log("filteredAmount1");
      var arr = this.filteredNames1;
      var final = [];
      for (let z = 0; z < arr.length; z++) {
        final.push(0);
      }
      for (let j = 0; j < arr.length; j++) {
        for (let i = 0; i < this.sorted.length; i++) {
          if (
            this.sorted[i].category === arr[j] &&
            this.sorted[i].username === this.user
          ) {
            final[j] += parseFloat(this.sorted[i].amount);
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
      for (let z = 0; z < arr.length; z++) {
        final.push(0);
      }
      for (let j = 0; j < arr.length; j++) {
        for (let i = 0; i < this.sorted.length; i++) {
          if (
            this.sorted[i].category === arr[j] &&
            this.sorted[i].username === this.user
          ) {
            final[j] += parseFloat(Math.abs(this.sorted[i].amount));
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
  /*background-color: #f7f7fc;*/
  /*display: grid;*/
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
  /*display: grid;*/
  align-items: center;
  align-content: center;
  padding: 2%;
}
#container-2 {
  background-color: #f7f7fc;
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