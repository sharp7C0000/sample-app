<style lang="scss">

  @import "~styles/_component.scss";

  #gm-index {

    margin     : auto;
    text-align : center;
    padding-top: 150px;

    .brand {
      h1 {
        font-size: 6rem;
      }

      h5 {
        opacity: 0.75;
      }
    }

    .picture {
      margin:50px;

      i {
        font-size: 12rem;
        opacity  : 0.5;
      }
    }

    footer {
      margin-top: 60px;

      h5 {
        font-size: 1.25rem;
      }
    }

    .button-large {
      font-size  : 1.4rem;
      height     : 4.5rem;
      line-height: 4.5rem;
      padding    : 0 2rem;
    }

    // TODO : global noticifation 으로 변경
    .notification {
      position        : fixed;
      top             : 0px;
      left            : 0px;
      width           : 100%;
      height          : 50px;
      background-color: $color-danger;
      color           : white;
      line-height     : 50px;
      text-align      : center;
    }
  }

</style>

<template>
  <div id="gm-index">

    <div class="page-wrapper">
      
      <section class="brand">
        <h1>
          Gallery Meh <i class="fa fa-meh-o"></i>
        </h1>
        <h5>
          Awesome cool your twitter gallery
        </h5>
      </section>
      
      <figure class="picture">
        <i class="fa fa-picture-o"></i>
      </figure>

      <section class="login">

        <div class="notification" v-if="$store.state.index.loginRequest == 'error'">
          오류 발생!!!
        </div>

        <a class="button button-primary button-large" href="/api/auth/twitter?callbackUrl=http://localhost">
          <i class="fa fa-twitter" aria-hidden="true"></i> Sign in from Twitter
        </a>
      </section>

      <footer>
        <h5>
          Created by <a target="_blank" href="https://github.com/sharp7c0000">sharp7c0000</a>
        </h5>        
      </footer>

    </div>
  </div>
</template>

<script>

  import axios from "axios";

  export default {
    asyncData ({ store, route, router }) {
      // login 이후 callback으로 간주
      if(route.query.oauth_token && route.query.oauth_secret) {
        const loginAction = store.dispatch('login', {
          oauthToken : route.query.oauth_token,
          oauthSecret: route.query.oauth_secret
        });
        return loginAction;
      }
    },

    mounted () {
      if(this.$store.state.index.loginRequest == "success") {
        this.$router.push({
          name: "main"
        })
      } else {
        this.$router.push({
          name: "index"
        })
      }
    }
  }

</script>