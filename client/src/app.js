import Vue from 'vue'
import App from './app.vue'

import { createRouter } from './router';
import { createStore }  from './store/index.js';
import { sync }         from 'vuex-router-sync';

export function createApp () {
  
  // create router
  const router = createRouter();
  const store  = createStore();

  // router.beforeEach((to, from, next) => {
  //  // console.log("!!!!!", global.localStorage, document.cookie);
  //   if (to.matched.some(record => record.meta.requiresAuth)) {
  //     // this route requires auth, check if logged in
  //     // if not, redirect to login page.
  //     if (!auth.loggedIn()) {
  //       next({
  //         name: "index"
  //       })
  //     } else {
  //       next()
  //     }
  //   } else {
  //     next() // make sure to always call next()!
  //   }
  // })


  sync(store, router);

  const app = new Vue({
    router,
    store,
    render: h => h(App)
  })
  
  return { app, router, store }
}