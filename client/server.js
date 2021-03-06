const fs          = require('fs');
const path        = require('path');
const Hapi        = require('hapi');
const Vue         = require('vue');
const Wreck       = require('wreck');
const Bell        = require('bell');
const AuthCookie  = require('hapi-auth-cookie');
const cookie      = require('cookie');
const Inert       = require('inert');
const H2O2        = require('h2o2');
const querystring = require('querystring');

const isProd = process.env.NODE_ENV === 'production';

const resolve  = file => path.resolve(__dirname, file);
const template = fs.readFileSync(resolve('./src/index.template.html'), 'utf-8');

const { createBundleRenderer } = require('vue-server-renderer');

const server = new Hapi.Server();

let isServerStarted = false;
let renderer        = null;
let readyPromise    = null;

server.connection({ 
  host: '0.0.0.0', 
  port: 8080 
});

// server.state('session', {
//   ttl: null,
//   isSecure: true,
//   isHttpOnly: true,
//   clearInvalid: false, // remove invalid cookies
//   strictHeader: true // don't allow violations of RFC 6265
// });

function createRenderer (bundle, options) {
  return createBundleRenderer(bundle, Object.assign(options, {
   template,
    basedir: resolve('./dist'),
    runInNewContext: false
  }))
}

function render (request, reply) {

  const context = { 
    url      : request.params.param, 
    query    : request.query, 
    authToken: (() => {
      if(request.headers.cookie) {
        return cookie.parse(request.headers.cookie).gm_authToken 
      }
    })()
  };

  renderer.renderToString(context, (err, html) => {
    if (err) {
      if (err.code === 404) {
        return reply('Page not found').code(404);
      } else if (err.code === 400) {
        return reply('Bad request').code(400);
      } else {
        return reply('Internal Server Error').code(500);
      }
    }
    reply(html);
    if (!isProd) {
      console.log(`whole request: ${Date.now()}ms`)
    }
  })
}

function startServer () {
  // Start the server
  if(!isServerStarted) {
    server.start((err) => {
      if (err) {
        throw err;
      }
      console.log('Server running at:', server.info.uri);
    });

    isServerStarted = true;
  }
}

// register plugin
server.register([Inert, H2O2], (err) => {

  // // auth information setting
  // server.auth.strategy("session", "cookie", {
  //   password  : 'a7d5c3c8-7fba-11e7-bb31-be2e44b06b34',
  //   isSameSite: "Lax",
  //   isSecure  : false
  // });

  // server.auth.strategy('twitter', 'bell', {
  //   provider    : 'twitter',
  //   password    : 'a7d5c3c8-7fba-11e7-bb31-be2e44b06b34',
  //   clientId    : 'bGKjn0l5Zu92zTBRruN1U8YCo',
  //   clientSecret: 'GcdeXsQccarT66eMgVzG9pG8WUPu0XWvHLKw3icyFcd9vpL57G',
  //   isSecure    : false
  // });

  ///////////////// routers /////////////////////////////////

  // api proxy
  const proxyHandler = {
    proxy: {
      redirects  : 5,
      passThrough: true,
      mapUri: function (request, callback) {
        callback(null, `http://localhost:9000/${request.params.param}?${querystring.stringify(request.query)}`);
      }
    }
  }
  
  server.route({
    method: 'GET',
    path: '/api/{param*}',
    handler: proxyHandler
  });

  server.route({
    method: '*',
    path: '/api/{param*}',
    handler: proxyHandler
  });

  server.route({
    method: 'GET',
    path: '/dist/{param*}',
    handler: {
      directory: {
        path: './dist'
      }
    }
  });

  server.route({
    method : 'GET',
    path   : '/manifest.json',
    handler: {
      file: function (request) {
        return "./manifest.json";
      }
    }
  });

  server.route({
    method: "GET",
    path  : "/auth/callback",
    config: {
      handler: function (request, reply) {
        // Do Login
        Wreck.post("http://localhost:8080/api/auth/authorize", {
          headers: {
            "Content-Type": "application/json"
          },
          payload: JSON.stringify({
            oauthToken : request.query.oauth_token,
            oauthSecret: request.query.oauth_secret
          })
        }, function(err, res, payload) {
          if(err) {
            reply("Cannot Login").code(400);
          } else {
            
            const result = JSON.parse(payload.toString());

            const cookieOptions = {
              ttl         : result.expired,
              encoding    : 'none',
              isHttpOnly  : false,
              isSameSite  : false,
              strictHeader: false,
              isSecure    : false,
              path        : "/"
            };
            
            reply.redirect("/").state('gm_authToken', result.authToken, cookieOptions);
          }
        });
      }
    }
  })

  server.route({
    method: "GET",
    path  : "/{param*}",
    config: {
      handler: function (request, reply) {
        if(isProd) {
          render(request, reply);
        } else {
          readyPromise.then(() => {
            render(request, reply);
          })
        }
      }
    },
  });

  // // twitter auth
  // server.route({
  //   method: 'GET',
  //   path  : '/auth/twitter',
  //   config: {
  //     auth   : 'twitter',
  //     handler: function(request, reply) {

  //       if (!request.auth.isAuthenticated) {
  //         return reply(Boom.unauthorized('Authentication failed: ' + request.auth.error.message));
  //       }

  //       const profile = request.auth.credentials.profile;

  //       request.cookieAuth.set({
  //         twitterId  : profile.id,
  //         username   : profile.username,
  //         displayName: profile.displayName
  //       });

  //       return reply.redirect('/');
  //     }
  //   }
  // });

  // // logout
  // server.route({
  //   method : "GET",
  //   path   : "/logout",
  //   config: {
  //     handler: function (request, reply) {
  //       request.cookieAuth.clear();
  //       reply.redirect('/');
  //     }
  //   }
  // });

  // start server

  if (isProd) {
    const bundle         = require('./dist/vue-ssr-server-bundle.json');
    const clientManifest = require('./dist/vue-ssr-client-manifest.json');

    renderer = createRenderer(bundle, { clientManifest });
    startServer();
  
  } else {
    readyPromise = require('./build/setup-dev-server')(server, (bundle, options) => {
      renderer = createRenderer(bundle, options);
      startServer();
    })
  }

});
