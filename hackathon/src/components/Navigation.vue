<template>
    <div id="nav-bar">
        <div id="logo">
            <h1>ONE</h1>
            <div id="rectangle"></div>
        </div>
        <p id="subtitle">your personal financial manager</p>
        <div id="nav">
            <div>Dashboard</div>
            <div>Transactions</div>
            <div>Goals</div>
            <div>Accounts</div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'navigation',
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
        padding-left: 5.3em;
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

</style>
