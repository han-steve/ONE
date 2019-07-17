<template>
  <div>
    <i class="fas fa-plus-circle plus-button" @click="open"></i>
    <transition name="modal-background" @after-enter="showModal=true">
      <div v-if="show" class="modal-bg" @click.self="showModal=false">
        <transition name="modal" @after-leave="close">
          <div v-if="showModal" class="modal">
            <div class="title-section">
              <h1>Add Transaction</h1>
              <div class="toggle-button">
                <toggle-button
                  v-model="toggle"
                  :value="false"
                  :labels="{checked: 'Earned', unchecked: 'Spent'}"
                  :color="{checked: 'rgba(62, 105, 221, 1)', unchecked: 'rgba(241, 42, 42, 1)'}"
                  :width="100"
                  :height="35"
                  :font-size="16"
                  :margin="5"
                />
              </div>
            </div>
            <div class="input-section">
              <div class="input-container">
                <label class="input-label">Date:</label>
                <treeselect class="input-field" :autoFocus="true"></treeselect>
              </div>
              <div class="input-container">
                <label class="input-label">Amount:</label>
                <treeselect class="input-field"></treeselect>
              </div>
              <div class="input-container">
                <label class="input-label">Date:</label>
                <treeselect class="input-field"></treeselect>
              </div>
              <div class="input-container">
                <label class="input-label">Amount:</label>
                <treeselect class="input-field"></treeselect>
              </div>
              <div class="input-container">
                <label class="input-label">Date:</label>
                <treeselect class="input-field"></treeselect>
              </div>
              <div class="input-container">
                <label class="input-label">Amount:</label>
                <treeselect class="input-field"></treeselect>
              </div>
            </div>
            <div class="submit-section">
              <button id="submit-button">Submit</button>
              <button id="cancel-button">Cancel</button>
            </div>
          </div>
        </transition>
      </div>
    </transition>
  </div>
</template>

<script>
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { ToggleButton } from "vue-js-toggle-button";

export default {
  components: {
    Treeselect,
    ToggleButton
  },
  data() {
    return {
      show: false,
      showModal: false,
      toggle: false
    };
  },
  methods: {
    open() {
      this.show = true;
    },
    close() {
      this.show = false;
    }
  }
};
</script>

<style scoped>
/* Transitions */
.modal-background-enter-active,
.modal-background-leave-active {
  transition: opacity 0.1s ease;
}
.modal-background-enter,
.modal-background-leave-to {
  opacity: 0;
}

.modal-enter-active,
.modal-leave-active {
  transition: all 0.2s ease-out;
}
.modal-enter,
.modal-leave-to {
  transform: translateY(500px);
  opacity: 0;
}

.plus-button {
  color: white;
  font-size: 50px;
  position: fixed;
  bottom: 100px;
  left: 5.9rem;
  transition: 0.2s ease-out;
  cursor: pointer;
  z-index: 900;
}
.plus-button:hover {
  transform: scale(1.1);
}
.modal-bg {
  width: 100vw;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 998;
  background-color: rgba(0, 0, 0, 0.3);
  display: grid;
  align-items: center;
  justify-items: center;
}
.modal {
  background-color: white;
  padding: 40px;
  padding-bottom: 30px;
  border-radius: 40px;
}
.title-section {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  padding-right: 15px;
}
h1 {
  font-size: 2em;
}
.toggle-button {
  height: 38px;
  align-self: flex-end;
}
.input-section {
  display: grid;
  grid-template-columns: auto auto;
  grid-column-gap: 30px;
  grid-row-gap: 15px;
}
label {
  font-size: 1.3em;
  font-weight: 700;
  margin-bottom: 10px;
  display: block;
}
.input-field {
  width: 200px;
}
.submit-section {
  margin-top: 30px;
  text-align: right;
}
.submit-section button {
  margin-left: 17px;
  color: white;
}
#submit-button {
  background-color: rgba(62, 105, 221, 1);
}
#cancel-button {
  background-color: rgba(241, 42, 42, 1);
}
</style>