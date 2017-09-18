<style lang="scss">

  #tgd-index {

    color: white;

    max-width: 1024px;
    margin   : auto;
    padding  : 120px;

    .brand, section, footer  {
      text-align: center;
    }

    .brand {
      font-size: 6rem;
    }

    section {
      .expl {
        padding: 20px;
        margin-bottom : 60px;
      }
    }
  }

</style>

<template>
  <div id="tgd-index">
    
    <h1 class="brand">
      Gallery Meh <i class="fa fa-meh-o"></i>
    </h1>

    <section style="margin-bottom:60px">
      
      <p class="expl">
        Awesome cool your twitter gallery<br><br>
        <i class="fa fa-picture-o" aria-hidden="true" style="font-size:12rem;opacity:0.5"></i>
      </p>

      <section>
        
      </section>

      <a class="button button-primary" href="/api/auth/twitter">
        <i class="fa fa-twitter" aria-hidden="true"></i> Sign in from Twitter
      </a>

    </section>

    <footer>
      github: sharp7c0timesfour
    </footer>
    
  </div>
</template>