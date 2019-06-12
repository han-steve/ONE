<template>
    <div class="container-1">
        <main>
            <div id="container-2">
            <h2>Login</h2>
            <div id="grid">
                <i class="fa fa-user" aria-hidden="true"></i>
                <input type="text" class="form-control margin-bottom" placeholder="Username" v-model=LoginModel.username>
                <i class="fa fa-lock" aria-hidden="true"></i>
                <input type="password" class="form-control margin-bottom" placeholder="Password" @keyup.enter="login()" v-model=LoginModel.password>
            </div>
            <button id="loginButton" class="btn btn-md btn-success float-center" @click="login()">
                Submit
            </button>
            <button id="signupButton" class="btn btn-md btn-primary float-center" @click="signup()">
                SignUp
            </button>
            </div>
        </main>
    </div>
</template>

<script>
    import { httpPostOptions } from "../lib/http";
    import MD5 from "crypto-js/md5";


    export default {
        name: 'login',
        data() {
            return {
                LoginModel: {
                    username: '',
                    password: ''
                }
            }
        },
        mounted() {
            this.resetCurrentUser("")
        },
        computed: {
            getCurrentUser() {
                return this.$store.state.username;
            }
        },
        methods: {
            login() {
                var model = {
                    username: this.LoginModel.username,
                    email: "",
                    password: MD5(this.LoginModel.password).toString(),
                    phoneNumber: ""
                };
                fetch("http://127.0.0.1:8080/users", httpPostOptions(model))
                    .then(res => res.json())
                    .then(response => {
                        if(!response)
                            alert("Wrong username or password!");
                        else {
                            this.resetCurrentUser(this.LoginModel.username.trim());
                            this.$router.push({path: '/dashboard'})
                        }
                    })
                    .catch(error => console.error('Error:', error));
            },
            signup() {
                this.$router.push({path: '/signup'});
            },
            resetCurrentUser(username) {
                this.$store.dispatch('setCurrentUserAction', username);
                this.$store.dispatch('clearCurrentStoredTransactionsAction');
            }
        }
    };
</script>

<style scoped>
    main {
        background-color: #F7F7FC;
        display: grid;
        height: 100vh;
        align-content: center;
        align-items: center;
        padding: 0 15%;
    }
    h2 {
        font-size: 2em;
        margin-top: 0;
        margin-bottom: 1em;
    }
    #grid {
        display: grid;
        grid-template-columns: 3em auto;
        grid-template-rows: repeat(2, 3em);
        grid-column-gap: 1em;
    }
    i {
        font-size: 2em;
        text-align: center;
    }
    #signupButton {
        margin-left: 1%;
    }
    .container-1 {
        display: grid; 
        align-items: center;
        align-content: center;
    }
    #container-2 {
        background-color: white;
        padding: 4em;
        border-radius: 2em;
          box-shadow: -5px 29px 162px -54px grey;

    }
    @media only screen and (max-width: 1100px) {
  main{
    height: 85vh;
    border-radius: 1em;
    margin: 0 5vw;
    background-color: transparent;
  }
}
</style>