<template>
  <div>
    <transition name="modal-background" @after-enter="showModal=true">
      <div v-if="show" class="modal-bg" @click.self="showModal=false">
        <transition name="modal" @after-leave="close">
          <div v-if="showModal" class="modal">
            <div class="title-section">
              <h1><slot></slot>Transaction</h1>
              <div class="toggle-button">
                <toggle-button
                  v-model="isIncome"
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
                <!-- I really don't have other options than putting them all in style -->
                <v-date-picker
                  class="datepicker"
                  :input-props="{
                    style: 'font-weight: 700; font-size: 1em; font-family: Segoe UI; padding: 8.3px 20px; border: none; box-shadow: none; background-color: #ebebeb; border-radius: 40px; width: 200px; color: #4F4F4F',
                    autofocus: true
                  }"
                  :popover="{visibility: 'click'}"
                  v-model="transaction.date"
                ></v-date-picker>
              </div>
              <div class="input-container">
                <label class="input-label">Amount:</label>
                <input class="input-field" type="number" min="0" v-model="transaction.amount"></input>
              </div>
              <div class="input-container">
                <label class="input-label">Account:</label>
                <treeselect class="input-field" :options="accounts" v-model="transaction.account"></treeselect>
              </div>
              <div class="input-container">
                <label class="input-label">Category:</label>
                <treeselect class="input-field" :options="categories" v-model="transaction.category"></treeselect>
              </div>
              <div class="input-container">
                <label class="input-label">Payee:</label>
                <input class="input-field" type="text" v-model="transaction.payee"></input>
              </div>
              <div class="input-container">
                <label class="input-label">Memo:</label>
                <input class="input-field" type="text" v-model="transaction.memo"></input>
              </div>
            </div>
            <div class="submit-section">
              <button :disabled="!readyToSubmit" :class="{disabled: !readyToSubmit}" id="submit-button" @click="submit">Submit</button>
              <button id="cancel-button" @click="cancel">Cancel</button>
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
import dateUtil from "@/services/DateUtil.js";

export default {
  components: {
    Treeselect,
    ToggleButton
  },
  data() {
    return {
      categories: require("@/data/CategoryTree.json"),
      accounts: [],
      show: false,
      showModal: false,
      isIncome: false,
      transaction: {
        date: dateUtil.getToday(),
        amount: 0,
        account: null,
        category: null,
        payee: "",
        memo: ""
      }
    };
  },
  computed: {
    readyToSubmit() {
      return this.transaction.date && this.transaction.amount && 
      this.transaction.account && this.transaction.category
    }
  },
  methods: {
    setTransaction(id) {
      var transaction = this.$store.getters.getTransaction(id);
      this.transaction = transaction;
    },
    open() {
      this.show = true;
    },
    close() {
      this.show = false;
    },
    submit() {
      if (this.isIncome) {
        this.transaction.amount = -this.transaction.amount
      }
      console.log(this.transaction);
      this.clear();
      this.close();
    },
    cancel() {
      this.clear();
      this.close();
    },
    clear() {
      this.transaction = {
        date: dateUtil.getToday(),
        amount: 0,
        account: null,
        category: null,
        payee: "",
        memo: ""
      }
    }
  },
  created() {
    this.accounts = this.$store.getters.getAccounts
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