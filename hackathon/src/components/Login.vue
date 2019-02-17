<template>
    <div>
        <main>
            <h2>Login</h2>
            <input type="text" placeholder="Username" v-model=LoginModel.username>
            <button @click="login()">Submit</button>
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
                    username: ''
                }
            }
        },
        firebase: {
            names: users
        },
        methods: {
            login() {
                Window.states.username = this.LoginModel.username;
                this.$router.push({path: '/dashboard'});
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