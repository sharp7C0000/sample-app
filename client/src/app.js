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

  // TODO : client 접근 확인 필요
  // router.beforeEach((to, from, next) => {
  //   if(to.matched.some(record => record.meta.requiresAuth) && !store.getters.isAuthed) {
  //      next({
  //       name: "index",
  //     })
  //   } else if(to.matched.some(record => record.meta.excludesAuth) && store.getters.isAuthed) {
  //     next({
  //       name: "main"
  //     })
  //   } else {
  //     next();
  //   }
  // })

  return { app, router, store }
}