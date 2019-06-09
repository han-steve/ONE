<template>
    <div class="container-1">
        <main>
            <div id="container-2">
            <h2>Login</h2>
            <input type="text" class="form-control margin-bottom" placeholder="Username" v-model=LoginModel.username><br/>
            <input type="password" class="form-control margin-bottom" placeholder="Password" @keyup.enter="login()" v-model=LoginModel.password><br/>
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
    import { httpGetOptions } from "../lib/http";

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
        computed: {
            getCurrentUser() {
                return this.$store.state.username;
            }
        },
        methods: {
            login() {
                fetch("http://127.0.0.1:8080/users", httpGetOptions())
                    .then(res => res.json())
                    .then(response => {
                        var users = response.users;
                        var found = false;
                        for(var i = 0; i < users.length; i++) {
                            if(users[i].username.trim() === this.LoginModel.username.trim() && users[i].password.trim() === this.LoginModel.password.trim()) {
                                found = true;
                                this.setCurrentUser(this.LoginModel.username.trim());
                                this.$store.dispatch("clearCurrentStoredTransactionsAction");
                                console.log("Set username in store: " + this.getCurrentUser);
                                this.getData();
                            }
                        }
                        if(!found) {
                            alert("Wrong username or password!");
                        }
                    })
                    .catch(error => console.error('Error:', error));
            },
            getData() {
                fetch("http://127.0.0.1:8080/transactions/" + this.$store.state.username, httpGetOptions())
                    .then(res => res.json())
                    .then(response => {
                        var transactions = response.transactions;
                        for(var i = 0; i < transactions.length; i++) {
                            this.$store.dispatch("addTransactionAction", transactions[i]);
                        }
                        this.$router.push({path: '/dashboard'})
                    })
                    .catch(error => console.error('Error:', error));
            },
            signup() {
                this.$router.push({path: '/signup'});
            },
            setCurrentUser(username) {
                this.$store.dispatch('setCurrentUserAction', username);
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