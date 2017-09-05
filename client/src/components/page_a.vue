<template>
  <div>
    <h1>PAGE A</h1>
    <a class="button" href="/logout" type="button">Logout</a>
  </div>
</template>

<script>

  import axios from "axios";

  export default {
    
    asyncData ({ store, route }) {
      console.log("asyncing");
      //console.log("asyning !!!!", store, route);
      // return the Promise from the action
      //return store.dispatch('fetchItem', route.params.id)
      axios.get('http://0.0.0.0:8080/api/auth/twitter/signin', {
        params: {
          oauthToken   : route.query.oauth_token,
          oauthVerifier: route.query.oauth_verifier
        }
      })
      .then((response) => {
        console.log(response, "fuck you");
      })
      .catch((error) => {
        console.log(error);
        //console.log(error.response.status);
      });
    },

    created () {
      //console.log("fuck you", this.$router, this.$route.query)
    }
  }

</script>