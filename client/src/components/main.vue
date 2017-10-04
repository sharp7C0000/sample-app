<style lang="scss">

  @import "~styles/_component.scss";

  $container-width: 1024px;

  $nav-height       : 45px;
  $nav-mobile-height: 50px;

  #gm-main {
    header {
      height: $nav-height;
      border-bottom: 1px solid #ccc;
     // background-color: $color-secondary;
      //color: $color-initial;

      nav {
        height: 100%;
        max-width: $container-width;
        margin   :auto;
        //border   : 1px solid pink;

        display        : flex;
        flex-direction : row;
        justify-content: space-between;
        align-items    : center;

        .button {
          margin-bottom: 0px;
        }

        .brand {
          font-size: 2rem;
          font-weight: bold;
        }


      }

    }

    .contents-container {
      // height: 500px;
      // width: $container-width;
      // border-right: 1px solid #DDD;
      // border-left: 1px solid #DDD;
      // margin:auto;
    }
  }

</style>

<template>

  <div id="gm-main">
    <header>
      <nav>
        <div class="nav-left">
          <div class="brand">Gallery Meh <i class="fa fa-meh-o"></i></div>
        </div>

        <div class="nav-right">
          <a class="button button-clear" @click="logout">logout</a>
        </div>

        
      </nav>
    </header>
    
    <section class="contents-container">
    </section>

  </div>

</template>

<script>

  import {mapActions, mapState}    from "vuex";

  export default {
    asyncData ({ store, route, router }) {
      return store.dispatch('init');
    },

    methods: {
      ...mapActions([
        "discardAuthToken"
      ]),

      logout () {
        this.discardAuthToken();
        this.$router.replace({
          name: "index"
        })
      }
    }
  }

</script>