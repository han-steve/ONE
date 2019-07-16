<template>
  <div class="background bubble">
    <v-chart class="line-chart" :options="line" :autoresize="true" />
  </div>
</template>

<script>
import ECharts from "vue-echarts";
import echarts from "echarts";
import { mapGetters } from "vuex";

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
            name: "Total Spending",
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
            data: this.lineChartData
          }
        ]
      }
    };
  },
  computed: mapGetters(["lineChartData"]),
  created() {
    this.line.series[0].data = this.lineChartData;
  }
};
</script>

<style scoped>
.line-chart {
  height: 300px;
  width: 100%;
}
</style>