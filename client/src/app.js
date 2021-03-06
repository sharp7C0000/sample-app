import Vue from 'vue'
import App from './app.vue'

import { createRouter } from './router';
import { createStore }  from './store/index.js';
import { sync }         from 'vuex-router-sync';

export function createApp () {
  
  // create router
  const router = createRouter();
  const store  = createStore(router);

  sync(store, router);

  const app = new Vue({
    router,
    store,
    render: h => h(App)
  })

  return { app, router, store }
}