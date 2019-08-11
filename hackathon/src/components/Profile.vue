<template>
  <div class="container-1">
    <main>
      <div id="container-2">
        <h2>My Profile</h2>
        <div id="grid">
          <i class="fa fa-user" aria-hidden="true"></i>
          <input
                  type="text"
                  class="form-control"
                  placeholder="Username"
                  required="true"
                  v-model="ProfileModel.username"
          >
          <i class="fa fa-envelope" aria-hidden="true"></i>
          <input
                  type="email"
                  class="form-control"
                  placeholder="Email Address"
                  v-model="ProfileModel.email"
          >
          <i class="fa fa-lock" aria-hidden="true"></i>
          <input
                  type="password"
                  placeholder="Password"
                  class="form-control"
                  required="true"
                  v-model="ProfileModel.password"
          >
          <i class="fa fa-phone" aria-hidden="true"></i>
          <input
                  type="number"
                  placeholder="Phone Number"
                  class="form-control"
                  v-model="ProfileModel.phoneNumber"
          >
        </div>
        <div id="container" class="initial-view">
          <span>Unlink selected bank: </span>
          <select>
            <option></option>
            <option v-for="connection of this.$store.state.connections" @click="setSelected(connection)">{{ connection.primary_key.split("---")[1] }}</option>
          </select>
          <button class="btn btn-md btn-success" @click="unlink()">Unlink account</button>
          <br>
          <button id="link-btn" class="button button--is-primary">Connect with Plaid</button>
        </div>
        <button class="btn btn-md btn-success float-center" @click="update()">Update Profile</button>
      </div>
    </main>
  </div>
</template>

<script>
  import { httpGetOptions, httpPostOptions } from "../lib/http";
  import MD5 from "crypto-js/md5";

  let PLAID_OPTIONS = null;

  export default {
    name: "profile",
    mounted() {
      if(this.$store.state.profile.username === "")
        this.$router.push({ path: "/" });
      fetch("http://127.0.0.1:8080/bank", httpGetOptions())
              .then(res => res.json())
              .then(response => {
                PLAID_OPTIONS = response;
                var self = this;
                (function($) {
                  var products = response.PLAID_PRODUCTS.split(',');
                  if (products.includes('assets')) {
                    $('#assets').show();
                  }

                  var handler = Plaid.create({
                    apiVersion: 'v2',
                    clientName: 'Plaid Quickstart',
                    env: response.PLAID_ENV,
                    product: products,
                    key: response.PLAID_PUBLIC_KEY,
                    countryCodes: response.PLAID_COUNTRY_CODES.split(','),
                    // webhook: 'https://your-domain.tld/plaid-webhook',
                    onSuccess: (public_token, metadata) => {
                      var model = {
                        public_token: public_token
                      };
                      fetch("http://127.0.0.1:8080/bank/token/get_access_token", httpPostOptions(model))
                              .then(res => res.json())
                              .then(data => {
                                var accountModel = {
                                  user_id: self.$store.state.profile.user_id,
                                  access_token: data.access_token,
                                  item_id: data.item_id,
                                  primary_key: self.$store.state.profile.username + "---" + metadata.institution.name
                                };
                                fetch("http://127.0.0.1:8080/bank/new", httpPostOptions(accountModel));
                                self.$store.dispatch("AddBankConnectionAction", accountModel)
                                self.getTransactions(data.access_token);
                              });
                    },
                  });

                  var accessToken = qs('access_token');
                  if (accessToken != null && accessToken != '') {
                    var model = {
                      access_token: accessToken
                    };
                    fetch("http://127.0.0.1:8080/bank/token/set_access_token", httpPostOptions(model))
                            .then(res => res.json())
                            .then(function(data) {
                              $('#container').fadeOut('fast', function() {
                                $('#item_id').text(data.item_id);
                                $('#access_token').text(accessToken);
                                $('#intro').hide();
                                $('#app, #steps').fadeIn('slow');
                              });
                            });
                  }

                  $('#link-btn').on('click', function(e) {
                    handler.open();
                  });
                })(jQuery);
              });
    },
    data() {
      return {
        ProfileModel: {
          username: this.$store.state.profile.username,
          email: this.$store.state.profile.email,
          password: this.$store.state.profile.password,
          phoneNumber: this.$store.state.profile.phoneNumber
        },
        BankConnectionModel: {
          selected: ''
        }
      };
    },
    methods: {
      update() {
        if(this.ProfileModel.username !== "" && this.ProfileModel.email.match("@") && this.ProfileModel.password.trim().length >= 6) {
          if(this.ProfileModel.phoneNumber !== "" && this.ProfileModel.phoneNumber.trim().length !== 10) {
            this.ProfileModel.phoneNumber = "";
          }
          const model = {
            user_id: this.$store.state.profile.user_id,
            username: this.ProfileModel.username,
            email: this.ProfileModel.email,
            password: MD5(this.ProfileModel.password).toString(),
            phoneNumber: this.ProfileModel.phoneNumber
          };
          fetch("http://127.0.0.1:8080/users/update/" + this.$store.state.profile.user_id, httpPostOptions(model))
                  .then(res => res.json())
                  .then(response => {
                    this.updateProfile(model)
                    alert("Successfully updated profile!")
                  }).catch(error => {
            alert("The username or email has been taken.")
          });

        }
      },
      updateProfile(model) {
        this.$store.dispatch('updateProfileAction', model);
      },
      getTransactions(access_token) {
        fetch("http://127.0.0.1:8080/bank/" + this.$store.state.profile.user_id + "/transactions/" + access_token, httpGetOptions())
                .then(res => res.json())
                .then(data => {
                  data.transactions.forEach(t => {
                    const model = {
                      id: -1,
                      username: this.$store.state.profile.username,
                      transaction_date: t.date,
                      category: t.category,
                      payee: t.payee,
                      amount: Number(t.amount),
                      memo: '',
                      account: t.account
                    };
                    this.$store.dispatch("addTransactionAction", model);
                  });
                });
      },
      setSelected(connection) {
        this.BankConnectionModel.selected = connection;
        console.log(connection)
      },
      unlink() {
        console.log(this.BankConnectionModel.selected)
      }
    }
  };

  function qs(key) {
    key = key.replace(/[*+?^$.\[\]{}()|\\\/]/g, "\\$&"); // escape RegEx meta chars
    var match = location.search.match(new RegExp("[?&]" + key + "=([^&]+)(&|$)"));
    return match && decodeURIComponent(match[1].replace(/\+/g, " "));
  }
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
  #grid {
    display: grid;
    grid-template-columns: 3em auto;
    grid-template-rows: repeat(4, 3em);
    grid-column-gap: 1em;
  }
  i {
    font-size: 2em;
    text-align: center;
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
  input {
    margin-bottom: 1em;
  }
  @media only screen and (max-width: 1100px) {
    main{
      height: 85vh;
      border-radius: 1em;
      margin: 0 5vw;
      background-color: transparent;
    }
  }
</style>
