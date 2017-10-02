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
        component: () => import("./components/index.vue") 
      },

      // login
      { 
        path     : '/login', 
        name     : "login", 
        component: () => import('./components/login.vue')
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