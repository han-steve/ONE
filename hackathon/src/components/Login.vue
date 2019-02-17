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
                var found = false;
                console.log(this.LoginModel.username + ";" + this.LoginModel.password);
                for(var i = 0; i < this.names.length; i++) {
                    console.log(this.names[i].username + ":" + this.names[i].password);
                    if(this.names[i].username === this.LoginModel.username && this.names[i].password === this.LoginModel.password) {
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
</style>