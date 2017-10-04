import { createApp } from './app'

export default context => {
  
  return new Promise((resolve, reject) => {
    const { app, router, store } = createApp()
    
    // set auth token
    store.dispatch("setAuthToken", context.authToken);
    
    router.push({
      path : context.url,
      query: context.query
    });
    
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
      }).catch(reject)
    }, reject)
  })
}