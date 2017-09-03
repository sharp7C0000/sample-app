const path         = require('path');
const webpack      = require('webpack');
const MFS          = require('memory-fs');
const clientConfig = require('./webpack.client.config');
const serverConfig = require('./webpack.server.config');
const fs           = require('fs');

const WebpackPlugin = require('hapi-webpack-plugin');

const readFile = (fs, file) => {
  try {
    return fs.readFileSync(path.join(clientConfig.output.path, file), 'utf-8')
  } catch (e) {}
}

module.exports = function setupDevServer (server, cb) {

  let bundle, clientManifest
  let resolve
  const readyPromise = new Promise(r => { resolve = r })
  const ready = (...args) => {
    resolve()
    cb(...args)
  }

  clientConfig.output.filename = '[name].js';

  watchOption = {
    aggregateTimeout: 300,
    poll            : 1000
  };

  // dev middleware
  const clientCompiler = webpack(clientConfig);
  const cMfs           = new MFS();

  clientCompiler.outputFileSystem = cMfs;

  server.register({
    register: WebpackPlugin,
    options : {
      compiler: clientCompiler, assets: { 
        publicPath  : clientConfig.output.publicPath,
        watchOptions: watchOption
      } 
    }
  });

  clientCompiler.plugin('done', stats => {
    stats = stats.toJson()
    stats.errors.forEach(err => console.error(err))
    stats.warnings.forEach(err => console.warn(err))
    if (stats.errors.length) return
    clientManifest = JSON.parse(readFile(
     cMfs,
      'vue-ssr-client-manifest.json'
    ));

    if (bundle) {
      ready(bundle, {
        clientManifest
      })
    }
  });

  // watch and update server renderer
  const serverCompiler = webpack(serverConfig)
  const mfs = new MFS()
  serverCompiler.outputFileSystem = mfs
  serverCompiler.watch(watchOption, (err, stats) => {
    if (err) throw err

    console.log(stats.toString({
      errorDetails: true
    }));
    
    stats = stats.toJson()

    if (stats.errors.length) return

    // read bundle generated by vue-ssr-webpack-plugin
    bundle = JSON.parse(readFile(mfs, 'vue-ssr-server-bundle.json'))
    if (clientManifest) {
      ready(bundle, {
        clientManifest
      })
    }
  })

  return readyPromise
}