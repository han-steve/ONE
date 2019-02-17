
<template>
  <div id="nav-bar">
    <div id="logo">
      <h1>ONE</h1>
      <div id="rectangle"></div>
    </div>
    <p id="subtitle">your personal financial manager</p>
    <div id="nav">
      <div>
        <a class="navbar-brand" @click="dashboard()"><i class="fa fa-home"></i> Dashboard </a>
      </div>
      <!--<div>-->
        <!--<a class="navbar-brand" :href="`#/`"><i class="fa fa-shopping-cart" aria-hidden="true"></i> Transactions </a>-->
      <!--</div>-->
      <!--<div>-->
        <!--<a class="navbar-brand" :href="`#/`"><i class="fa fa-line-chart" aria-hidden="true"></i> Goals </a>-->
      <!--</div>-->
      <div>
        <a class="navbar-brand" @click="account()"><i class="fa fa-users" aria-hidden="true"></i> Accounts </a>
      </div>
      <div>
        <a class="navbar-brand" @click="addEntry()"><i class="fa fa-plus-circle" aria-hidden="true"></i> Add Entry</a>
      </div>
      <div>
        <a class="navbar-brand" @click="signinout()"><i class="fa fa-sign-out" aria-hidden="true"></i> <span id="toggle">Sign In</span></a>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'navTpl',
    data() {
        return {
            out: false
        }
    },
    computed() {
        if(Window.states.username === '')
          document.getElementById("toggle").innerText = "Sign In";
        else
          document.getElementById("toggle").innerText = "Sign Out";
    },
    methods: {
        dashboard() {
            if(Window.states.username === '')
                this.$router.push({path: '/'});
            else
                this.$router.push({path: '/dashboard'});
        },
        account() {
            if(Window.states.username === '')
                this.$router.push({path: '/'});
            else
                this.$router.push({path: '/accounts'});
        },
        addEntry() {
            if(Window.states.username === '')
                this.$router.push({path: '/'});
            else
                this.$router.push({path: '/add'});
        },
        signinout() {
            if(this.out)
              Window.states.username = '';
            this.out = !this.out;
            this.$router.push({path: '/'});
        }
    }
  };
</script>

<style scoped>
  #nav-bar {
    background: linear-gradient(45deg, rgba(62,105,221,1) 0%, rgba(241,42,42,1) 100%);
    height: 100vh;
    width: 100%;
  }
  #logo {
    padding-top: 5em;
  }
  #logo h1 {
    text-align: center;
    font-weight: 900;
    font-style: italic;
    font-size: 5em;
    margin: 0;
    color: white;
  }
  #rectangle {
    width: 10em;
    height: 1em;
    background-color: white;
    margin: 0 auto;
  }
  #subtitle {
    text-align: center;
    font-size: 1.3em;
    font-weight: 600;
    color: #ccc;
  }
  #nav{
    margin-top: 3em;
    display: grid;
    grid-template-rows: repeat(auto);
    height: 20em;
  }
  #nav div{
    display: grid;
    align-content: center;
    padding: auto;
    padding-left: 4em;
    font-size: 1.7em;
    text-align: left;
    font-weight: 600;
    color: white;
  }
  #nav div:hover {
    background-color: #F7F7FC;
    color: black;
    font-weight: 700;
  }
  .navbar-header {
  width: 80%;
  margin: 0 auto;
  display: inline-block;
  vertical-align: middle;
}
.navbar-brand {
  color: #ffffff;
  font-size: 1em;
  /*margin-top: 0.5em;*/
  margin-left: 1em;
}
.navigation {
  background-color: #5cb85c;
  border-color: #4cae4c;
  margin-bottom: 1em;
  min-height: 50px;
  text-align: justify;
  cursor: pointer;
  border-radius: 0.5em 0.5em 0 0;
  width: 80%;
  position: relative;
}
</style>
