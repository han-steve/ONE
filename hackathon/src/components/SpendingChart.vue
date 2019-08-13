<template>

    <div id="main">
      <div id="container-2">
        <h2>Spendings Chart</h2>
        <div class="chart">
          <pie-chart id="myChart" :chart-data="datacollection" :options="chartOptions" :width="220" :height="300"></pie-chart>
        </div>
      </div>
    </div>

</template>

<script>
import PieChart from "../PieChart.js";

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
          display: false,
          position: "bottom"
        },
        maintainAspectRatio:false
      }
    };
  },
  mounted() {
    this.user = this.$store.state.profile.username;
  },
  computed: {
    datacollection: function() {
      if (!this.user) {
        return null;
      } else {
        return {
          labels: this.filteredNames1,
          datasets: [
            {
              data: this.filteredAmount1,
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
        if (!final.includes(this.sorted[i].category)
        ) {
          final.push(this.sorted[i].category);
        }
      }
      return final;
    },
    filteredAmount1: function() {
      var arr = this.filteredNames1;
      var final = [];
      for (let z = 0; z < arr.length; z++) {
        final.push(0);
      }
      for (let j = 0; j < arr.length; j++) {
        for (let i = 0; i < this.sorted.length; i++) {
          if (this.sorted[i].category === arr[j]) {
            final[j] += parseFloat(this.sorted[i].amount);
          }
        }
      }
      return final;
    },
    filteredNames2: function() {
      var cat = this.filteredNames1;
      var amounts = this.filteredAmount1;
      var final = [];
      for (let i = 0; i < amounts.length; i++) {
        if (amounts[i] < 0) {
          final.push(cat[i]);
        }
      }
        return final;
    },
    filteredAmount2: function() {
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
        return final;
    }
  }
};
</script>

<style scoped>
#main {
  /*background-color: #f7f7fc;*/
  /*display: grid;*/
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
