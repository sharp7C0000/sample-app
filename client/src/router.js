import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

// route-level code splitting
const Index = () => import("./components/index.vue")
const Login = () => import('./components/login.vue')
const Main  = () => import('./components/main.vue')

export function createRouter () {
  return new Router({
    mode: 'history',
    routes: [
      { path: '/login', name: "login", component: Login},
      { path: "/app"  , name: "main" , component: Main },
      { path: '/'     , name: "index", component: Index },
    ]
  })
}