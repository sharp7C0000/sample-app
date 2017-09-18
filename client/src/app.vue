<style lang="scss">

 @import "styles/app.scss";

</style>

<template>

  <div id="app" class="body-wrapper">

    <router-view></router-view>
  
    <!--<nav>
      <h1>SSR Sample App1</h1>

      <section>
        <router-link class="button" :to="{name:'p1'}">Link1</router-link>
        <router-link class="button" :to="{name:'p2'}">Link2</router-link>
        <router-link class="button" :to="{name:'p3'}">Link3</router-link>
      </section>
    </nav>

    {{sessionInfo}}

    <router-view></router-view>

    <div>
      <a class="button" href="/api/auth/twitter/request" type="button">Twitter Login</a>
    </div>-->


  </div>

</template>

<script>

  import axios from "axios";

  //import Style from "vue-style-loader!css-loader!sass-loader!./styles/app.scss";

  import { mapState } from 'vuex';

  export default {


    computed: {
      ...mapState({
        sessionInfo: (state) => state.session
      })
    },

    created () {
      //console.log("fuck you2", this.$router, this.$route.query)
    },

    asyncData ({ store, route }) {
     // console.log("!!!!", store, route);
      // return the Promise from the action
      //return store.dispatch('fetchItem', route.params.id)
    },

  }

</script>