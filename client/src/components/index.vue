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
        <a class="button button-primary button-large" href="/api/auth/twitter?callbackUrl=http://localhost:8080/login">
          <i class="fa fa-twitter" aria-hidden="true"></i> Sign in from Twitter
        </a>
      </section>

      <router-link :to="{name: 'main'}">fuck you</router-link>

      <footer>
        <h5>
          Created by <a target="_blank" href="https://github.com/sharp7c0000">sharp7c0000</a>
        </h5>        
      </footer>

    </div>
  </div>
</template>

<script>

  export default {
    

  }

</script>