<template>
  <div>Login..</div>
</template>

<script>

  import {mapActions, mapState} from "vuex";

  import axios from "axios";

  import LoginModule from "store/modules/login";

  export default {

    computed: {
      ...mapState({
        loginRequest: (state) => state.login.request
      })
    },

    methods: {
      ...mapActions([
        "login"
      ])
    },
    
    asyncData ({ store, route, router }) {
      store.registerModule('login', LoginModule);
      return store.dispatch('login', {
        oauthToken : route.query.oauth_token,
        oauthSecret: route.query.oauth_secret
      });
    },

    created () {
      if(this.loginRequest.status == 'success') {
        this.$router.push({
          name: "main"
        });
      }
    },

    destroyed () {
      this.$store.unregisterModule('login')
    }
  }

</script>