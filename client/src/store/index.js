import Vue from 'vue';
import Vuex from 'vuex';

import * as Api from "api";

import AuthModule  from "./modules/auth";

Vue.use(Vuex);

export function createStore (router) {
  return new Vuex.Store({
    
    state: {
      
    },

    actions: {

      callApi ({state, getters, dispatch}, {name, options}) {
        const apiInfo = Api[name];
        if(apiInfo.auth) {

          return new Promise((resolve, reject) => {
            return Api.authedFetch(Object.assign({}, {
              url      : apiInfo.url,
              method   : apiInfo.method,
              authToken: getters.isAuthed
            }, options))
            .then((result) => {
              resolve(result);
            })
            .catch((error) => {
              // unauthorize
              if(error.code == 401) {
                dispatch("discardAuthToken");
                router.replace("/");
              }
              reject(error);
            })
          });
        } else {
          return Api.fetch(Object.assign({}, {
            url   : apiInfo.url,
            method: apiInfo.method
          }, options))
        }
      },

      init ({dispatch, commit}) {
        return new Promise((resolve, reject) => {
          dispatch("callApi", {
            name: "init"
          })
          .then((result) => {
            console.log('@@@@@@', result);
            resolve();
          })
          .catch((error) => {
            reject(error);
          })
        });        
      }
    
    },

    modules: {
      auth : AuthModule
    }
  })
}