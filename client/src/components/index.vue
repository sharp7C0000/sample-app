<style lang="scss">

  #tgd-index {

    
    max-width: 1024px;
    margin   : auto;
    padding  : 100px;

    .brand, section  {
      text-align: center;
    }

    section {
      .expl {
        padding: 20px;
        margin-bottom : 100px;
      }
    }
  }

</style>

<template>
  <div id="tgd-index">
    
    <h1 class="brand">Gallery Meh</h1>

    <i class="fa fa-meh-o"></i>

    <section>
      
      <p class="expl">
        Awesome cool your twitter gallery
      </p>

      <a class="button button-outline" href="/api/auth/twitter">
        Sign in from Twitter
      </a>

    </section>
    
  </div>
</template>