import Vue from 'vue';
import Vuex from 'vuex';

import * as Api from "api";

import LoginModule from "./modules/login";
import AuthModule  from "./modules/auth";

Vue.use(Vuex);

export function createStore () {
  return new Vuex.Store({
    
    state: {
      
    },

    actions: {

      callApi ({state, getters, dispatch}, {name, options}) {
        const apiInfo = Api[name];
        if(apiInfo.auth) {
          return Api.authedFetch(Object.assign({}, {
            url      : apiInfo.url,
            method   : apiInfo.method,
            authToken: getters.isAuthed
          }, options))
        } else {
          return Api.fetch(Object.assign({}, {
            url   : apiInfo.url,
            method: apiInfo.method
          }, options))
        }
      },

      init ({dispatch, commit}) {

        return new Promise((reject, resolve) => {
          dispatch("callApi", {
            name: "init"
          })
          .then((result) => {
            console.log("!!!!!", result);
            reolve();
          })
          .catch((error) => {
            if(error.code == 401) {
              dispatch("discardServerToken")
  
              console.log("do not logined")

  
            }

            
            reject();
          })
        })

        
      }
    
    },

    modules: {
      auth : AuthModule,
      login: LoginModule
    }
  })
}