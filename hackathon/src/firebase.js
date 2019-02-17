import {initializeApp} from 'firebase';
const app = initializeApp({
    apiKey: "AIzaSyAeDwHG7zvtEUiXmrjGzJi13rGWno8wEcQ",
    authDomain: "hackdfw2019-a4545.firebaseapp.com",
    databaseURL: "https://hackdfw2019-a4545.firebaseio.com",
    projectId: "hackdfw2019-a4545",
    storageBucket: "hackdfw2019-a4545.appspot.com",
    messagingSenderId: "675704425230"
});

export const db = app.database();
export const users = db.ref("users");
export const transactions = db.ref("transactions");