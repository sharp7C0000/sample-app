import Vue from 'vue'
import { createApp } from './app'

const { app, router, store } = createApp()

// state 할당
if (window.__INITIAL_STATE__) {
  store.replaceState(window.__INITIAL_STATE__)
}

// auth 상태 검사하여 페이지 리다이렉트
if(router.history.pending.meta.requiresAuth && !store.state.auth.serverToken) {
  router.replace({
    name: "index"
  })
}

if(!router.history.pending.meta.requiresAuth && store.state.auth.serverToken) {
  router.replace({
    name: "main"
  })
}

router.onReady(() => {

  router.beforeResolve((to, from, next) => {

    const matched     = router.getMatchedComponents(to)
    const prevMatched = router.getMatchedComponents(from)

    let diffed = false
    const activated = matched.filter((c, i) => {
      return diffed || (diffed = (prevMatched[i] !== c))
    })

    if (!activated.length) {
      return next()
    }

    Promise.all(activated.map(c => {
      if (c.asyncData) {
        return c.asyncData({ store, route: to })
      }
    })).then(() => {
      return next()
    }).catch(next)
  })

  app.$mount('#app');
});