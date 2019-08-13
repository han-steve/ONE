<template>
  <div class="background bubble">
    <v-chart
      :options="pie"
      ref="pie"
      :autoresize="true"
      @click="nextLevel"
      @legendselectchanged="legendSelected"
      class="pie-chart"
    />
    <div class="back-arrow" :class="{hidden: hidden}" @click="previousLevel">
      <i class="fas fa-arrow-left"></i>
    </div>
    <div class="label">
      <h3>Total:</h3>
      <span class="total" :style="{color: startColor}">${{total}}</span>
    </div>
  </div>
</template>

<script>
import ECharts from "vue-echarts";
import echarts from "echarts";
import dataUtil from "@/services/DataUtil.js";
import { interpolateColors } from "@/services/Colors.js";

var dataStack = [];
var colors = [];

export default {
  components: {
    "v-chart": ECharts
  },
  props: ["startColor", "endColor"],
  data() {
    return {
      hidden: true,
      pie: {
        tooltip: {
          trigger: "item",
          formatter: param => {
            return `${param.name}: $${param.value.toFixed(2)} (${
              param.percent
            }%)`;
          }
        },
        legend: {
          type: "scroll",
          orient: "vertical",
          left: "65%",
          top: "middle",
          itemWidth: 5,
          textStyle: {
            fontWeight: 700,
            fontSize: 15,
            fontFamily: "Segoe UI",
            color: "#4F4F4F"
          }
        },
        series: [
          {
            name: "Categories",
            type: "pie",
            center: ["33%", "50%"],
            radius: ["50%", "80%"],
            //basically hijacking the labels to display the total
            label: {
              show: false,
              position: "center",
              formatter: params => {
                //only display the label for the first element
                if (params.dataIndex == 0) {
                  var sum = 0;
                  this.pie.series[0].data.forEach(element => {
                    sum += element.value;
                  });
                  return `Total:\n${sum}`;
                } else {
                  return "";
                }
              },
              color: "#4F4F4F",
              fontSize: 20,
              fontFamily: "Segoe UI",
              fontWeight: 700
            },
            itemStyle: {
              color: param => {
                return colors[param.dataIndex];
              }
            },
            labelLine: {
              show: false
            },
            data: []
          }
        ]
      }
    };
  },
  methods: {
    // Kind of highjacks the legend tool and
    // use it instead for clicking into deeper categories.
    legendSelected(param) {
      //find the category that is unselected (clicked on)
      var category = Object.keys(param.selected).find(
        key => param.selected[key] === false
      );
      // select the category again to cancel the unselect action
      // (since we only want the click event not to actually change the state)
      this.$refs.pie.dispatchAction({
        type: "legendSelect",
        // legend name
        name: category
      });
      var currentData = this.pie.series[0].data;
      // Find the category's index to use as a parameter for nextLevel
      for (let i = 0; i < currentData.length; i++) {
        if (currentData[i].name === category) {
          this.nextLevel({
            dataIndex: i
          });
          return;
        }
      }
    },
    nextLevel(param) {
      this.$store.commit("SET_FILTER_CATEGORIES", [param.data.id]);
    },
    previousLevel() {
      if (dataStack.length > 1) {
        dataStack.pop();
        console.log(dataStack[dataStack.length - 1])
        // this.$store.commit("SET_FILTER_CATEGORIES", [
        //   dataStack[dataStack.length - 1].id
        // ]);
        this.updateData();
      }
    },
    updateData() {
      var data = dataStack[dataStack.length - 1].filter(element => {
        return element.value > 0;
      });
      if (data.length > 0) {
        this.pie.series[0].data = data;
      } else {
        //to prevent adding data of 0 length to the stack.
        //Honestly should put the filtering code above in nextLevel instead
        dataStack.pop();
      }
      if (dataStack.length === 1) {
        this.hidden = true;
      } else {
        this.hidden = false;
      }
      colors = interpolateColors(
        this.startColor,
        this.endColor,
        this.pie.series[0].data.length
      );
    }
  },
  created() {
    dataStack = [this.pieChartData];
    this.updateData();
  },
  computed: {
    total() {
      var sum = 0;
      this.pie.series[0].data.forEach(element => {
        sum += element.value || 0;
      });
      return sum.toFixed(2);
    },
    pieChartData() {
      return this.$store.getters.pieChartData;
    },
    categoryFilter() {
      return this.$store.state.filters.categories;
    }
  },
  watch: {
    pieChartData() {
      dataStack = [this.pieChartData];
      this.updateData();
    },
    categoryFilter() {
      var filteredCategory = dataUtil.findCategory(
        this.categoryFilter[0] + "",
        this.pieChartData
      );
      console.log(filteredCategory);
      var newData = filteredCategory.children;
      if (newData) {
        dataStack.push(newData);
      } else {
        dataStack.push([filteredCategory]);
      }
      this.updateData();
    }
  }
};
</script>

<style scoped>
.pie-chart {
  height: 300px;
  width: 420px;
}
.background {
  position: relative;
}
.back-arrow {
  position: absolute;
  top: 10px;
  left: 10px;
  box-sizing: border-box;
  border-radius: 20px;
  transition: 0.5s ease;
  cursor: pointer;
  padding: 2px 10px;
  background-color: #ebebeb;
  color: #909090;
}
.back-arrow:hover {
  color: #4f4f4f;
}
.hidden {
  opacity: 0;
  transform: translateX(-50px);
  cursor: default;
}
.label {
  position: absolute;
  left: 33%;
  top: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
}
h3 {
  color: #4f4f4f;
  font-size: 1.5em;
  margin: 0;
}
.total {
  font-size: 1.3em;
  font-weight: 700;
}
</style>