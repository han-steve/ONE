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
      </div>
      <div class="right-filter-container"></div>
    </div>
    <hr />
  </div>
</template>

<script>
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  components: {
    Treeselect
  },
  data() {
    return {
      categories: require("@/data/CategoryTree.json")
    };
  },
  computed: {
    selectedCategories() {
      return this.$store.state.filters.categories;
    }
  },
  methods: {
    selectCategories(e) {
      this.$store.commit("SET_FILTER_CATEGORIES", e);
    }
  }
};
</script>

<style scoped>
.filter-bar-container {
  display: flex;
  flex-flow: row wrap;
  justify-content: space-between;
  margin-bottom: 1em;
}
.left-filter-container {
  display: grid;
  grid-template-columns: auto auto auto;
  grid-column-gap: 1em;
  min-width: 600px;
}
.left-filter-button {
  width: 300px;
}
.right-filter-container {
  display: flex;
  flex-direction: row;
  justify-content: end;
}
.right-filter-button {
  width: 100px;
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