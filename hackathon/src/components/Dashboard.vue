<template>
  <div>
    <main>
      <h2>Dashboard</h2>
      <p class="subtitle">overview of your financial life</p>
      <label>Name:</label>
      <input type="text" v-model="name">
      <button @click="submitName()">Submit</button>
      <div>
        <ul>
          <li v-for="name of names" v-bind:key="name['.key']">
            <p>{{name.name}}</p>
            <button @click="remove(name['.key'])">Remove</button>
          </li>
        </ul>
      </div>
    </main>
  </div>
</template>

<script>
  import {namesRef} from '../firebase';
  export default {
    name: 'dashboard',
    data(){
      return{
        name: 'Paul'
      }
    }, 
    firebase: {
      names: namesRef
    },
    methods: {
      submitName() {
        namesRef.push({name: this.name, edit: false})
      },
      remove(key) {
        namesRef.child(key).remove(); 
      }
    }
  };
</script>

<style>
  main {
    background-color: #F7F7FC;
    padding: 3em;
    padding-left: 4em;
  }
  h2 {
    font-size: 2em;
    margin-top: 0;
    margin-bottom: 0;
  }
  .subtitle {
    margin: 0;
    font-weight: 500;
    font-size: 1.2em;
    color: #878B9D;
  }
</style>
