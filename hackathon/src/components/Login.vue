<template>
    <div>
        <main>
            <h2>Login</h2>
            <input type="text" placeholder="Username" v-model=LoginModel.username><br/>
            <input type="password" placeholder="Password" @keyup.enter="login()" v-model=LoginModel.password><br/>
            <button @click="login()">Submit</button><br/>
            <button @click="signup()">Sign Up</button>

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
                if (users.orderByChild("username").equalTo(this.LoginModel.username)
                    && users.orderByChild("password").equalTo(this.LoginModel.password)) {
                    Window.states.username = this.LoginModel.username;
                    this.$router.push({path: '/dashboard'});
                }
                else {
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
</style>