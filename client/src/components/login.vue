<style lang="scss">

  #gm-login {
    padding: 10px;
  }

</style>

<template>
  <div id="gm-login">
    <section>Login..</section>
  </div>
</template>

<script>

  import {mapActions, mapState}    from "vuex";
  import {status as requestStatus} from "classes/request";

  export default {

    computed: {
      ...mapState({
        loginRequest: (state) => state.login.request
      })
    },

    asyncData ({ store, route }) {
      return store.dispatch('login', {
        oauthToken : route.query.oauth_token,
        oauthSecret: route.query.oauth_secret
      });
    },

    beforeMount () {
      if(this.loginRequest.status == requestStatus.SUCCESS) {
        this.$store.dispatch("storeAuthToken", this.$store.state.login.tempToken);
        location.reload();
      }
    }
  }

</script>