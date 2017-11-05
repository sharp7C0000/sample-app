import { createApp } from './app'

export default context => {
  
  return new Promise((resolve, reject) => {
    const { app, router, store } = createApp()

    // set auth token
    store.dispatch("setServerToken", context.authToken);

    router.push({
      path : context.url,
      query: context.query
    });

    // auth 상태 검사하여 페이지 리다이렉트
    if(router.history.pending.meta.requiresAuth && !context.authToken) {
      router.replace({
        name: "index"
      })
    }

    if(!router.history.pending.meta.requiresAuth && context.authToken) {
      router.replace({
        name: "main"
      })
    }

    router.onReady(() => {
      
      const matchedComponents = router.getMatchedComponents()

      if (!matchedComponents.length) {
        return reject({ code: 404 })
      }

      Promise.all(matchedComponents.map(Component => {
        if (Component.asyncData) {
          return Component.asyncData({
            store,
            route: router.currentRoute,
            router: router
          })
        }
      })).then(() => {
        context.state = store.state
        resolve(app)
      }).catch((e) => {
        // unauthorize..
        if(e.code == 401) {
          router.replace("/");
          context.state = store.state
          resolve(app);
        } else {
          console.log(e);
          reject({
            code: 400
          });
        }
      })
    }, reject)
  })
}