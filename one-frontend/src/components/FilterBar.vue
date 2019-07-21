<template>
  <div class="filter-bar">
    <div class="filter-bar-container">
      <div class="left-filter-container">
        <treeselect
          class="left-filter-button"
          :value="selectedCategories"
          @input="selectCategories"
          :options="categories"
          :closeOnSelect="true"
          :multiple="true"
          placeholder="Filter by Category"
        ></treeselect>
        <treeselect
          class="left-filter-button"
          :value="selectedAccounts"
          @input="selectAccounts"
          :options="accounts"
          :closeOnSelect="true"
          :multiple="true"
          placeholder="Filter by Account"
        ></treeselect>
        <treeselect
          class="left-filter-button"
          :value="selectedAccounts"
          @input="selectAccounts"
          :options="accounts"
          :closeOnSelect="true"
          :multiple="true"
          placeholder="Filter by Payee"
        ></treeselect>
      </div>
      <div class="right-filter-container">
        <treeselect
          class="right-filter-button"
          :options="dateRangeOptions"
          :closeOnSelect="true"
          :multiple="false"
          :searchable="false"
          :clearable="false"
          :value="selectedRangeType"
          @input="selectRangeType"
        ></treeselect>
        <v-date-picker
          class="datepicker"
          mode="range"
          :color="color"
          :value="selectedRange"
          @input="selectRange"
          :max-date="new Date()"
          :input-props="{
            style: 'font-weight: 700; font-size: 1em; font-family: Segoe UI; padding: 8.3px 20px; border: none; box-shadow: none; background-color: #ebebeb; border-radius: 40px; color: #4F4F4F',
            readonly: true
          }"
          :popover="{visibility: 'click'}"
        />
      </div>
    </div>
    <hr />
  </div>
</template>

<script>
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import dateUtil from "@/services/DateUtil.js";

export default {
  components: {
    Treeselect
  },
  props: ["color"],
  data() {
    // end is always today
    var dateRangeTypes = [
      {
        id: 0,
        label: "This Month",
        start: dateUtil.getThisMonth()
      },
      {
        id: 1,
        label: "Past Month",
        start: dateUtil.getPastMonth()
      },
      {
        id: 2,
        label: "This Year",
        start: dateUtil.getThisYear()
      },
      {
        id: 3,
        label: "Past Year",
        start: dateUtil.getPastYear()
      },
      {
        id: 4,
        label: "All",
        start: dateUtil.getEarliestDate()
      },
      {
        id: 5,
        label: "Custom"
      }
    ];
    return {
      categories: require("@/data/CategoryTree.json"),
      accounts: [],
      dateRangeOptions: dateRangeTypes
    };
  },
  created() {
    // currently doesn't react to changes to accounts
    this.accounts = this.$store.getters.getAccounts;
  },
  computed: {
    selectedCategories() {
      return this.$store.state.filters.categories;
    },
    selectedAccounts() {
      return this.$store.state.filters.accounts;
    },
    selectedRange() {
      return this.$store.state.filters.date;
    },
    selectedRangeType() {
      var range = this.selectedRange;
      if (+range.end === +dateUtil.getToday()) {
        for (let i = 0; i < 5; i++) {
          if (+this.dateRangeOptions[i].start === +range.start) {
            return this.dateRangeOptions[i].id;
          }
        }
      }
      return 5;
    }
  },
  methods: {
    selectCategories(e) {
      this.$store.commit("SET_FILTER_CATEGORIES", e);
    },
    selectAccounts(e) {
      this.$store.commit("SET_FILTER_ACCOUNTS", e);
    },
    selectRange(e) {
      this.$store.commit("SET_FILTER_DATE_RANGE", e);
    },
    selectRangeType(e) {
      if (e !== 5) {
        var range = {
          start: this.dateRangeOptions[e].start,
          end: dateUtil.getToday()
        };
        this.$store.commit("SET_FILTER_DATE_RANGE", range);
      }
    }
  }
};
</script>

<style scoped>
.filter-bar-container {
  display: flex;
  flex-flow: row wrap;
  justify-content: space-between;
}
.left-filter-container {
  display: grid;
  grid-template-columns: auto auto auto;
  min-width: 600px;
  margin-bottom: 0.8em;
}
.left-filter-button {
  width: 200px;
  margin-right: 1em;
}
.right-filter-container {
  display: grid;
  grid-template-columns: auto auto;
  grid-column-gap: 1em;
  margin-bottom: 0.8em;
}
.right-filter-button {
  width: 140px;
}
.datepicker {
  width: 235px;
}
</style>

// [
//         {
//           id: "education",
//           label: "Education",
//           children: [
//             {
//               id: "books & supplies",
//               label: "Books & Supplies"
//             },
//             {
//               id: "tuition",
//               label: "Tuition"
//             },
//             {
//               id: "exams",
//               label: "Exams"
//             }
//           ]
//         },
//         {
//           id: "health & fitness",
//           label: "Health & Fitness",
//           children: [
//             {
//               id: "dentist",
//               label: "Dentist"
//             },
//             {
//               id: "doctor",
//               label: "Doctor"
//             },
//             {
//               id: "gym",
//               label: "Gym"
//             }
//           ]
//         },
//         {
//           id: "electronics",
//           label: "Electronics",
//           children: [
//             {
//               id: "computer",
//               label: "Computer"
//             },
//             {
//               id: "mobile",
//               label: "Mobile"
//             },
//             {
//               id: "accessory",
//               label: "Accessory"
//             }
//           ]
//         }
//       ]