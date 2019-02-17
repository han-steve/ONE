<template>
    <div class="container-1">
        <main>
            <div id="container-2">
            <h2>Chart</h2>
            <div class="chart">
                    <canvas id="myChart"></canvas>
                </div>
            </div>
        </main>
    </div>
</template>

<script src="../chart.js"></script>

<script>
    import {users} from '../firebase';
    export default {
        name: 'chart',
        data() {
            return {
                
            }
        },
        firebase: {
        },
        methods: {
            
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