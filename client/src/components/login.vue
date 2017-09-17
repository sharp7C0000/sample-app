<template>
  <div>
    <h1>Need Login2</h1>
    
    <section>
      <a href="/api/auth/twitter" type="button">Twitter Login</a>
    </section>
  </div>
</template>

<script>

  import axios from "axios";

  export default {
    asyncData ({ store, route,router }) {
      console.log("asyncing");

      if(route.query.oauth_token) {
        return new Promise((resolve, reject) => {
          axios.defaults.withCredentials = true;
          //console.log("asyning !!!!", store, route);
          // return the Promise from the action
          //return store.dispatch('fetchItem', route.params.id)
          axios.post('http://0.0.0.0:8080/api/auth/authorize', {
            oauthToken : route.query.oauth_token,
            oauthSecret: route.query.oauth_secret
          })
          .then((response) => {
            console.log(response.data, "fuck you", route);
            //window.location = "/page1";
            router.push({
              name: "p1"
            })
            resolve();
          })
          .catch((error) => {
            console.log(error);
            //console.log(error.response.status);
            reject();
          });
        })
        
      }
    },
  }

</script>