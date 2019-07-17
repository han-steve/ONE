<template>
  <div class="background bubble">
    <v-chart class="line-chart" :options="line" :autoresize="true" />
  </div>
</template>

<script>
import ECharts from "vue-echarts";
import echarts from "echarts";

export default {
  components: {
    "v-chart": ECharts
  },
  props: ["startColor", "endColor"],
  data() {
    return {
      line: {
        tooltip: {
          trigger: "axis",
          formatter: params => {
            return `${params[0].name} ${
              params[0].seriesName
            }: $${params[0].data.value[1].toFixed(2)}`;
          }
        },
        xAxis: {
          type: "time",
          splitLine: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: "#909090",
              width: 3
            }
          },
          axisLabel: {
            color: "#4F4F4F",
            fontWeight: 600
          }
        },
        yAxis: {
          type: "value",
          axisLine: {
            lineStyle: {
              color: "#909090",
              width: 3
            }
          },
          axisLabel: {
            color: "#4F4F4F",
            fontWeight: 600
          }
        },
        series: [
          {
            name: "",
            type: "line",
            showSymbol: false,
            itemStyle: {
              color: this.startColor
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: this.endColor
                },
                {
                  offset: 1,
                  color: this.startColor
                }
              ])
            },
            data: []
          }
        ]
      }
    };
  },
  computed: {
    lineChartData() {
      return this.$store.getters.lineChartData;
    },
    lineChartName() {
      var type = this.$store.state.filters.type;
      if (type === "expense") return "Total Spending";
      if (type === "income") return "Total Income";
      if (type === "all") return "Balance";
    }
  },
  created() {
    this.line.series[0].data = this.lineChartData;
    this.line.series[0].name = this.lineChartName;
  },
  watch: {
    // because you can't use computed property in data.
    // tell me if you have a better way to do this lol
    // Also props in data don't seem to be reactive
    lineChartData() {
      this.line.series[0].data = this.lineChartData;
      this.line.series[0].itemStyle.color = this.startColor;
      this.line.series[0].areaStyle.color = new echarts.graphic.LinearGradient(
        0,
        0,
        0,
        1,
        [
          {
            offset: 0,
            color: this.endColor
          },
          {
            offset: 1,
            color: this.startColor
          }
        ]
      );
      this.line.series[0].name = this.lineChartName;
    }
  }
};
</script>

<style scoped>
.line-chart {
  height: 300px;
  width: 100%;
}
</style>