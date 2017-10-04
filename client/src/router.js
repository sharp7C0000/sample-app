import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export function createRouter () {
  return new Router({
    mode: 'history',
    routes: [
      
      // index
      { 
        path     : '/', 
        name     : "index", 
        component: () => import("./components/index.vue"),
        meta: {
          excludesAuth: true
        }
      },

      // login
      { 
        path     : '/login', 
        name     : "login", 
        component: () => import('./components/login.vue'),
        meta: {
          excludesAuth: true
        }
      },

      // app
      { 
        path     : "/app", 
        name     : "main", 
        component: () => import('./components/main.vue'),
        meta     : {
          requiresAuth: true
        }
      }
    ]
  })
}