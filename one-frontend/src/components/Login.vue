<template>
  <div class="background">
    <div class="login">
      <form>
        <transition name="title" mode="out-in">
          <!-- adding a key makes the transition work -->
          <h1 :key="text">{{ text }}</h1>
        </transition>
        <div class="input-fields">
          <div class="input-field">
            <input type="text" placeholder="username" autofocus v-model="username"/>
          </div>
          <div class="input-field">
            <input type="text" placeholder="password" autofocus v-model="password"/>
          </div>
          <div class="signup" v-if="!login">
            <div class="input-field">
              <input type="text" placeholder="email" autofocus v-model="email"/>
            </div>
            <div class="input-field">
              <input type="text" placeholder="phone number" autofocus v-model="phoneNumber"/>
            </div>
          </div>
        </div>
        <button type="submit" @click.prevent="submit">Submit</button>
      </form>
      <div class="social-media">
        <div id="or">or</div>
        <img id="facebook" class="button" src="../assets/facebook.svg" />
      </div>
      <div class="alternate-action">
        <p>{{firstWord}} have a account?</p>
        <img
          id="signup-button"
          v-if="login"
          class="button"
          src="../assets/signup.svg"
          @click="login = false"
        />
        <img
          id="signup-button"
          v-else
          class="button"
          src="../assets/login.svg"
          @click="login = true"
        />
      </div>
    </div>
  </div>
</template>

<script>
import {httpGetOptions, httpPostOptions} from "../http";
import MD5 from "crypto-js/md5";

export default {
  data() {
    return {
      login: true,
      username: "",
      password: "",
      email: "",
      phoneNumber: ""
    };
  },
  methods: {
    signin() {
        var username = this.username;
        var password = MD5(this.password).toString();
        fetch("http://127.0.0.1:8080/users/login/" + username + "/" + password, httpGetOptions())
            .then(res => res.json())
            .then(response => {
                if(!response)
                    alert("Wrong username or password!");
                else {
                    this.resetCurrentUser();
                    fetch("http://127.0.0.1:8080/users/" + username, httpGetOptions())
                        .then(res => res.json())
                        .then(response => {
                            let user = response;
                            this.$store.dispatch("updateProfileAction", user);

                        })
                        .then(() =>  {
                          this.getDBTransactions();
                          this.getBankConnections();
                        })
                        .then(() => this.$router.push("dashboard"))
                        .catch(error => console.error("Error:", error));
                }
            })
            .catch(error => console.error('Error:', error));
    },
    signup() {
      if(this.username !== "" && this.email.match("@") && this.password.trim().length >= 6) {
        if(this.phoneNumber !== "" && this.phoneNumber.trim().length !== 10) {
          this.phoneNumber = "";
        }
        const model = {
          user_id: -1,
          username: this.username,
          email: this.email,
          password: MD5(this.password).toString(),
          phoneNumber: this.phoneNumber
        };
        fetch("http://127.0.0.1:8080/users/signup", httpPostOptions(model))
            .then(res => res.json())
            .then(response => {
              if(!response) {
                alert("\"This username or email has been taken.\"")
              }
              else {
                this.resetCurrentUser()
                this.$router.push("dashboard");
              }
            });
        // .catch(error => {
        //   console.error('Error:', error)
        //   alert("This username or email has been taken.")
        //   });
      }
      else
        alert("Make sure the inputted email is properly formatted and includes '@'")
    },
    getDBTransactions() {
      fetch(
            "http://127.0.0.1:8080/transactions/" +
              this.$store.state.profile.user_id,
            httpGetOptions()
          )
            .then(res => res.json())
            .then(response => {
              this.$store.dispatch("clearCurrentStoredTransactionsAction");
              let transactions = response.transactions;
              for (let i = 0; i < transactions.length; i++) {
                transactions[i].isPlaid = false;
                this.$store.dispatch("addTransactionAction", transactions[i]);
              }
            })
            .catch(error => console.error("Error:", error));
    },
    getBankConnections() {
      fetch(
            "http://127.0.0.1:8080/bank/" + this.$store.state.profile.user_id,
            httpGetOptions()
          )
            .then(res => res.json())
            .then(response => {
              let banks = response["banks"];
              let numBanks = banks.length;
              for (let i = 0; i < numBanks; i++) {
                this.$store.dispatch("addBankConnectionAction", banks[i]);
                this.getTransactions(banks[i].access_token);
              }
            })
            .catch(error => console.error("Error:", error));
    },
    getTransactions(access_token) {
      fetch(
        "http://127.0.0.1:8080/bank/" +
          this.$store.state.profile.user_id +
          "/transactions/" +
          access_token,
        httpGetOptions()
      )
        .then(res => res.json())
        .then(data => {
          data.transactions.forEach(t => {
            const model = {
              isPlaid: true,
              user_id: this.$store.state.profile.user_id,
              transaction_id: t.transaction_id,
              transaction_date: t.transaction_date,
              category_id: t.category_id,
              category: t.category,
              account_id: t.account_id,
              account: t.account,
              payee: t.payee,
              amount: -Number(t.amount),
              memo: t.memo
            };
            this.$store.dispatch("addTransactionAction", model);
          });
        });
    },
    submit() {
      if(this.login) {
        this.signin();
      }
      else {
        this.signup();
      }
    },
    resetCurrentUser() {
        this.$store.dispatch('resetAction');
    }
  },
  computed: {
    firstWord() {
      return this.login ? "Don't" : "Already";
    },
    text() {
      return this.login ? "Log in" : "Sign up";
    }
  }
};
</script>

<style scoped>
.background {
  width: 100vw;
  height: 100vh;
  display: grid;
  justify-items: center;
  align-items: center;
}
.login {
  width: 350px;
}
h1 {
  color: white;
  font-size: 3.5em;
}
input {
  background-color: transparent;
  color: white;
  font-size: 1.2em;
  width: initial;
  padding-left: 0px;
  border-bottom: 3px solid white;
  border-radius: 0;
  transition: 0.2s ease;
}
input:focus {
  border-bottom: 7px solid white;
}
.input-fields {
  padding-left: 50px;
  padding-top: 30px;
}
.input-field {
  margin-bottom: 10px;
}
::placeholder {
  /* Chrome, Firefox, Opera, Safari 10.1+ */
  color: white;
  opacity: 1; /* Firefox */
}

:-ms-input-placeholder {
  /* Internet Explorer 10-11 */
  color: white;
}

::-ms-input-placeholder {
  /* Microsoft Edge */
  color: white;
}
button {
  background-color: transparent;
  color: white;
  margin-left: 215px;
  margin-top: 30px;
  font-size: 1.6em;
  font-weight: 800;
  border: 0px white solid;
  border-radius: 5px;
  padding: 5px 10px;
}
button:focus,
button:hover {
  border: 2px white solid;
}
.alternate-action {
  margin-top: 50px;
  padding-left: 50px;
}
p {
  color: white;
  margin: 0;
  margin-bottom: 10px;
}
.button {
  cursor: pointer;
  transition: transform 0.3s ease;
}
#signup-button {
  width: 100px;
}
.button:hover {
  transform: scale(1.05);
}

.title-enter-active,
.title-leave-active {
  transition: all 0.5s ease;
}
.title-enter,
.title-leave-to {
  opacity: 0;
  transform: translateX(-200px);
}

#or {
  color: white;
  font-weight: bold;
  font-size: 1.1em;
  /* using margin instead of center align cause not everything's centered */
  margin: 25px 0 20px 170px;
}
#facebook {
  margin-left: 50px;
  width: 280px;
}
</style>