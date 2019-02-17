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
    import {users} from '../firebase';
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
        firebase: {
            names: users
        },
        methods: {
            login() {
                var found = false;
                for(var i = 0; i < this.names.length; i++) {
                    if(this.names[i].username.trim() === this.LoginModel.username.trim() && this.names[i].password.trim() === this.LoginModel.password.trim()) {
                        found = true;
                        Window.states.username = this.LoginModel.username;
                        this.$router.push({path: '/dashboard'});
                    }
                }
                if(!found) {
                    alert("Wrong username or password!");
                }
            },
            signup() {
                this.$router.push({path: '/signup'});
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
</style>