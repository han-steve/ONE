<template>
    <div class="container">
        <main>
            <h2>Login</h2>
            <input type="text" class="form-control margin-bottom" placeholder="Username" v-model=LoginModel.username><br/>
            <input type="password" class="form-control margin-bottom" placeholder="Password" @keyup.enter="login()" v-model=LoginModel.password><br/>
            <button id="loginButton" class="btn btn-md btn-success float-center" @click="login()">
                Submit
            </button>
            <button id="signupButton" class="btn btn-md btn-primary float-center" @click="signup()">
                SignUp
            </button>

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
    #signupButton {
        margin-left: 1%;
    }
</style>