<template>
    <div>
        <main>
            <h2>SignUp</h2>
            <input type="text" placeholder="Username" v-model=SignUpModel.username>
            <input type="password" placeholder="Password" @keyup.enter="signup()" v-model=SignUpModel.password>
            <button @click="signup()">Submit</button>
        </main>
    </div>
</template>

<script>
    import {users} from '../firebase';
    export default {
        name: 'login',
        data() {
            return {
                SignUpModel: {
                    username: '',
                    password: '',
                    transactions: []
                }
            }
        },
        firebase: {
            names: users
        },
        methods: {
            signup() {
                Window.states.username = this.SignUpModel.username;
                users.push(this.SignUpModel);
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